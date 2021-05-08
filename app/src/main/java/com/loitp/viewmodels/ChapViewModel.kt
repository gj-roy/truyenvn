package com.loitp.viewmodels

import com.loitp.model.Chap
import com.loitp.service.StoryApiClient
import com.loitp.service.StoryRepository
import com.loitp.service.StoryViewModel
import com.service.livedata.ActionData
import com.service.livedata.ActionLiveData
import kotlinx.coroutines.launch

class ChapViewModel : StoryViewModel() {
    private val logTag = javaClass.simpleName
    private val repository = StoryRepository(StoryApiClient.apiService)

    val listStoryLiveData: ActionLiveData<ActionData<List<Chap>>> = ActionLiveData()

    fun getListChap(comicId: String, pageSize: Int, pageIndex: Int) {
        listStoryLiveData.set(ActionData(isDoing = true))
        ioScope.launch {
            val response = repository.getListChap(
                comicId = comicId,
                pageSize = pageSize,
                pageIndex = pageIndex
            )
            if (response.items == null || response.isSuccess == false) {
                listStoryLiveData.postAction(
                    getErrorRequestStory(response)
                )
            } else {
                val data = response.items.reversed()
                listStoryLiveData.post(
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
