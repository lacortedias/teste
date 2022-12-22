package com.example.teste.main.di

import com.example.teste.framework.data.AppDatabase
import com.example.teste.framework.network.RetrofitProvider
import com.example.teste.main.data.MainService
import com.example.teste.main.data.repository.MainRepository
import com.example.teste.main.data.repository.MainRepositoryImpl
import com.example.teste.main.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {
    single { AppDatabase.getDatabase(get()) }
    single { AppDatabase.provideRepositoriesDao(get()) }
    single<MainRepository> { MainRepositoryImpl(get(), get()) }
    single<MainService> { RetrofitProvider.getApiService() }
    viewModel { MainViewModel(get()) }
}