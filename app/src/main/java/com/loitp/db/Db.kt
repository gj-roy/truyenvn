package com.loitp.db

import com.core.utilities.LAppResource
import com.core.utilities.LSharedPrefsUtil
import com.loitp.R

class Db {
    companion object {

        private const val KEY_LAST_BACKGROUND_IMG = "KEY_LAST_BACKGROUND_IMG"
        private const val KEY_TEXT_SIZE = "KEY_TEXT_SIZE"
        const val DEFAULT_TEXT_SIZE = 12f

        fun setLastBackground(linkImg: String) {
            LSharedPrefsUtil.instance.putString(
                KEY_LAST_BACKGROUND_IMG,
                linkImg
            )

        }

        fun getLastBackground(): String {
            return LSharedPrefsUtil.instance.getString(
                KEY_LAST_BACKGROUND_IMG,
                LAppResource.getString(R.string.link_cover)
            )
        }

        fun setTextSize(textSize: Float) {
            LSharedPrefsUtil.instance.putFloat(
                KEY_TEXT_SIZE,
                textSize
            )

        }

        fun getTextSize(): Float {
            return LSharedPrefsUtil.instance.getFloat(
                KEY_TEXT_SIZE,
                DEFAULT_TEXT_SIZE
            )
        }
    }
}
