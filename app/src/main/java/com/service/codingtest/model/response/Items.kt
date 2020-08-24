package com.service.codingtest.model.response

import com.google.gson.annotations.SerializedName

data class Items(
        @SerializedName("avatar_url")
        val avatar_url: String,

        @SerializedName("login")
        val login: String,

        @SerializedName("id")
        val id: Int
)