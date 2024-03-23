package com.data.di

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    @Singleton
    fun providesRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ) = Retrofit.Builder()
        .baseUrl("https://api.mockfly.dev/mocks/")
        .addConverterFactory(gsonConverterFactory)
        .client(okHttpClient)
            
        .build()


    @JvmStatic
    @Provides
    @Singleton
    fun provideGsonConverter(gson: Gson) = GsonConverterFactory.create(gson)


}