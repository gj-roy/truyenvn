package com.loitp.activity

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.annotation.IsFullScreen
import com.annotation.LogTag
import com.core.base.BaseApplication
import com.core.base.BaseFontActivity
import com.core.utilities.LActivityUtil
import com.core.utilities.LValidateUtil
import com.loitp.BuildConfig
import com.loitp.R
import com.loitp.service.StoryApiClient
import com.loitp.viewmodels.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*

@LogTag("LoginActivity")
@IsFullScreen(false)
class LoginActivity : BaseFontActivity() {

    private var loginViewModel: LoginViewModel? = null

    override fun setLayoutResourceId(): Int {
        return R.layout.activity_login
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupViews()
        setupViewModels()

        loginViewModel?.login(
            email = BuildConfig.EMAIL,
            password = BuildConfig.PW
        )

        LValidateUtil.isValidPackageName()
    }

    private fun setupViews() {
        //do nothing
    }

    private fun setupViewModels() {
        loginViewModel = getViewModel(LoginViewModel::class.java)
        loginViewModel?.let { vm ->
            vm.loginActionLiveData.observe(this, Observer { actionData ->
                logD("<<<loginActionLiveData observe " + BaseApplication.gson.toJson(actionData))
                val isDoing = actionData.isDoing
                val isSuccess = actionData.isSuccess
                if (isDoing == true) {
                    indicatorView.smoothToShow()
                } else {
                    indicatorView.smoothToHide()

                    if (isSuccess == true) {
                        val data = actionData.data
//                    logD("<<<loginActionLiveData observe " + BaseApplication.gson.toJson(data))
                        val token = data?.jwtToken
//                    logD("token $token")
                        if (token.isNullOrEmpty()) {
                            showDialogError(
                                getString(R.string.err_unknow), Runnable {
                                onBackPressed()
                            });
                        } else {

                            //add token
                            StoryApiClient.addAuthorization(token)

                            val intent = Intent(this, HomeActivity::class.java)
                            startActivity(intent)
                            LActivityUtil.tranIn(context = this)
                            finish()
                        }
                    } else {
                        val error = actionData.errorResponse
                        showDialogError(error?.message, Runnable {
                            onBackPressed()
                        })
                    }
                }
            })
        }
    }
}
