package com.citycash.shop.ui.viewmodel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.citycash.shop.network.model.ApiResponse
import com.citycash.shop.network.model.Product
import com.citycash.shop.network.model.Wrapper
import com.citycash.shop.network.request.ProductRequest
import com.citycash.shop.network.requesthandler.Kobserver

class MainViewModel(application: Application) : AndroidViewModel(application) {
    // var products = MutableLiveData<List<Wish>>()
    private val _exception = MutableLiveData<java.lang.Exception>()
    val exception: LiveData<java.lang.Exception> get() = _exception



    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> get() = _products


    fun loadProducts() {
        productApi().observeForever(object : Kobserver<ApiResponse>(),
            Observer<Wrapper<ApiResponse>> {
            override fun onSuccess(data: ApiResponse) {
                _products.value = data.data!!
            }

            override fun onException(exception: Exception) {
                super.onException(exception)
                _exception.value = exception
            }
        })
    }

    fun filerDate(filterQuery: String) {

    }
    private fun productApi(): LiveData<Wrapper<ApiResponse>> {
        return ProductRequest().doRequest()
    }
}