package com.service.codingtest.view.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.service.codingtest.R
import com.service.codingtest.databinding.FragFavoriteBinding
import com.service.codingtest.manager.AppDB
import com.service.codingtest.network.MLog
import com.service.codingtest.view.adapters.FavoriteAdapter
import com.service.codingtest.viewmodel.FavoriteViewModel

class FavoriteFragment : Fragment() {

    private val mTAG = FavoriteFragment::class.java.name

    private var mMediaFileAdapter: FavoriteAdapter? = null

    private var mLikeReceiver: LikeReceiver? = null

    private lateinit var viewDataBinding: FragFavoriteBinding
    private val viewModel by viewModels<FavoriteViewModel>()
//
//    private val mainViewModel by activityViewModels<MainViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewDataBinding = FragFavoriteBinding.bind(inflater.inflate(R.layout.frag_favorite, container, false)).apply { viewmodel = viewModel  }
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner

        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
    }

    private fun init() {
//        initData()
        setList()
        initSwipeRefresh()
    }

    private fun setList() {
        setRecyclerViewLayoutManager()

//        mMediaFileAdapter = LikeAdapter(ArrayList<LikeEntity>(), activity!!)
//        mBinding.rvMediaFileList.adapter = mMediaFileAdapter

        setMediaDBList()
    }

    fun setMediaDBList() {
//        val folder = File(Util.getPicturesDir(activity!!))
//        val fileList = folder.listFiles()
//        val docList = mutableListOf<String>()
//
//        Observable.just(fileList)
//                .map {
//                    for (file in it)
//                        docList.add(file.path)
//                    docList
//                }
//                .subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
//                .subscribe {
//                    mMediaFileAdapter!!.submitList(it)
//                }.addTo((activity as BaseActivity).cDisposable)




        viewModel.list = AppDB.getInstance(requireContext()).favoriteDao().loadAll()

        viewModel.list.observe(viewLifecycleOwner, Observer {
            mMediaFileAdapter = FavoriteAdapter(it, requireActivity())
            viewDataBinding.rvMediaFileList.adapter = mMediaFileAdapter
//            mMediaFileAdapter!!.setData(it)
        })
    }

    private fun setRecyclerViewLayoutManager() {
        var scrollPosition = 0

        if (viewDataBinding.rvMediaFileList!!.layoutManager != null) {
            scrollPosition = (viewDataBinding.rvMediaFileList!!.layoutManager as LinearLayoutManager)
                    .findFirstCompletelyVisibleItemPosition()
        }

        val llm = LinearLayoutManager(activity)
        viewDataBinding.rvMediaFileList!!.layoutManager = llm
        val dividerItemDecoration = DividerItemDecoration(activity, llm.orientation)
        viewDataBinding.rvMediaFileList.addItemDecoration(dividerItemDecoration)

        viewDataBinding.rvMediaFileList!!.scrollToPosition(scrollPosition)
    }

    private fun initSwipeRefresh() {
        viewDataBinding.layoutSwipeRefresh.setColorSchemeColors(Color.parseColor("#58be17"))
        viewDataBinding.layoutSwipeRefresh.setOnRefreshListener {
            viewDataBinding.layoutSwipeRefresh.isRefreshing = false
            setMediaDBList()
        }
    }

    override fun onDestroy() {
        MLog.d(mTAG, "onDestroy()")

        if (mLikeReceiver != null) {
            requireActivity().unregisterReceiver(mLikeReceiver)
            mLikeReceiver = null
        }

        super.onDestroy()
    }

    inner class LikeReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
//            setMediaDBList()
        }
    }
}