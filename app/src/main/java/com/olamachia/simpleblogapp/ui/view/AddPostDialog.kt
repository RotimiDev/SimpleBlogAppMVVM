package com.olamachia.simpleblogapp.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.olamachia.simpleblogapp.databinding.FragmentAddPostDialogBinding
import com.olamachia.simpleblogapp.model.Post
import com.olamachia.simpleblogapp.repository.Repository
import com.olamachia.simpleblogapp.room.CachedCommentMapper
import com.olamachia.simpleblogapp.room.CachedPostMapper
import com.olamachia.simpleblogapp.room.LocalDataBase
import com.olamachia.simpleblogapp.viewModel.MainViewModelFactory
import com.olamachia.simpleblogapp.viewModel.MainViewModel

class AddPostDialog : DialogFragment() {
    private var _binding: FragmentAddPostDialogBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: Repository
    private lateinit var mainViewModel: MainViewModel
    private lateinit var commentMapper: CachedCommentMapper
    private lateinit var postMapper: CachedPostMapper
    private lateinit var viewModelMainViewModelFactory: MainViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_DeviceDefault_Light_Dialog_MinWidth)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddPostDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //Initialise ViewModel
        val roomDatabase = LocalDataBase.getInstance(requireContext())
        commentMapper = CachedCommentMapper()
        postMapper = CachedPostMapper()
        repository = Repository(roomDatabase, commentMapper, postMapper)
        viewModelMainViewModelFactory = MainViewModelFactory(repository)
        mainViewModel =
            ViewModelProvider(requireActivity(), viewModelMainViewModelFactory).get(MainViewModel::class.java)

        binding.btPost.setOnClickListener {
            val postTitle = binding.newPostTitle.text.toString()
            val postBody = binding.newPostBody.text.toString()
            val userId = (1..10).random()
            val id = 0

            when {
                postTitle.isEmpty() -> {
                    binding.newPostTitle.error = "Post Title Required"
                    return@setOnClickListener
                }
                postBody.isEmpty() -> {
                    binding.newPostBody.error = "Write A Post"
                    return@setOnClickListener
                }
                else -> {
                    val newPost = Post(userId, id, postTitle, postBody)
                    mainViewModel.addPost(newPost)
                    dismiss()
                }
            }
        }
    }
}