package com.example.refit.di

import com.example.refit.data.repository.signup.SignUpRepository
import com.example.refit.data.repository.signup.SignUpRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<SignUpRepository> { SignUpRepositoryImpl(get()) }
}