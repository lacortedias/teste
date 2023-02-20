package com.example.teste.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teste.framework.data.Repositories
import com.example.teste.framework.data.model.RepositoriesAction
import com.example.teste.framework.enum.Action
import com.example.teste.framework.network.ResultWrapper
import com.example.teste.framework.util.FileUtil
import com.example.teste.main.data.repository.MainRepository
import com.example.teste.main.data.response.DataWords
import com.example.teste.main.data.response.ListDataWords
import com.example.teste.main.viewstate.MainViewState
import kotlinx.coroutines.launch


class MainViewModel(private val repository: MainRepository) : ViewModel() {

    val viewState: MutableLiveData<MainViewState> = MutableLiveData()
    var listCallApi: MutableLiveData<ListDataWords> = MutableLiveData()
    var getWordData: MutableLiveData<RepositoriesAction> = MutableLiveData()
    val listRepositories: MutableLiveData<List<Repositories>> = MutableLiveData()
    val cacheTemp = arrayListOf<DataWords>()

    fun getWord(repositories: Repositories) = viewModelScope.launch {
        viewState.value = MainViewState.Loading(true)

        val dataCacheTemp = cacheTemp.find {
            it.word == repositories.word
        }
        dataCacheTemp?.let {
            listCallApi.value = ListDataWords(arrayListOf(it), repositories)
            viewState.value = MainViewState.Loading(false)
            return@launch
        }

        when (val response = repository.getWord(repositories.word)) {
            is ResultWrapper.Success -> {
                response.value.firstOrNull()?.let { cacheTemp.add(it) }
                listCallApi.value = ListDataWords(response.value, repositories)
            }
            is ResultWrapper.Error -> {
                viewState.value = MainViewState.Error(response.message)
            }
        }
        viewState.value = MainViewState.Loading(false)
    }

    fun getWordData(id: Int, action: Action) = viewModelScope.launch {
        getWordData.value = RepositoriesAction(repository.getWordData(id), action)
    }

    fun updateRepositories(repositories: Repositories) = viewModelScope.launch {
        repository.updateRepositories(repositories)
    }

    fun getFavoritesWords() = viewModelScope.launch {
        viewState.value = MainViewState.Loading(true)
        listRepositories.value = repository.getFavoritesWords()
        viewState.value = MainViewState.Loading(false)
    }

    fun getHistoryWords() = viewModelScope.launch {
        viewState.value = MainViewState.Loading(true)
        listRepositories.value = repository.getHistoryWords()
        viewState.value = MainViewState.Loading(false)
    }

    fun getRepositories() = viewModelScope.launch {
        viewState.value = MainViewState.Loading(true)

        when (val result = repository.getRepositories()) {
            is ResultWrapper.Success -> {
                result.value.let {
                    listRepositories.value = it
                }
            }
            is ResultWrapper.Error -> {
                viewState.value = MainViewState.Error(result.message)
            }
        }
        viewState.value = MainViewState.Loading(false)
    }

    fun searchWords(search: String) = viewModelScope.launch {
        viewState.value = MainViewState.Loading(true)

        if (search.isBlank()) {
            getRepositories()
        } else {
            when (val result = repository.searchWords(search)) {
                is ResultWrapper.Success -> {
                    listRepositories.value = result.value.filter {
                        it.word.startsWith(search, true)
                    }
                }
                is ResultWrapper.Error -> {
                    viewState.value = MainViewState.Error(result.message)
                }
            }
        }
        viewState.value = MainViewState.Loading(false)
    }
}