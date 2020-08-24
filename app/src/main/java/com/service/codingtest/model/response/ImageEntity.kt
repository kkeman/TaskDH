package com.service.codingtest.model.response

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Image")
data class ImageEntity(

        val searchWord: String,

        val login: Int,

        val id: String,

        val avatar_url: String
)