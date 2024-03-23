package com.data.network

import com.data.util.Applog
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenAuthenticator @Inject constructor() : Authenticator{
    override fun authenticate(route: Route?, response: Response): Request? {
        Applog.d("response: ${response.toString()}")
       return response.request().newBuilder().build()
    }

}