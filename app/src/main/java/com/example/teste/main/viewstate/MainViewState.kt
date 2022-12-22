package com.example.teste.main.viewstate

sealed class MainViewState{
    data class Error(val message: String) : MainViewState()
    data class Loading(val show: Boolean) : MainViewState()
}
