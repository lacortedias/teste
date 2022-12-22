package com.example.teste.main.data.repository

import com.example.teste.framework.data.Repositories
import com.example.teste.framework.data.RepositoriesDao
import com.example.teste.framework.network.ResultWrapper
import com.example.teste.framework.network.call
import com.example.teste.framework.util.FileUtil
import com.example.teste.main.data.MainService
import com.example.teste.main.data.response.DataWords

class MainRepositoryImpl(
    private val repositoriesDao: RepositoriesDao,
    private val service: MainService
) : MainRepository {

    override suspend fun getWord(word: String): ResultWrapper<List<DataWords>> {
        val result = service.getWord(word)
        return if (result.isSuccessful) {
            call { result.body() ?: arrayListOf() }
        } else {
            ResultWrapper.Error()
        }
    }

    override suspend fun getRepositories(): ResultWrapper<List<Repositories>> {

        val words = FileUtil.getWordsFromUrl()
        if (repositoriesDao.getRepositories().isEmpty()) {
            repositoriesDao.insertRepository(words = words)
        }
        return call { repositoriesDao.getRepositories() }
    }

    override suspend fun getFavoritesWords(): List<Repositories> {
        return repositoriesDao.getFavoritesWords()
    }

    override suspend fun getHistoryWords(): List<Repositories> {
        return repositoriesDao.getHistoryWords()
    }

    override suspend fun insertRepository(words: ArrayList<Repositories>) {
        repositoriesDao.insertRepository(words)
    }

    override suspend fun getWordData(id: Int): Repositories {
        return repositoriesDao.getWordData(id)
    }

    override suspend fun updateRepositories(repositories: Repositories) {
        repositoriesDao.updateRepositories(repositories)
    }
}