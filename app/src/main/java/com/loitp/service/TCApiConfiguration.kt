package com.loitp.service

/**
 * Created by Loitp on 24,December,2019
 * HMS Ltd
 * Ho Chi Minh City, VN
 * www.muathu@gmail.com
 */
class TCApiConfiguration {
    companion object {
        // authentication
        //TODO gradle
        const val BASE_URL = "https://api.docngontinh.com/"

        // API Param
        const val AUTHORIZATION_HEADER = "Authorization"
        const val TOKEN_TYPE = "bearer"
        const val TIME_OUT = 20L // In second
        const val LIMIT_PAGE = 20

        const val PAGE_SIZE = 50
    }
}
