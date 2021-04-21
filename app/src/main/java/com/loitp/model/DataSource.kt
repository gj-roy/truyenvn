package com.loitp.model

import com.core.common.Constants
import com.loitp.R

object DataSource {

    fun getListUser() = ArrayList<User>().apply {
        for (i in 0..5) {
            add(User(i, "Loitp$i", Constants.URL_IMG_ANDROID))
        }
    }

    fun getBanner() = ArrayList<Banner>().apply {
        add(Banner(bannerImage = R.mipmap.ic_launcher))
    }
}
