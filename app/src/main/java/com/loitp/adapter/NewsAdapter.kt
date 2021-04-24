package com.loitp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.annotation.LogTag
import com.core.adapter.BaseAdapter
import com.core.base.BaseApplication
import com.core.utilities.LImageUtil
import com.loitp.R
import com.loitp.model.News
import com.views.setSafeOnClickListener
import kotlinx.android.synthetic.main.view_row_item_news.view.*

@LogTag("NewsAdapter")
class NewsAdapter(
    private val listNews: ArrayList<News>
) : BaseAdapter() {

    var onClickRootListener: ((News, Int) -> Unit)? = null

    fun addData(listNews: ArrayList<News>) {
        this.listNews.addAll(listNews)
        notifyDataSetChanged()
    }

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(news: News) {
            itemView.textViewNews.text = news.title
            LImageUtil.load(
                context = itemView.imageView.context,
                any = news.image,
                imageView = itemView.imageView
            )
            itemView.layoutRoot.setSafeOnClickListener {
                onClickRootListener?.invoke(news, bindingAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.view_row_item_news, parent,
                false
            )
        )

    override fun getItemCount(): Int = listNews.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is DataViewHolder) {
            holder.bind(listNews[position])
        }
    }

}
