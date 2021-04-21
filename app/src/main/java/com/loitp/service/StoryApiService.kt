package com.loitp.service

import com.loitp.model.Login
import com.loitp.model.RequestLogin
import com.loitp.model.Story
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface StoryApiService {

    @POST("login/")
    fun loginAsync(
        @Body requestLogin: RequestLogin
    ): Deferred<Response<StoryApiResponse<Login>>>

    @GET("stories/")
    fun getListStoryAsync(
        @Query("PageSize") pageSize: Int,
        @Query("PageIndex") pageIndex: Int
    ): Deferred<Response<StoryApiResponse<List<Story>>>>
}
