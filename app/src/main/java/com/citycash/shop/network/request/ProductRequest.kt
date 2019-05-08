package com.citycash.shop.network.request

import com.citycash.shop.network.errorhandler.ApiErrorHandler
import com.citycash.shop.network.requesthandler.GenericRequestHandler
import com.citycash.shop.network.client.ProductApi
import com.citycash.shop.network.errorhandler.WishErrorHandler
import com.citycash.shop.network.NetworkModule
import com.citycash.shop.network.model.ApiResponse
import retrofit2.Call

class ProductRequest : GenericRequestHandler<ApiResponse>() {
    private val productApi: ProductApi by lazy {
        NetworkModule.createRetrofit().create(ProductApi::class.java)
    }

    override val errorHandler: ApiErrorHandler = WishErrorHandler()

    override fun makeRequest(): Call<ApiResponse> {
        return productApi.wish()
    }
}