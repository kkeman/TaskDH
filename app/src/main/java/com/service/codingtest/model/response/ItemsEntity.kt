package com.service.codingtest.model.response

import com.google.gson.annotations.SerializedName
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "Items")
data class ItemsEntity(

        @PrimaryKey
        @SerializedName("login")
        val login: String,

        @SerializedName("id")
        val id: Int,

        @SerializedName("avatar_url")
        val avatar_url: String,

        var searchWord: String,

        var isFavorite: Boolean
)