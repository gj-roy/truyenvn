package com.loitp.activity

import android.os.Bundle
import android.view.View
import androidx.core.widget.NestedScrollView
import com.annotation.IsFullScreen
import com.annotation.IsShowAdWhenExit
import com.annotation.IsSwipeActivity
import com.annotation.LogTag
import com.core.base.BaseFontActivity
import com.core.utilities.LActivityUtil
import com.core.utilities.LSocialUtil
import com.core.utilities.LUIUtil
import com.loitp.R
import com.loitp.model.Chap
import com.views.layout.swipeback.SwipeBackLayout.OnSwipeBackListener
import com.views.setSafeOnClickListener
import kotlinx.android.synthetic.main.activity_read.*


@LogTag("ReadActivity")
@IsFullScreen(false)
@IsSwipeActivity(true)
@IsShowAdWhenExit(true)
class ReadActivity : BaseFontActivity() {
    //TODO animation
    companion object {
        const val KEY_CHAP = "KEY_CHAP"
    }

    private var chap: Chap? = null
    private var isScrollDown = false

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
        nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            if (scrollY > oldScrollY) {
                if (!isScrollDown) {
                    isScrollDown = true
                    logD("loitpp Scroll DOWN")
                    layoutControl.visibility = View.GONE
                }
            }
            if (scrollY < oldScrollY) {
                if (isScrollDown) {
                    isScrollDown = false
                    logD("loitpp Scroll UP")
                    layoutControl.visibility = View.VISIBLE
                }
            }
        })

        btShare.setSafeOnClickListener {
            LSocialUtil.shareApp(this)
        }
        btMinusSize.setSafeOnClickListener {
            //TODO
        }
        btAddSize.setSafeOnClickListener {
            //TODO
        }
    }

}