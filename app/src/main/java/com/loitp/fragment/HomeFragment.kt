package com.loitp.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.annotation.LogTag
import com.core.base.BaseApplication
import com.core.base.BaseFragment
import com.core.common.Constants
import com.core.utilities.LActivityUtil
import com.core.utilities.LUIUtil
import com.loitp.R
import com.loitp.activity.MainActivity
import com.loitp.adapter.BannerAdapter
import com.loitp.adapter.LoadingAdapter
import com.loitp.adapter.NewsAdapter
import com.loitp.model.News
import com.loitp.model.Story
import com.loitp.service.StoryApiClient
import com.loitp.service.StoryApiConfiguration
import com.loitp.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.frm_home.*
import kotlinx.android.synthetic.main.frm_home.indicatorView

//TODO refresh layout
@LogTag("HomeFragment")
class HomeFragment : BaseFragment() {

    private var concatAdapter = ConcatAdapter()
    private var bannerAdapter: BannerAdapter? = null
    private var newsAdapter: NewsAdapter? = null
    private val loadingAdapter = LoadingAdapter()
    private var mainViewModel: MainViewModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupViewModels()

        mainViewModel?.getListStory(StoryApiConfiguration.PAGE_SIZE, 0)
    }

    override fun setLayoutResourceId(): Int {
        return R.layout.frm_home
    }

    private fun setupViews() {
        setupDataInRecyclerView()
    }

    private fun setupViewModels() {
        mainViewModel = getViewModel(MainViewModel::class.java)
        mainViewModel?.let { mvm ->
            mvm.listStoryLiveData.observe(viewLifecycleOwner, Observer { actionData ->
//                logD("<<<listStoryLiveData " + BaseApplication.gson.toJson(actionData.data))
                val isDoing = actionData.isDoing
                val isSuccess = actionData.isSuccess
                if (isDoing == true) {
                    indicatorView.smoothToShow()
                } else {
                    indicatorView.smoothToHide()

                    if (isSuccess == true) {

                        val listStory = actionData.data
                        if (listStory.isNullOrEmpty()) {
                            tvNoData.visibility = View.VISIBLE
                        } else {
                            tvNoData.visibility = View.GONE
                            val listBannerStory = ArrayList<Story>()
                            if (listStory is ArrayList) {
                                if (listStory.size > 5) {
                                    listBannerStory.addAll(listStory)
                                    listBannerStory.shuffle()
                                    bannerAdapter?.setData(listBannerStory.subList(0, 5))
                                }
                            }
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

        bannerAdapter = BannerAdapter(ArrayList())
        newsAdapter = NewsAdapter(ArrayList())

        newsAdapter?.let { na ->
            na.onClickRootListener = { _, position ->
                showShortInformation("Click position $position")
            }
        }

        bannerAdapter?.let {
            concatAdapter.addAdapter(it)
        }
        newsAdapter?.let {
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
                //TODO
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

    private fun genNewsData() {
        if (!isLoading()) {
            concatAdapter.addAdapter(loadingAdapter)
            concatAdapter.itemCount.let {
                recyclerView.scrollToPosition(it - 1)
            }

            val listNews = ArrayList<News>()
            for (i in 0..10) {
                val news = News(
                    id = System.currentTimeMillis(),
                    title = "Title " + System.currentTimeMillis(),
                    image = Constants.URL_IMG_10
                )
                listNews.add(news)
            }
            concatAdapter.removeAdapter(loadingAdapter)
            newsAdapter?.addData(listNews)
        }
    }
}
