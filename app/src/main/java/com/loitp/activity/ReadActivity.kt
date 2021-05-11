package com.loitp.activity

import android.os.Bundle
import android.view.View
import com.annotation.IsFullScreen
import com.annotation.IsShowAdWhenExit
import com.annotation.IsSwipeActivity
import com.annotation.LogTag
import com.core.base.BaseFontActivity
import com.core.utilities.LActivityUtil
import com.core.utilities.LUIUtil
import com.loitp.R
import com.loitp.model.Chap
import com.views.layout.swipeback.SwipeBackLayout.OnSwipeBackListener
import kotlinx.android.synthetic.main.activity_read.*

@LogTag("ReadActivity")
@IsFullScreen(false)
@IsSwipeActivity(true)
@IsShowAdWhenExit(true)
class ReadActivity : BaseFontActivity() {
    //TODO animation
    //TODO text size
    //TODO text share
    companion object {
        const val KEY_CHAP = "KEY_CHAP"
    }

    private var chap: Chap? = null

    override fun setLayoutResourceId(): Int {
        return R.layout.activity_read
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupData()
        setupViews()
    }

    private fun setupData() {
        intent?.getSerializableExtra(KEY_CHAP)?.let {
            if (it is Chap) {
                this.chap = it
            }
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
                    LActivityUtil.transActivityNoAnimation(this@ReadActivity)
                }
            }
        })
        tvTitle.text = chap?.title
        LUIUtil.setTextFromHTML(
            textView = tvDescription,
            bodyData = chap?.description ?: getString(R.string.no_data)
        )

    }

}