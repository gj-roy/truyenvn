package com.loitp.service

import com.loitp.model.Login
import com.loitp.model.RequestLogin
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface TCApiService {

    @POST("login/")
    fun loginAsync(
        @Body requestLogin: RequestLogin
    ): Deferred<Response<TCApiResponse<Login>>>
}
