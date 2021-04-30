package com.loitp.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.core.utilities.LImageUtil
import com.loitp.R
import com.loitp.model.Story
import kotlinx.android.synthetic.main.fragment_banner.*
import kotlinx.android.synthetic.main.view_row_item_story.view.*

class FrmBanner : Fragment() {

    companion object {
        const val KEY_STORY = "KEY_STORY"
    }

    private var story: Story? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_banner, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tmpStory = arguments?.getSerializable(KEY_STORY)
        if (tmpStory != null && tmpStory is Story) {
            story = tmpStory
        }
        setupViews()
    }

    private fun setupViews() {
        LImageUtil.load(
            context = context,
            any = story?.getImgSource(),
            imageView = ivBackground,
            resError = R.drawable.place_holder_error404
        )
    }

}
