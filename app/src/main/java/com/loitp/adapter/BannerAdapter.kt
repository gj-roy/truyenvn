package com.loitp.adapter

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.recyclerview.widget.RecyclerView
import com.annotation.LogTag
import com.core.adapter.BaseAdapter
import com.loitp.R
import com.loitp.model.Story
import com.loitp.view.ScaleCircleNavigator
import kotlinx.android.synthetic.main.view_row_item_banner.view.*
import net.lucode.hackware.magicindicator.ViewPagerHelper

@LogTag("BannerAdapter")
class BannerAdapter(
    private val fragmentManager: FragmentManager,
    private val listStoryBanner: ArrayList<Story>
) : BaseAdapter() {

    var onClickRootListener: ((Story, Int) -> Unit)? = null

    fun setData(listStoryBanner: List<Story>) {
        this.listStoryBanner.clear()
        this.listStoryBanner.addAll(listStoryBanner)
        notifyDataSetChanged()
    }

    inner class BannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
            itemView.viewPager.adapter = PagerAdapter(fragmentManager)
            itemView.viewPager.setAutoScrollEnabled(true)

            val scaleCircleNavigator = ScaleCircleNavigator(itemView.context)
            scaleCircleNavigator.setCircleCount(listStoryBanner.size)
            scaleCircleNavigator.setNormalCircleColor(Color.LTGRAY)
            scaleCircleNavigator.setSelectedCircleColor(Color.WHITE)
            scaleCircleNavigator.setCircleClickListener(object :
                ScaleCircleNavigator.OnCircleClickListener {
                override fun onClick(index: Int) {
                    itemView.viewPager.currentItem = index
                }
            })
            itemView.magicIndicator3.navigator = scaleCircleNavigator
            ViewPagerHelper.bind(itemView.magicIndicator3, itemView.viewPager)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        BannerViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.view_row_item_banner, parent,
                false
            )
        )

    override fun getItemCount(): Int = 1

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is BannerViewHolder) {
            holder.bind()
        }
    }

    private inner class PagerAdapter(fm: FragmentManager) :
        FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int): Fragment {
            val story = listStoryBanner[position]
            val frmBanner = FrmBanner()
            val bundle = Bundle()
            bundle.putSerializable(FrmBanner.KEY_STORY, story)
            frmBanner.arguments = bundle
            return frmBanner
        }

        override fun getCount(): Int {
            return listStoryBanner.size
        }
    }
}
