package com.citycash.shop.network.errorhandler

import com.citycash.shop.network.model.Kexception
import com.citycash.shop.network.model.StatusCode

open class ApiErrorHandler {

    open fun getExceptionType(response: retrofit2.Response<*>): Exception {
        return Kexception(response.errorBody(), response.message(), null, StatusCode(response.code()))
    }


}