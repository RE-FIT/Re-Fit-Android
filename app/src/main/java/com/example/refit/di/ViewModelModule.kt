package com.example.refit.di

import com.example.refit.presentation.AccessTokenViewModel
import com.example.refit.presentation.closet.viewmodel.ClosetViewModel
import com.example.refit.presentation.closet.viewmodel.ClothAddViewModel
import com.example.refit.presentation.signin.viewmodel.SignInViewModel
import com.example.refit.presentation.signup.viewmodel.SignUpViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module{
    viewModel { AccessTokenViewModel(get(), get()) }
    viewModel { SignInViewModel(get(), get()) }
    viewModel { SignUpViewModel(get()) }
    viewModel { ClothAddViewModel(get()) }
    viewModel { ClosetViewModel(get()) }
}