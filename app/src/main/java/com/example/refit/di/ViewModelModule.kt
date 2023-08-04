package com.example.refit.di

import com.example.refit.presentation.AccessTokenViewModel
import com.example.refit.presentation.closet.viewmodel.ClosetViewModel
import com.example.refit.presentation.closet.viewmodel.ClothAddViewModel
import com.example.refit.presentation.signin.viewmodel.SignInViewModel
import com.example.refit.presentation.community.viewmodel.CommunityAddPostViewModel
import com.example.refit.presentation.community.viewmodel.CommunityInfoViewModel
import com.example.refit.presentation.community.viewmodel.CommunitySearchViewModel
import com.example.refit.presentation.community.viewmodel.CommunityViewModel
import com.example.refit.presentation.community.viewmodel.PostReportViewModel
import com.example.refit.presentation.mypage.viewmodel.MyFeedViewModel
import com.example.refit.presentation.mypage.viewmodel.MyInfoViewModel
import com.example.refit.presentation.mypage.viewmodel.MyPageViewModel
import com.example.refit.presentation.mypage.viewmodel.MyScrapViewModel
import com.example.refit.presentation.signup.viewmodel.SignUpViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { AccessTokenViewModel(get(), get()) }
    viewModel { SignInViewModel(get(), get()) }
    viewModel { SignUpViewModel(get()) }
    viewModel { ClothAddViewModel(get()) }
    viewModel { ClosetViewModel(get()) }
    viewModel { CommunityViewModel(get(), get()) }
    viewModel { CommunityInfoViewModel(get()) }
    viewModel { CommunityAddPostViewModel(get(), get()) }
    viewModel { CommunitySearchViewModel(get(), get()) }
    viewModel { PostReportViewModel(get()) }
    viewModel { MyScrapViewModel(get(), get())}
    viewModel { MyFeedViewModel(get(), get()) }
    viewModel { MyPageViewModel(get()) }
    viewModel { MyInfoViewModel(get(), get()) }

}