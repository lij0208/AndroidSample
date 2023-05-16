package com.liz.data.di

import com.liz.data.BuildConfig
import com.liz.data.network.ApiType
import com.liz.data.network.api.Test1Api
import com.liz.data.network.api.Test2Api
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun okHttpCallFactory(): Call.Factory = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor()
                .apply {
                    if (BuildConfig.DEBUG) {
                        setLevel(HttpLoggingInterceptor.Level.BODY)
                    }
                },
        )
        .connectTimeout(1, TimeUnit.MINUTES)
        .readTimeout(1, TimeUnit.MINUTES)
        .writeTimeout(1, TimeUnit.MINUTES)
        .build()

    @Provides
    @Singleton
    fun provideTest1Api(factory: Call.Factory): Test1Api {
        return Retrofit.Builder()
            .baseUrl(ApiType.Test1.baseUrl)
            .callFactory(factory)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Test1Api::class.java)
    }

    @Provides
    @Singleton
    fun provideTest2Api(factory: Call.Factory): Test2Api {
        return Retrofit.Builder()
            .baseUrl(ApiType.Test2.baseUrl)
            .callFactory(factory)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Test2Api::class.java)
    }
}