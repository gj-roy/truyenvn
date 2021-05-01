package com.loitp.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.annotation.LogTag
import com.core.base.BaseFragment
import com.core.common.Constants
import com.core.utilities.LActivityUtil
import com.core.utilities.LUIUtil
import com.loitp.R
import com.loitp.activity.SwipeBackLayoutActivity
import com.loitp.adapter.StoryAdapter
import com.loitp.adapter.LoadingAdapter
import com.loitp.adapter.BannerAdapter
import com.loitp.model.Story
import com.loitp.service.StoryApiConfiguration
import com.loitp.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.frm_home.*
import kotlinx.android.synthetic.main.frm_home.indicatorView

//TODO request api search
@LogTag("HomeFragment")
class HomeFragment : BaseFragment() {

    private var concatAdapter = ConcatAdapter()
    private var storyAdapter: StoryAdapter? = null
    private var bannerAdapter: BannerAdapter? = null
    private val loadingAdapter = LoadingAdapter()
    private var mainViewModel: MainViewModel? = null
    private var pageIndex = 0
    private var totalPage = Int.MAX_VALUE

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupViewModels()

        mainViewModel?.getListStory(
            pageSize = StoryApiConfiguration.PAGE_SIZE,
            pageIndex = pageIndex,
            isRefresh = false
        )
    }

    override fun setLayoutResourceId(): Int {
        return R.layout.frm_home
    }

    private fun setupViews() {
        swipeRefreshLayout.setOnRefreshListener {
            pageIndex = 0
            mainViewModel?.getListStory(
                pageSize = StoryApiConfiguration.PAGE_SIZE,
                pageIndex = pageIndex,
                isRefresh = true
            )
        }
        setupDataInRecyclerView()
    }

    private fun setupViewModels() {
        mainViewModel = getViewModel(MainViewModel::class.java)
        mainViewModel?.let { mvm ->
            mvm.listStoryLiveData.observe(viewLifecycleOwner, Observer { actionData ->
//                logD("<<<listStoryLiveData " + BaseApplication.gson.toJson(actionData.data))
                val isDoing = actionData.isDoing
                val isSuccess = actionData.isSuccess
                val isSwipeToRefresh = actionData.isSwipeToRefresh
                actionData.totalPages?.let {
                    totalPage = it
                }

                if (isDoing == true) {
                    if (actionData?.page == 0) {
                        indicatorView.smoothToShow()
                    }
                } else {
                    if (actionData?.page == 0) {
                        indicatorView.smoothToHide()
                    }

                    if (isSuccess == true) {
                        swipeRefreshLayout?.isRefreshing = false
                        concatAdapter.removeAdapter(loadingAdapter)
                        val listStory = actionData.data
                        if (listStory.isNullOrEmpty()) {
                            if (storyAdapter?.itemCount == 0) {
                                tvNoData.visibility = View.VISIBLE
                            }
                        } else {
                            tvNoData.visibility = View.GONE

                            //banner
                            val listBannerStory = ArrayList<Story>()
                            if (listStory is ArrayList) {
                                if (listStory.size > 5) {
                                    listBannerStory.addAll(listStory)
                                    listBannerStory.shuffle()
                                    bannerAdapter?.setData(listBannerStory.subList(0, 5))
                                }
                            }

                            //list item
                            storyAdapter?.addData(
                                listBannerStory = listStory,
                                isSwipeToRefresh = isSwipeToRefresh
                            )
                        }
                    } else {
                        val error = actionData.errorResponse
                        showDialogError(error?.message ?: getString(R.string.err_unknow), Runnable {
                            //do nothing
                        })
                    }
                }
            })
        }

    }

    private fun setupDataInRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(context)

        storyAdapter = StoryAdapter(ArrayList())
        bannerAdapter =
            BannerAdapter(fragmentManager = childFragmentManager, listStoryBanner = ArrayList())

        bannerAdapter?.let { na ->
            na.onClickRootListener = { story, _ ->
                goToReadScreen(story)
            }
        }

        storyAdapter?.let { na ->
            na.onClickRootListener = { story, _ ->
                goToReadScreen(story)
            }
        }

        bannerAdapter?.let {
            concatAdapter.addAdapter(it)
        }
        storyAdapter?.let {
            concatAdapter.addAdapter(it)
        }

        recyclerView.adapter = concatAdapter

        LUIUtil.setScrollChange(
            recyclerView = recyclerView,
            onTop = {
                logD("onTop")
            },
            onBottom = {
                logD("onBottom")
                loadMore()
            }
        )
    }

    private fun isLoading(): Boolean {
        concatAdapter.adapters.forEach { childAdapter ->
            if (childAdapter == loadingAdapter) {
                return true
            }
        }
        return false
    }

    private fun loadMore() {
//        logE("loadMore pageIndex $pageIndex, totalPage $totalPage, isLoading() ${isLoading()}")
        if (pageIndex >= totalPage) {
            return
        }
        if (!isLoading()) {
            concatAdapter.addAdapter(loadingAdapter)
            concatAdapter.itemCount.let {
                recyclerView.scrollToPosition(it - 1)
            }
            pageIndex++
            mainViewModel?.getListStory(
                pageSize = StoryApiConfiguration.PAGE_SIZE,
                pageIndex = pageIndex,
                isRefresh = false
            )
        }
    }

    private fun goToReadScreen(story: Story) {
        val intent = Intent(context, SwipeBackLayoutActivity::class.java)
        startActivity(intent)
        LActivityUtil.tranIn(context)

    }
}
