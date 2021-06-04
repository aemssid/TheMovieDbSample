package com.example.savvologytask.di

import com.example.savvologytask.data.remote.service.MovieService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    single { provideHttpLogginInterpretor() }
    single { provideOkHttpClient(get()) }
    single { provideRetrofit(get()) }
    single { providerMovieServices(get()) }
}


fun provideHttpLogginInterpretor(): HttpLoggingInterceptor {
    val loggingInterceptor = HttpLoggingInterceptor()
    loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    return loggingInterceptor
}


fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
    return OkHttpClient().newBuilder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(httpLoggingInterceptor)
        .build()
}

fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder().baseUrl("https://api.themoviedb.org/3/").client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create()).build()
}

fun providerMovieServices(retrofit: Retrofit) = retrofit.create(MovieService::class.java)