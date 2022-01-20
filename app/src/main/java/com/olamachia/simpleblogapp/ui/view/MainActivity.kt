package com.olamachia.simpleblogapp.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.olamachia.simpleblogapp.R
import com.olamachia.simpleblogapp.databinding.ActivityMainBinding
import com.olamachia.simpleblogapp.model.Post
import com.olamachia.simpleblogapp.repository.Repository
import com.olamachia.simpleblogapp.room.CachedCommentMapper
import com.olamachia.simpleblogapp.room.CachedPostMapper
import com.olamachia.simpleblogapp.room.LocalDataBase
import com.olamachia.simpleblogapp.ui.adapter.PostAdapter
import com.olamachia.simpleblogapp.util.LocalListUtil.getPostList
import com.olamachia.simpleblogapp.util.Resource
import com.olamachia.simpleblogapp.viewModel.MainViewModelFactory
import com.olamachia.simpleblogapp.viewModel.MainViewModel

class MainActivity : AppCompatActivity(), PostAdapter.Interaction {
    private lateinit var binding: ActivityMainBinding
    private lateinit var progressBar: ProgressBar
    private lateinit var repository: Repository
    private lateinit var commentMapper: CachedCommentMapper
    private lateinit var postMapper: CachedPostMapper
    private lateinit var mainViewModel: MainViewModel
    private lateinit var mainViewModelFactory: MainViewModelFactory
    private lateinit var postAdapter: PostAdapter

    private var localPostList = getPostList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Set Status bar Color
        window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window?.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window?.statusBarColor = resources?.getColor(R.color.nice)!!

        //Initialise ViewModel
        val roomDatabase = LocalDataBase.getInstance(this)
        commentMapper = CachedCommentMapper()
        postMapper = CachedPostMapper()
        repository = Repository(roomDatabase, commentMapper, postMapper)
        mainViewModelFactory = MainViewModelFactory(repository)
        mainViewModel = ViewModelProvider(this, mainViewModelFactory).get(MainViewModel::class.java)
        progressBar = binding.progressBar

        //Initialise RecyclerView
        setupRecyclerView()
        loadPage()

        //Setup Search functionality
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { mainViewModel.searchPostList(it) }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { mainViewModel.searchPostList(it) }
                return false
            }
        })

        //Method To Add New Comment
        binding.fabPost.setOnClickListener {
            AddPostDialog().show(supportFragmentManager, "D")
        }

        //Setup Rcv Swipe to Refresh
        binding.swipeRefresh.setOnRefreshListener {
            loadPage()
            binding.swipeRefresh.isRefreshing = false
        }

    }

    //Initialise RecyclerView
    private fun setupRecyclerView() {
        binding.rcvPost.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            postAdapter = PostAdapter(this@MainActivity)
            adapter = postAdapter
        }

        //Scroll to Position of New Post
        postAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver(){
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                binding.rcvPost.scrollToPosition(positionStart)
            }
        })
    }

    private fun loadPage() {
        mainViewModel.postList.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let {
                        postAdapter.submitList(it)
                        localPostList = it as MutableList<Post>
                    }
                    hideProgressBar()
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
                is Resource.Error -> {
                    hideProgressBar()
                    Toast.makeText(this, "An Error Occurred", Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    override fun onItemSelected(position: Int, item: Post) {
        val currentPost = localPostList[position]

        val intent = Intent(this, CommentActivity::class.java).apply {
            putExtra(POST, currentPost)
        }
        startActivity(intent)
    }

    companion object {
        const val POST = "post"
    }
}