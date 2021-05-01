package com.loitp.viewmodels

import com.loitp.model.Story
import com.loitp.service.StoryApiClient
import com.loitp.service.StoryRepository
import com.loitp.service.StoryViewModel
import com.service.livedata.ActionData
import com.service.livedata.ActionLiveData
import kotlinx.coroutines.launch

class MainViewModel : StoryViewModel() {
    private val logTag = javaClass.simpleName
    private val repository = StoryRepository(StoryApiClient.apiService)

    val listStoryLiveData: ActionLiveData<ActionData<List<Story>>> = ActionLiveData()

    fun getListStory(pageSize: Int, pageIndex: Int, isRefresh: Boolean) {
        listStoryLiveData.set(ActionData(isDoing = true))
        ioScope.launch {
            val response = repository.getListStory(
                pageSize = pageSize,
                pageIndex = pageIndex
            )
            if (response.items == null || response.isSuccess == false) {
                listStoryLiveData.postAction(
                    getErrorRequestStory(response)
                )
            } else {
                val data = response.items
                listStoryLiveData.post(
                    ActionData(
                        isDoing = false,
                        isSuccess = true,
                        data = data,
                        total = response.total,
                        totalPages = response.totalPages,
                        page = response.page,
                        isSwipeToRefresh = isRefresh
                    )
                )
            }

        }
    }
}
