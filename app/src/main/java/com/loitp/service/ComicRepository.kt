package com.loitp.service

import com.model.Login
import com.model.RequestLogin

/**
 * Created by Loitp on 24,December,2019
 * HMS Ltd
 * Ho Chi Minh City, VN
 * www.muathu@gmail.com
 */
class ComicRepository(private val comicApiService: ComicApiService) : ComicBaseRepository() {

    suspend fun login(email: String, password: String): ComicApiResponse<Login> = makeApiCall {
        val requestLogin = RequestLogin(
            email = email,
            password = password
        )
        comicApiService.loginAsync(
            requestLogin = requestLogin
        ).await()
    }
}
