package com.loitp.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.annotation.LogTag
import com.core.base.BaseApplication
import com.core.base.BaseFragment
import com.core.common.Constants
import com.core.utilities.LUIUtil
import com.loitp.R
import com.loitp.adapter.BannerAdapter
import com.loitp.adapter.LoadingAdapter
import com.loitp.adapter.NewsAdapter
import com.loitp.model.News
import com.loitp.service.StoryApiConfiguration
import com.loitp.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.frm_home.*

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
            mvm.eventLoading.observe(viewLifecycleOwner, Observer { isLoading ->
                if (isLoading) {
                    indicatorView.smoothToShow()
                } else {
                    indicatorView.smoothToHide()
                }
            })

            mvm.listStoryLiveData.observe(viewLifecycleOwner, Observer { listTC ->
                logD("<<<listStoryLiveData " + BaseApplication.gson.toJson(listTC))
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

        genNewsData()
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
