package com.loitp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.annotation.LogTag
import com.core.adapter.BaseAdapter
import com.core.utilities.LImageUtil
import com.loitp.R
import com.loitp.model.Story
import kotlinx.android.synthetic.main.view_row_item_banner.view.*

@LogTag("BannerAdapter")
class BannerAdapter(
    private val listBannerStory: ArrayList<Story>
) : BaseAdapter() {

    fun setData(listBannerStory: List<Story>) {
        this.listBannerStory.clear()
        this.listBannerStory.addAll(listBannerStory)
        notifyDataSetChanged()
    }

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(story: Story) {
            LImageUtil.load(
                context = itemView.imageViewBanner.context,
                any = story.imageSrc,
                imageView = itemView.imageViewBanner
            )
            itemView.tvTitle.text = story.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.view_row_item_banner,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = listBannerStory.size
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is DataViewHolder) {
            holder.bind(listBannerStory[position])
        }
    }

}
