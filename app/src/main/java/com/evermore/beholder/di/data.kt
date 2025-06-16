package com.evermore.beholder.di

import com.evermore.beholder.data.repositories.ClassRepository
import com.evermore.beholder.data.repositories.MonsterRepository
import com.evermore.beholder.data.repositories.RaceRepository
import org.koin.dsl.module

val dataModule = module {
    single { ClassRepository(get()) }
    single { RaceRepository(get()) }
    single { MonsterRepository(get()) }
}
