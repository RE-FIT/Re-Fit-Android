package com.example.refit.di

import com.example.refit.data.repository.chat.ChatRepository
import com.example.refit.data.repository.chat.ChatRepositoryImpl
import com.example.refit.data.repository.colset.ClosetRepository
import com.example.refit.data.repository.colset.ClosetRepositoryImpl
import com.example.refit.data.repository.community.CommunityRepository
import com.example.refit.data.repository.community.CommunityRepositoryImpl
import com.example.refit.data.repository.mypage.MyPageRepository
import com.example.refit.data.repository.mypage.MyPageRepositoryImpl
import com.example.refit.data.repository.signup.SignUpRepository
import com.example.refit.data.repository.signup.SignUpRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<ChatRepository> { ChatRepositoryImpl(get()) }
    single<SignUpRepository> { SignUpRepositoryImpl(get()) }
    single<ClosetRepository> { ClosetRepositoryImpl(get()) }
    single<CommunityRepository> { CommunityRepositoryImpl(get()) }
    single<MyPageRepository> { MyPageRepositoryImpl(get()) }
}