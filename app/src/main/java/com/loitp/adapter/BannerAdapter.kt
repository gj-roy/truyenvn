package com.loitp.adapter

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
import kotlinx.android.synthetic.main.view_row_item_banner.view.*

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
            itemView.viewPager.adapter = SamplePagerAdapter(fragmentManager)
            itemView.viewPager.setAutoScrollEnabled(true)
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

    private inner class SamplePagerAdapter(fm: FragmentManager) :
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
