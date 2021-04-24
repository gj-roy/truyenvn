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
import com.views.setSafeOnClickListener
import kotlinx.android.synthetic.main.view_row_item_story.view.*

@LogTag("StoryAdapter")
class StoryAdapter(
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
                any = story.getImgSource(),
                imageView = itemView.imageViewBanner,
                resError = R.drawable.place_holder_error404
            )
            itemView.tvTitle.text = story.title

            itemView.layoutRoot.setSafeOnClickListener {
                //TODO
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.view_row_item_story,
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
