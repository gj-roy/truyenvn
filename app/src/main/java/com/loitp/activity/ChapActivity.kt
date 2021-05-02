package com.loitp.activity

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.annotation.IsFullScreen
import com.annotation.IsSwipeActivity
import com.annotation.LogTag
import com.core.base.BaseApplication
import com.core.base.BaseFontActivity
import com.core.common.Constants
import com.core.utilities.LActivityUtil
import com.core.utilities.LImageUtil
import com.core.utilities.LSocialUtil
import com.loitp.R
import com.loitp.model.Story
import com.loitp.service.StoryApiConfiguration
import com.loitp.viewmodels.ChapViewModel
import com.views.layout.swipeback.SwipeBackLayout.OnSwipeBackListener
import kotlinx.android.synthetic.main.activity_chap.*
import kotlinx.android.synthetic.main.frm_home.*

@LogTag("ChapActivity")
@IsFullScreen(false)
@IsSwipeActivity(true)
class ChapActivity : BaseFontActivity() {

    companion object {
        const val KEY_STORY = "KEY_STORY"
    }

    private var story: Story? = null
    private var chapViewModel: ChapViewModel? = null
    private var pageIndex = 0

    override fun setLayoutResourceId(): Int {
        return R.layout.activity_chap
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupData()
        setupViews()
        setupViewModels()

        story?.id?.let { comicId ->
            chapViewModel?.getListChap(
                comicId,
                StoryApiConfiguration.PAGE_SIZE,
                pageIndex
            )
        }
    }

    private fun setupData() {
        val tmpStory = intent.getSerializableExtra(KEY_STORY)
        if (tmpStory != null && tmpStory is Story) {
            this.story = tmpStory
        }
    }

    private fun setupViews() {
        swipeBackLayout.setSwipeBackListener(object : OnSwipeBackListener {
            override fun onViewPositionChanged(
                mView: View?,
                swipeBackFraction: Float,
                swipeBackFactor: Float
            ) {
            }

            override fun onViewSwipeFinished(mView: View?, isEnd: Boolean) {
                if (isEnd) {
                    finish()
                    LActivityUtil.transActivityNoAnimation(this@ChapActivity)
                }
            }
        })

        LImageUtil.load(context = this, any = Constants.URL_IMG, imageView = kbv)
    }

    private fun setupViewModels() {
        chapViewModel = getViewModel(ChapViewModel::class.java)
        chapViewModel?.let { mvm ->
            mvm.listStoryLiveData.observe(this, Observer { actionData ->
                logD("<<<loitpp listStoryLiveData " + BaseApplication.gson.toJson(actionData.data))
                //TODO
//                val isDoing = actionData.isDoing
//                val isSuccess = actionData.isSuccess
//                val isSwipeToRefresh = actionData.isSwipeToRefresh
//                actionData.totalPages?.let {
//                    totalPage = it
//                }
//
//                if (isDoing == true) {
//                    if (actionData?.page == 0) {
//                        indicatorView.smoothToShow()
//                    }
//                } else {
//                    if (actionData?.page == 0) {
//                        indicatorView.smoothToHide()
//                    }
//
//                    if (isSuccess == true) {
//                        swipeRefreshLayout?.isRefreshing = false
//                        concatAdapter.removeAdapter(loadingAdapter)
//                        val listStory = actionData.data
//                        if (listStory.isNullOrEmpty()) {
//                            if (storyAdapter?.itemCount == 0) {
//                                tvNoData.visibility = View.VISIBLE
//                            }
//                        } else {
//                            tvNoData.visibility = View.GONE
//
//                            //banner
//                            val listBannerStory = ArrayList<Story>()
//                            if (listStory is ArrayList) {
//                                if (listStory.size > 5) {
//                                    listBannerStory.addAll(listStory)
//                                    listBannerStory.shuffle()
//                                    bannerAdapter?.setData(listBannerStory.subList(0, 5))
//                                }
//                            }
//
//                            //list item
//                            storyAdapter?.addData(
//                                listBannerStory = listStory,
//                                isSwipeToRefresh = isSwipeToRefresh
//                            )
//                        }
//                    } else {
//                        val error = actionData.errorResponse
//                        showDialogError(error?.message ?: getString(R.string.err_unknow), Runnable {
//                            //do nothing
//                        })
//                    }
//                }
            })
        }

    }

}