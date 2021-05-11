package com.loitp.activity

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.annotation.IsFullScreen
import com.annotation.IsSwipeActivity
import com.annotation.LogTag
import com.core.base.BaseFontActivity
import com.core.utilities.LActivityUtil
import com.core.utilities.LUIUtil
import com.loitp.R
import com.loitp.fragment.ChapFragment
import com.views.layout.swipeback.SwipeBackLayout.OnSwipeBackListener
import kotlinx.android.synthetic.main.activity_read.*

@LogTag("ReadActivity")
@IsFullScreen(false)
@IsSwipeActivity(true)
class ReadActivity : BaseFontActivity() {

    companion object {
        const val KEY_TOTAL = "KEY_TOTAL"
        const val KEY_TOTAL_PAGE = "KEY_TOTAL_PAGE"
    }

    private var totalPage = 0
    private var total = 0

    override fun setLayoutResourceId(): Int {
        return R.layout.activity_read
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupData()
        setupViews()

        logD("loitpp totalPage $totalPage")
        logD("loitpp total $total")
    }

    private fun setupData() {
        intent?.getIntExtra(KEY_TOTAL_PAGE, 0)?.let {
            totalPage = it
        }
        intent?.getIntExtra(KEY_TOTAL, 0)?.let {
            total = it
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

        vp.adapter = SamplePagerAdapter(supportFragmentManager)
        tabLayout.setupWithViewPager(vp)
        LUIUtil.changeTabsFont(tabLayout, com.core.common.Constants.FONT_PATH)
    }

    private inner class SamplePagerAdapter(fm: FragmentManager) :
        FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int): Fragment {
            return ChapFragment()
        }

        override fun getCount(): Int {
            return total
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return "Page Title $position"
        }
    }
}