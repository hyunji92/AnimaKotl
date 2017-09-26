package com.hyundeee.app.animakotl.di.module

import com.hyundeee.app.animakotl.api.ImageClient
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

/**
 * Created by ${hyundee} on 2017. 9. 26..
 */

@Module
class ClientModule{
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor())
                .addInterceptor(logging)
                .build()
    }

    @Provides
    fun provideImageClient(): ImageClient {
        return ImageClient(provideOkHttpClient())
    }
}