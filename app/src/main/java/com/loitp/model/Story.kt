package com.loitp.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Keep
data class Story(
    @SerializedName("title")
    var title: String = "",

    @SerializedName("description")
    var description: String = "",

    @SerializedName("totalChapter")
    val totalChapter: Int = 0,

    @SerializedName("imageSrc")
    var imageSrc: String = "",

    @SerializedName("viewCount")
    var viewCount: Int = 0,

    @SerializedName("id")
    var id: String = "",

    @SerializedName("createdDate")
    var createdDate: String = "",

    @SerializedName("modifiedDate")
    var modifiedDate: String = ""
) : Serializable