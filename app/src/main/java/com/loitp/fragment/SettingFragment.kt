package com.loitp.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.annotation.LogTag
import com.core.base.BaseFragment
import com.core.utilities.LActivityUtil
import com.core.utilities.LDialogUtil
import com.core.utilities.LImageUtil
import com.core.utilities.LUIUtil
import com.loitp.R
import com.loitp.activity.SplashActivity
import com.loitp.db.Db
import kotlinx.android.synthetic.main.frm_home.*
import kotlinx.android.synthetic.main.frm_setting.*
import kotlinx.android.synthetic.main.frm_setting.ivBackground

@LogTag("SettingFragment")
class SettingFragment : BaseFragment() {
    private var dialog: AlertDialog? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    override fun setLayoutResourceId(): Int {
        return R.layout.frm_setting
    }

    override fun onResume() {
        super.onResume()

        LImageUtil.load(
            context = context,
            any = Db.getLastBackground(),
            imageView = ivBackground
        )
    }

    override fun onDestroy() {
        dialog?.dismiss()
        super.onDestroy()
    }

    private fun setupViews() {
        val isDarkTheme = LUIUtil.isDarkTheme()
        sw.isChecked = isDarkTheme

        sw.setOnCheckedChangeListener { _, isChecked ->
            handleSwitchDarkTheme(isChecked)
        }
    }

    private fun handleSwitchDarkTheme(isChecked: Boolean) {
        context?.let { c ->
            val isDarkTheme = LUIUtil.isDarkTheme()
            if (isDarkTheme == isChecked) {
                return@let
            }

            dialog = LDialogUtil.showDialog2(
                context = c,
                title = getString(com.R.string.warning_vn),
                msg = getString(com.R.string.app_will_be_restarted_vn),
                button1 = getString(com.R.string.cancel),
                button2 = getString(com.R.string.ok),
                onClickButton1 = {
                    sw?.isChecked = LUIUtil.isDarkTheme()
                },
                onClickButton2 = {
                    if (isChecked) {
                        LUIUtil.setDarkTheme(isDarkTheme = true)
                    } else {
                        LUIUtil.setDarkTheme(isDarkTheme = false)
                    }
                    dialog?.dismiss()

                    activity?.let {
                        val intent = Intent(it, SplashActivity::class.java)
                        startActivity(intent)
                        LActivityUtil.tranIn(context = it)
                        it.finish()
                    }
                }
            )
            dialog?.setOnCancelListener {
                sw?.isChecked = LUIUtil.isDarkTheme()
            }
        }
    }
}
