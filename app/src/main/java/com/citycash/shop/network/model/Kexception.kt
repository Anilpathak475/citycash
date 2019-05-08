package com.citycash.shop.network.model

import com.citycash.shop.network.model.StatusCode
import okhttp3.ResponseBody

open class Kexception(val errorBody: ResponseBody?,
                      message: String?,
                      cause: Throwable?,
                      val statusCode: StatusCode
) : Exception(message, cause)