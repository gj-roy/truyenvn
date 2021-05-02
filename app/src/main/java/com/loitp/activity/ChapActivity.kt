package com.loitp.activity

import android.os.Bundle
import android.view.View
import com.annotation.IsFullScreen
import com.annotation.IsSwipeActivity
import com.annotation.LogTag
import com.core.base.BaseFontActivity
import com.core.utilities.LActivityUtil
import com.loitp.R
import com.loitp.model.Story
import com.views.layout.swipeback.SwipeBackLayout.OnSwipeBackListener
import kotlinx.android.synthetic.main.activity_chap.*

@LogTag("ChapActivity")
@IsFullScreen(false)
@IsSwipeActivity(true)
class ChapActivity : BaseFontActivity() {

    companion object {
        const val KEY_STORY = "KEY_STORY"
    }

    private var story: Story? = null

    override fun setLayoutResourceId(): Int {
        return R.layout.activity_chap
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupData()
        setupViews()
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
    }

}