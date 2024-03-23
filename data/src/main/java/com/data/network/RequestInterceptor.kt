package com.data.network

import com.data.util.Applog
import com.data.util.InternetConnection
import com.domain.exceptions.ConnectivityException
import com.squareup.moshi.Moshi
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RequestInterceptor @Inject constructor(
    private var internetConnection: InternetConnection,
    private var moshi: Moshi
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        if (!internetConnection.isNetworkConnected()) {
            throw ConnectivityException()
        }

        val requestBuilder = chain.request().newBuilder()
        requestBuilder.addHeader("Accept", "application/json")
        /*runBlocking {
            if (userSession.isUserLoggedIn())
                requestBuilder.addHeader("Authorization", userSession.getToken())
        }*/



        val request = requestBuilder.build()


        Applog.d("endpoint: ${request.url().url()}")
        Applog.d("headerMap: ${request.headers()}")
        Applog.d("queryMap: ${request.url().query()}")
        Applog.d("bodyMap: ${chain.request().body()}")






        return chain.proceed(request)
    }
}