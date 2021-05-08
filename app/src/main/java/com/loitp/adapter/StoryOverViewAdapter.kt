package com.loitp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.annotation.LogTag
import com.core.adapter.BaseAdapter
import com.core.utilities.LImageUtil
import com.core.utilities.LUIUtil
import com.loitp.R
import com.loitp.model.Story
import com.views.setSafeOnClickListener
import kotlinx.android.synthetic.main.view_row_item_story_overview.view.*

@LogTag("StoryOverViewAdapter")
class StoryOverViewAdapter : BaseAdapter() {
    var story: Story? = null

    var onClickBackListener: ((Unit) -> Unit)? = null
    var onClickShareListener: ((Unit) -> Unit)? = null

    fun setData(story: Story?) {
        this.story = story
        notifyDataSetChanged()
    }

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(story: Story) {
            LImageUtil.load(
                context = itemView.context,
                any = story.getImgSource(),
                imageView = itemView.kbv
            )
            LImageUtil.load(
                context = itemView.context,
                any = story.getImgSource(),
                imageView = itemView.ivCover
            )
            itemView.tvTitle.text = story.title
            itemView.tvTotalChapter.text = "Số chương: ${story.totalChapter}"
            itemView.tvViewCount.text = "Số lượt xem: ${story.viewCount}"
            LUIUtil.setTextFromHTML(
                textView = itemView.tvShortDescription,
                bodyData = story.description ?: ""
            )

            itemView.ivBack.setSafeOnClickListener {
                onClickBackListener?.invoke(Unit)
            }
            LUIUtil.setSafeOnClickListenerElastic(
                view = itemView.tvShare,
                runnable = Runnable {
                    onClickShareListener?.invoke(Unit)
                }
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.view_row_item_story_overview, parent, false)
        )

    override fun getItemCount(): Int = 1
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is DataViewHolder) {
            story?.let {
                holder.bind(it)
            }
        }
    }

}
