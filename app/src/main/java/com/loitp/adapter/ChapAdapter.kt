package com.loitp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.annotation.LogTag
import com.core.adapter.BaseAdapter
import com.core.utilities.LUIUtil
import com.loitp.R
import com.loitp.model.Chap
import kotlinx.android.synthetic.main.view_row_item_story_chap.view.*

@LogTag("ChapAdapter")
class ChapAdapter(
    private val listChap: ArrayList<Chap>
) : BaseAdapter() {

    var onClickRootListener: ((Chap, Int) -> Unit)? = null

    fun addData(listChap: List<Chap>) {
        this.listChap.addAll(listChap)
        notifyDataSetChanged()
    }

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(chap: Chap) {
            itemView.tvNoChapter.text = "${chap.noChapter}"
            itemView.tvTitle.text = chap.title
            LUIUtil.setTextFromHTML(textView = itemView.tvDescription, bodyData = chap.description)

            LUIUtil.setSafeOnClickListenerElastic(
                view = itemView.btReadFull,
                runnable = Runnable {
                    onClickRootListener?.invoke(chap, bindingAdapterPosition)
                }
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.view_row_item_story_chap,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = listChap.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is DataViewHolder) {
//            logD("onBindViewHolder position $position")
            holder.bind(listChap[position])
        }
    }

}
