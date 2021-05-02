package com.loitp.model

import androidx.annotation.Keep
import java.io.Serializable

@Keep
data class Chap(
//    val chapterComicsDetails: Any,
//    val comicsId: String,
//    val createdBy: String,
//    val createdDate: String,
    val description: String,
    val id: String,
//    val isDelete: Boolean,
//    val modifiedBy: String,
//    val modifiedDate: String,
//    val nextChap: Any,
    val noChapter: Int,
//    val prevChap: Any,
//    val slug: String,
    val title: String
) : Serializable
