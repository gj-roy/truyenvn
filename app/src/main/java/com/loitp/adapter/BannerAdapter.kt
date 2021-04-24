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
import kotlinx.android.synthetic.main.view_row_item_banner.view.*

@LogTag("BannerAdapter")
class BannerAdapter(
    private val listStoryBanner: ArrayList<Story>
) : BaseAdapter() {

    var onClickRootListener: ((Story, Int) -> Unit)? = null

    fun setData(listStoryBanner: List<Story>) {
        this.listStoryBanner.clear()
        this.listStoryBanner.addAll(listStoryBanner)
        notifyDataSetChanged()
    }

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(story: Story) {
            itemView.textViewNews.text = story.title
            LImageUtil.load(
                context = itemView.imageView.context,
                any = story.getImgSource(),
                imageView = itemView.imageView
            )
            itemView.layoutRoot.setSafeOnClickListener {
                onClickRootListener?.invoke(story, bindingAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.view_row_item_banner, parent,
                false
            )
        )

    override fun getItemCount(): Int = listStoryBanner.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is DataViewHolder) {
            holder.bind(listStoryBanner[position])
        }
    }

}
