package com.evermore.beholder.di

import com.evermore.beholder.data.repositories.ClassRepository
import com.evermore.beholder.data.repositories.MonsterRepository
import com.evermore.beholder.domain.interfaces.Dnd5eApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

val domainModule = module {
    single {
        Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
    }
    single {
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }
    single {
        Retrofit.Builder()
            .baseUrl("https://www.dnd5eapi.co/api/2014/")
            .client(get())
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .build()
    }
    single { get<Retrofit>().create(Dnd5eApi::class.java) }

    single { ClassRepository(get()) }
    single { MonsterRepository(get()) }
}
