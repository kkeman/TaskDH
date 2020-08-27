package com.service.codingtest.model.response

import com.google.gson.annotations.SerializedName
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "Items")
data class ItemsEntity(

        var searchWord: String,

        @SerializedName("avatar_url")
        val avatar_url: String,

        @SerializedName("login")
        val login: String,

        @PrimaryKey
        @SerializedName("id")
        val id: Int,

        var isFavorite: Boolean
)