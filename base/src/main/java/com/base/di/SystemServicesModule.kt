package com.base.di

import android.content.Context
import android.net.ConnectivityManager
import android.view.inputmethod.InputMethodManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SystemServicesModule {

    @JvmStatic
    @Provides
    @Singleton
    fun provideInputMedthodManager(@ApplicationContext context: Context) =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    @JvmStatic
    @Provides
    @Singleton
    fun provideConnectivityManager(@ApplicationContext context: Context) =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


}