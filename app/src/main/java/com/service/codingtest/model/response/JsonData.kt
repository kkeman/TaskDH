package com.service.codingtest.model.response

import com.google.gson.annotations.SerializedName

data class JsonData(
        @SerializedName("items")
        val items: List<ItemsEntity>
)