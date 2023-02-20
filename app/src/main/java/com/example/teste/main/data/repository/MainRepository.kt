package com.example.teste.main.data.repository

import com.example.teste.framework.data.Repositories
import com.example.teste.framework.network.ResultWrapper
import com.example.teste.main.data.response.DataWords

interface MainRepository {
    suspend fun insertRepository(word: ArrayList<Repositories>)
    suspend fun getWordData(id: Int): Repositories
    suspend fun getWord(word: String): ResultWrapper<List<DataWords>>
    suspend fun getRepositories(): ResultWrapper<List<Repositories>>
    suspend fun getFavoritesWords(): List<Repositories>
    suspend fun getHistoryWords(): List<Repositories>
    suspend fun updateRepositories(repositories: Repositories)
    suspend fun searchWords(search: String): ResultWrapper<List<Repositories>>
}