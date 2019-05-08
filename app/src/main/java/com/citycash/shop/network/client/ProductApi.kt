package com.citycash.shop.network.client

import com.citycash.shop.network.model.ApiResponse
import retrofit2.Call
import retrofit2.http.GET

interface ProductApi {
    @GET("/interview/productList")
    fun wish(): Call<ApiResponse>
}