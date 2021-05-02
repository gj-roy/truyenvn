package com.loitp.service

import com.loitp.model.Chap
import com.loitp.model.Login
import com.loitp.model.RequestLogin
import com.loitp.model.Story

/**
 * Created by Loitp on 24,December,2019
 * HMS Ltd
 * Ho Chi Minh City, VN
 * www.muathu@gmail.com
 */
class StoryRepository(private val storyApiService: StoryApiService) : StoryBaseRepository() {

    suspend fun login(email: String, password: String): StoryApiResponse<Login> = makeApiCall {
        val requestLogin = RequestLogin(
            email = email,
            password = password
        )
        storyApiService.loginAsync(
            requestLogin = requestLogin
        ).await()
    }

    suspend fun getListStory(pageSize: Int, pageIndex: Int): StoryApiResponse<List<Story>> =
        makeApiCall {
            storyApiService.getListStoryAsync(
                pageSize = pageSize,
                pageIndex = pageIndex
            ).await()
        }

    suspend fun getListChap(
        comicId: String,
        pageSize: Int,
        pageIndex: Int
    ): StoryApiResponse<List<Chap>> =
        makeApiCall {
            storyApiService.getListChapAsync(
                comicId = comicId,
                pageSize = pageSize,
                pageIndex = pageIndex
            ).await()
        }
}
