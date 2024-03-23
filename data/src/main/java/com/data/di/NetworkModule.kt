package com.data.di


import android.content.Context
import com.data.network.ApiInterface
import com.data.network.RequestInterceptor
import com.data.network.TokenAuthenticator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module()
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttp(
        tokenAuthenticator: TokenAuthenticator,
        requestInterceptor: RequestInterceptor,
        @ApplicationContext context: Context
    ) = OkHttpClient.Builder()
        .readTimeout(30, TimeUnit.SECONDS)
        .addInterceptor { chain ->
            val res = chain.proceed(chain.request())
            if (res.code() == 429){
                Thread.sleep(1000)
                chain.proceed(chain.request())
            }else{
                res
            }

        }
        .retryOnConnectionFailure(true)
        .addInterceptor(requestInterceptor)
         /* .addNetworkInterceptor(
              ChuckerInterceptor.Builder(context)
                  .collector(ChuckerCollector(context,showNotification = true,RetentionManager.Period.ONE_HOUR))
                  .maxContentLength(250000L)
                  .alwaysReadResponseBody(true)
                  .redactHeaders(emptySet())
                  .alwaysReadResponseBody(true)
                  .build()
          )*/


//        .authenticator(tokenAuthenticator)
        .writeTimeout(30, TimeUnit.SECONDS)
        .connectTimeout(30, TimeUnit.SECONDS)

        .build()

    @Provides
    @Singleton
    fun provideApiInterface(retrofit: Retrofit) =
        retrofit.create<ApiInterface>(ApiInterface::class.java)

}
