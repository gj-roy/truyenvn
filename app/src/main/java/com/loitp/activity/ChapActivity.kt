package com.loitp.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.annotation.IsFullScreen
import com.annotation.IsSwipeActivity
import com.annotation.LogTag
import com.core.base.BaseApplication
import com.core.base.BaseFontActivity
import com.core.utilities.LActivityUtil
import com.core.utilities.LImageUtil
import com.core.utilities.LSocialUtil
import com.core.utilities.LUIUtil
import com.loitp.R
import com.loitp.model.Story
import com.loitp.service.StoryApiConfiguration
import com.loitp.viewmodels.ChapViewModel
import com.views.layout.swipeback.SwipeBackLayout.OnSwipeBackListener
import com.views.setSafeOnClickListener
import kotlinx.android.synthetic.main.activity_chap.*
import kotlinx.android.synthetic.main.fragment_banner.*

@LogTag("ChapActivity")
@IsFullScreen(true)
@IsSwipeActivity(true)
class ChapActivity : BaseFontActivity() {

    companion object {
        const val KEY_STORY = "KEY_STORY"
    }

    private var story: Story? = null
    private var chapViewModel: ChapViewModel? = null
    private var pageIndex = 0
    private var totalPage = Int.MAX_VALUE

    override fun setLayoutResourceId(): Int {
        return R.layout.activity_chap
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupData()
        setupViews()
        setupViewModels()

        getListChap()
    }

    private fun getListChap() {
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

    @SuppressLint("SetTextI18n")
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

        LImageUtil.load(context = this, any = story?.getImgSource(), imageView = kbv)
        LImageUtil.load(context = this, any = story?.getImgSource(), imageView = ivCover)
        tvTitle.text = story?.title
        tvTotalChapter.text = "Số chương: ${story?.totalChapter}"
        tvViewCount.text = "Số lượt xem: ${story?.viewCount}"
        LUIUtil.setTextFromHTML(tvShortDescription, story?.description ?: "")

        ivBack.setSafeOnClickListener {
            onBackPressed()
        }
        LUIUtil.setSafeOnClickListenerElastic(
            view = tvShare,
            runnable = Runnable {
                LSocialUtil.shareApp(this)
            }
        )
    }

    private fun setupViewModels() {
        chapViewModel = getViewModel(ChapViewModel::class.java)
        chapViewModel?.let { mvm ->
            mvm.listStoryLiveData.observe(this, Observer { actionData ->
                //TODO iplm list chap
                logD("<<<loitpp listStoryLiveData " + BaseApplication.gson.toJson(actionData.data))
                val isDoing = actionData.isDoing
                val isSuccess = actionData.isSuccess
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

}