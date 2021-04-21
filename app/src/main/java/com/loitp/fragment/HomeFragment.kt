package com.loitp.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.annotation.LogTag
import com.core.base.BaseApplication
import com.core.base.BaseFragment
import com.core.common.Constants
import com.core.utilities.LUIUtil
import com.loitp.R
import com.loitp.adapter.AboutMeAdapter
import com.loitp.adapter.BannerAdapter
import com.loitp.adapter.LoadingAdapter
import com.loitp.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.frm_home.*
import vn.loitp.app.activity.customviews.recyclerview.concatadapter.adapter.*
import com.loitp.model.AboutMe
import com.loitp.model.DataSource
import com.loitp.model.News
import com.views.setSafeOnClickListener

@LogTag("HomeFragment")
class HomeFragment : BaseFragment() {
    private var concatAdapter: ConcatAdapter? = null
    private var aboutMeAdapter: AboutMeAdapter? = null
    private var usersAdapter: UsersAdapter? = null
    private var bannerAdapter: BannerAdapter? = null
    private var newsAdapter: NewsAdapter? = null
    private val loadingAdapter = LoadingAdapter()
    private var mainViewModel: MainViewModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupViewModels()
        context?.let {
            mainViewModel?.loadListChap(context = it)
        }
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

            mvm.listChapLiveData.observe(viewLifecycleOwner, Observer { listChap ->
                logD("<<<listChapLiveData " + BaseApplication.gson.toJson(listChap))
            })
        }

    }

    private fun setupDataInRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(context)

        aboutMeAdapter = AboutMeAdapter(ArrayList())
        usersAdapter = UsersAdapter(ArrayList())
        bannerAdapter = BannerAdapter(ArrayList())
        newsAdapter = NewsAdapter(ArrayList())

        aboutMeAdapter?.let { ama ->
            ama.onClickRootListener = { aboutMe, position ->
                aboutMe.name = System.currentTimeMillis().toString()
                ama.notifyItemChanged(position)
            }
        }

        usersAdapter?.let { ua ->
            ua.onClickRootListener = { user, position ->
                user.avatar = Constants.URL_IMG_1
                user.name = System.currentTimeMillis().toString()
                ua.notifyItemChanged(position)
            }
        }

        newsAdapter?.let { na ->
            na.onClickRootListener = { _, position ->
                showShortInformation("Click position $position")
            }
        }

        aboutMeAdapter?.let { ama ->
            usersAdapter?.let { ua ->
                bannerAdapter?.let { ba ->
                    newsAdapter?.let { na ->
                        val listOfAdapters =
                            listOf<RecyclerView.Adapter<out RecyclerView.ViewHolder>>(
                                ama,
                                ua,
                                ba,
                                na
                            )
                        concatAdapter = ConcatAdapter(listOfAdapters)
                    }
                }
            }
        }

        recyclerView.adapter = concatAdapter

        LUIUtil.setScrollChange(
            recyclerView = recyclerView,
            onTop = {
                logD("onTop")
            },
            onBottom = {
                logD("onBottom")
                showShortInformation("onBottom")
                genNewsData()
            }
        )

        btClearAll.setSafeOnClickListener {
            concatAdapter?.let { a ->
                a.adapters.let { list ->
                    list.forEach { childAdapter ->
                        a.removeAdapter(childAdapter)
                    }
                }
            }
        }
        btGenAboutMe.setSafeOnClickListener {
            val aboutMe = AboutMe(1, "Loitp93", "I'm a developer.")
            val listAboutMe = ArrayList<AboutMe>()
            listAboutMe.add(aboutMe)
            aboutMeAdapter?.setData(listAboutMe)
        }
        btGenListUser.setSafeOnClickListener {
            usersAdapter?.setData(DataSource.getListUser())
        }
        btGenBanner.setSafeOnClickListener {
            bannerAdapter?.setData(DataSource.getBanner())
        }
        btAddBannerAt0.setSafeOnClickListener {
            val newBannerAdapter = BannerAdapter(ArrayList())
            newBannerAdapter.setData(DataSource.getBanner())
            concatAdapter?.addAdapter(0, newBannerAdapter)
        }
        btAddAboutMeAtLast.setSafeOnClickListener {
            val newAboutMeAdapter = AboutMeAdapter(ArrayList())
            val aboutMe = AboutMe(1, "Loitp ^^!", "Hello world!!!")
            val listAboutMe = ArrayList<AboutMe>()
            listAboutMe.add(aboutMe)
            newAboutMeAdapter.setData(listAboutMe)
            concatAdapter?.addAdapter(newAboutMeAdapter)
        }
        btRemoveAdapterListUser.setSafeOnClickListener {
            usersAdapter?.let { ua ->
                concatAdapter?.removeAdapter(ua)
            }
        }

        genNewsData()
    }

    private fun isLoading(): Boolean {
        concatAdapter?.let {
            it.adapters.forEach { childAdapter ->
                if (childAdapter == loadingAdapter) {
                    return true
                }
            }
        }
        return false
    }

    private fun genNewsData() {
        if (!isLoading()) {
            concatAdapter?.addAdapter(loadingAdapter)
            concatAdapter?.itemCount?.let {
                recyclerView.scrollToPosition(it - 1)
            }

            LUIUtil.setDelay(mls = 2000, runnable = Runnable {
                val listNews = ArrayList<News>()
                for (i in 0..10) {
                    val news = News(
                        id = System.currentTimeMillis(),
                        title = "Title " + System.currentTimeMillis(),
                        image = Constants.URL_IMG_10
                    )
                    listNews.add(news)
                }
                concatAdapter?.removeAdapter(loadingAdapter)
                newsAdapter?.addData(listNews)
            })
        }
    }
}
