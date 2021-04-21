package com.loitp.viewmodels

import com.annotation.LogTag
import com.loitp.service.BaseComicViewModel
import com.loitp.service.ComicApiClient
import com.loitp.service.ComicRepository
import com.model.Login
import com.service.livedata.ActionData
import com.service.livedata.ActionLiveData
import kotlinx.coroutines.launch

/**
 * Created by Loitp on 24,December,2019
 * HMS Ltd
 * Ho Chi Minh City, VN
 * www.muathu@gmail.com
 */

@LogTag("ComicLoginViewModel")
class ComicLoginViewModel : BaseComicViewModel() {
    private val repository = ComicRepository(ComicApiClient.apiService)

    val loginActionLiveData: ActionLiveData<ActionData<Login>> = ActionLiveData()

    fun login(email: String, password: String) {
        loginActionLiveData.set(ActionData(isDoing = true))

        ioScope.launch {
            val response = repository.login(
                email = email,
                password = password
            )
//            logD("<<<login " + BaseApplication.gson.toJson(response))
            if (response.items == null || response.isSuccess == false) {
                loginActionLiveData.postAction(
                    getErrorRequestComic(response)
                )
            } else {
                val data = response.items
                loginActionLiveData.post(
                    ActionData(
                        isDoing = false,
                        isSuccess = true,
                        data = data,
                        total = response.total,
                        totalPages = response.totalPages,
                        page = response.page
                    )
                )
            }
        }

    }

}
