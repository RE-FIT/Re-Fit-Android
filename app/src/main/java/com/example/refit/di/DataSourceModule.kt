package com.example.refit.di

import com.example.refit.data.repository.signup.datasource.SignUpDataSource
import com.example.refit.data.repository.signup.datasource.SignUpDataSourceImpl
import org.koin.dsl.module

val dataSourceModule = module {
    single<SignUpDataSource> { SignUpDataSourceImpl(get()) }
}