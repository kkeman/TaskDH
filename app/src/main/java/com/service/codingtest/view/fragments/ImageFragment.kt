package com.service.codingtest.view.fragments

import android.app.Application
import android.os.Bundle
import android.view.*
import androidx.activity.viewModels
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.paging.LoadState
import androidx.savedstate.SavedStateRegistryOwner
import com.service.codingtest.R
import com.service.codingtest.databinding.FragImageBinding
import com.service.codingtest.manager.AppDB
import com.service.codingtest.network.Constant
import com.service.codingtest.network.ImageAPI
import com.service.codingtest.repository.DbImagePostRepository
import com.service.codingtest.view.adapters.ImageAdapter
import com.service.codingtest.view.adapters.ImageLoadStateAdapter
import com.service.codingtest.viewmodel.ImageListViewModel
import kotlinx.android.synthetic.main.frag_image.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter


class ImageFragment : Fragment() {

    private lateinit var binding: FragImageBinding

    private lateinit var adapter: ImageAdapter

//    private val model: ImageListViewModel by viewModels {
//        object : AbstractSavedStateViewModelFactory(this, null) {
//            override fun <T : ViewModel?> create(
//                key: String,
//                modelClass: Class<T>,
//                handle: SavedStateHandle
//            ): T {
//                @Suppress("UNCHECKED_CAST")
//                return ImageListViewModel(handle) as T
//            }
//        }
//    }

    class MainViewModelFactory(owner: SavedStateRegistryOwner, private val documentRepository :DbImagePostRepository) : AbstractSavedStateViewModelFactory(owner, null) {
        override fun <T : ViewModel?> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T {
//            val repoTypeParam = intent.getIntExtra(KEY_REPOSITORY_TYPE, 0)
//            val repoType = RedditPostRepository.Type.values()[repoTypeParam]
//            val repo = ServiceLocator.instance(this@RedditActivity)
//                .getRepository(repoType)
            @Suppress("UNCHECKED_CAST")
            return ImageListViewModel(handle, documentRepository) as T
        }
//        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//            if (modelClass.isAssignableFrom(ImageListViewModel::class.java)) {
//                return ImageListViewModel(AppDB.getInstance(context.applicationContext as Application)) as T
//            }
//            throw IllegalArgumentException("Unknown ViewModel class")
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.frag_image, container, false)
        binding = FragImageBinding.bind(view)
//        binding.vm = ViewModelProvider(this, defaultViewModelProviderFactory)
        binding.vm = ViewModelProvider(requireActivity(), MainViewModelFactory(this, DbImagePostRepository(AppDB.getInstance(context!!.applicationContext as Application), ImageAPI.create())))
            .get(ImageListViewModel::class.java)
//        AppDB.getInstance(context.applicationContext as Application)



        //        ImageListViewModel(this, DbImagePostRepository(AppDB.getInstance(context.applicationContext as Application), ImageAPI.create()))
//        DbImagePostRepository(AppDB.getInstance(context.applicationContext as Application), ImageAPI.create())

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initImageListView()
        initSwipeToRefresh()
        initSearchEditText()
    }

    private fun initImageListView() {
        adapter = ImageAdapter()
        rv_image.adapter = adapter.withLoadStateHeaderAndFooter(
            header = ImageLoadStateAdapter(adapter),
            footer = ImageLoadStateAdapter(adapter)
        )

        binding.vm!!.apply {
            lifecycleScope.launchWhenCreated {
                @OptIn(ExperimentalCoroutinesApi::class)
                posts.collectLatest {
                    adapter.submitData(it)
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            @OptIn(ExperimentalCoroutinesApi::class)
            adapter.loadStateFlow.collectLatest { loadStates ->
                layout_swipe_refresh.isRefreshing = loadStates.refresh is LoadState.Loading
            }
        }

        lifecycleScope.launchWhenCreated {
            @OptIn(FlowPreview::class)
            adapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .filter { it.refresh is LoadState.NotLoading }
                .collect { rv_image.scrollToPosition(0) }
        }
    }

    private fun initSwipeToRefresh() {
        layout_swipe_refresh.setOnRefreshListener { adapter.refresh() }
    }

    private fun initSearchEditText() =
        et_search.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                binding.vm!!.showSubreddit()
                return@OnKeyListener true
            }
            false
        })
}