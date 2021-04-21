package com.loitp.fragment

import android.os.Bundle
import android.view.View
import com.annotation.LogTag
import com.core.base.BaseFragment
import com.loitp.R

@LogTag("SettingFragment")
class SettingFragment : BaseFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    override fun setLayoutResourceId(): Int {
        return R.layout.frm_setting
    }

    private fun setupViews() {

    }
}
