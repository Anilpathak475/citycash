package com.citycash.shop.network

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit


object NetworkModule {
    private const val BASE_URL = "http://52.25.13.115:7000"
    private var gson: Gson = GsonBuilder()
        .setLenient()
        .create()

    fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(NullOnEmptyConverterFactory())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(BASE_URL)
            .client(provideOkHttpClient())
            .build()
    }

    private fun provideOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
        client.addInterceptor(Interceptor { chain ->
            val request = chain.request()
            val requestBuilder = request.newBuilder().headers(makeHttpHeaders().build())
            val response = chain.proceed(requestBuilder.build())
            try {

                assert(response.body() != null)
                val responseString = String(response.body()!!.bytes())
                Log.d(
                    "-OkHttp-NET-Interceptor",
                    "Api:-" + request.url() + " :- Response: " + responseString
                )


                return@Interceptor response.newBuilder()
                    .body(
                        ResponseBody.create(
                            response.body()!!.contentType(),
                            responseString
                        )
                    )
                    .build()

            } catch (ex: KotlinNullPointerException) {
                throw ex
            }
        })
        client.connectTimeout(20, TimeUnit.SECONDS)
        client.readTimeout(30, TimeUnit.SECONDS)
        client.writeTimeout(30, TimeUnit.SECONDS)
        return client.build()
    }

    private class NullOnEmptyConverterFactory : Converter.Factory() {
        override fun responseBodyConverter(
            type: Type?,
            annotations: Array<Annotation>?,
            retrofit: Retrofit?
        ): Converter<ResponseBody, Any>? {
            val delegate = retrofit!!.nextResponseBodyConverter<Any>(this, type!!, annotations!!)
            return Converter { body -> if (body.contentLength() == 0L) null else delegate.convert(body) }
        }
    }

    private fun makeHttpHeaders(): Headers.Builder {
        return Headers.Builder()

    }
}