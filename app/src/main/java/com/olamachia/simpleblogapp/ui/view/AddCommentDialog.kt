package com.olamachia.simpleblogapp.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.olamachia.simpleblogapp.databinding.ActivityAddCommentDialogBinding
import com.olamachia.simpleblogapp.model.Comment
import com.olamachia.simpleblogapp.model.Post
import com.olamachia.simpleblogapp.repository.Repository
import com.olamachia.simpleblogapp.room.CachedCommentMapper
import com.olamachia.simpleblogapp.room.CachedPostMapper
import com.olamachia.simpleblogapp.room.LocalDataBase
import com.olamachia.simpleblogapp.viewModel.MainViewModel
import com.olamachia.simpleblogapp.viewModel.MainViewModelFactory

class AddCommentDialog(private val post: Post) : DialogFragment() {
    private var _binding: ActivityAddCommentDialogBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: Repository
    private lateinit var viewModel: MainViewModel
    private lateinit var commentMapper: CachedCommentMapper
    private lateinit var postMapper: CachedPostMapper
    private lateinit var viewModelFactory: MainViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_DeviceDefault_Light_Dialog_MinWidth)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = ActivityAddCommentDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //Initialise ViewModel
        val roomDatabase = LocalDataBase.getInstance(requireContext())
        commentMapper = CachedCommentMapper()
        postMapper = CachedPostMapper()
        repository = Repository(roomDatabase, commentMapper, postMapper)
        viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        binding.btComment.setOnClickListener {
            val name = binding.commenterName.text.toString()
            val email = binding.emailAddress.text.toString()
            val comments = binding.newComment.text.toString()
            val id = 0
            val postId = post.id


            when {
                name.isEmpty() -> {
                    binding.commenterName.error = "Name Is Required"
                    return@setOnClickListener
                }
                email.isEmpty() -> {
                    binding.emailAddress.error = "Email Is Required"
                    return@setOnClickListener
                }
                comments.isEmpty() -> {
                    binding.newComment.error = "Input Comment"
                    return@setOnClickListener
                }
                else -> {
                    val newComment = Comment(comments, email, id, name, postId)
                    viewModel.pushComment(newComment)
                    dismiss()
                }
            }
        }
    }
}