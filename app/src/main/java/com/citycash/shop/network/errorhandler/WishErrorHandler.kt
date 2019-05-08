package com.citycash.shop.network.errorhandler

import retrofit2.Response

class WishErrorHandler : ApiErrorHandler() {

    override fun getExceptionType(response: Response<*>): Exception {
        return when (response.code()) {
            NETWORK_ERROR -> NetworkException()
            NOT_FOUND -> AlbumException()
            else -> Exception()
        }
    }

    companion object ErrorConfig {
        const val NETWORK_ERROR = 401
        const val NOT_FOUND = 404

        class NetworkException : Exception() {
            override val message = "Error due to network"
        }

        class AlbumException : Exception() {
            override val message = "Error from Wish Api"
        }
    }
}