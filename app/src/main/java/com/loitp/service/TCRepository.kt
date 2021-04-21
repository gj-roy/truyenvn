package com.loitp.service

import com.model.Login
import com.model.RequestLogin

/**
 * Created by Loitp on 24,December,2019
 * HMS Ltd
 * Ho Chi Minh City, VN
 * www.muathu@gmail.com
 */
class TCRepository(private val TCApiService: TCApiService) : TCBaseRepository() {

    suspend fun login(email: String, password: String): TCApiResponse<Login> = makeApiCall {
        val requestLogin = RequestLogin(
            email = email,
            password = password
        )
        TCApiService.loginAsync(
            requestLogin = requestLogin
        ).await()
    }
}
