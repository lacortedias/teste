package com.example.teste.main.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.teste.data.model.RepositoriesFactoryTest
import com.example.teste.framework.data.Repositories
import com.example.teste.framework.data.model.RepositoriesAction
import com.example.teste.framework.enum.Action
import com.example.teste.framework.network.ResultWrapper
import com.example.teste.main.data.repository.MainRepository
import com.example.teste.main.data.response.DataWords
import com.example.teste.main.data.response.Definition
import com.example.teste.main.data.response.ListDataWords
import com.example.teste.main.data.response.Meaning
import com.example.teste.main.viewstate.MainViewState
import com.example.teste.util.TestCoroutineRule
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var mainRepository: MainRepository

    private lateinit var mainViewModel: MainViewModel

    private val fakeRepositories = RepositoriesFactoryTest()
        .create(RepositoriesFactoryTest.FakeRepositories.FakeRepositories1)
    var definition = listOf(Meaning(listOf(Definition("definition")), null))

    @Before
    fun setUp() {
        mainViewModel = MainViewModel(mainRepository)
    }

    @Test
    fun `should report success in getting cache`() = runTest{

        //arrange
        val cachedData = DataWords(word = "fakeWords", phonetics = null,  definition)
        mainViewModel.cacheTemp.add(cachedData)

        //act
        mainViewModel.getWord(fakeRepositories)

        //assert
        assertEquals(fakeRepositories.word, mainViewModel.cacheTemp.map { it.word }.first())
    }

    @Test
    fun `should return result success`() = runTest{

        //arrange
        val returnApi = listOf(DataWords(word = "fakeWords", phonetics = null,  definition))
        val request = mainRepository.getWord(fakeRepositories.word)
        whenever(request).thenReturn(ResultWrapper.Success(returnApi))

        //act
        mainViewModel.getWord(fakeRepositories)

        //assert
        assertEquals(mainViewModel.listCallApi.value?.responseApi, returnApi)
    }

    @Test
    fun `should return result error`() = runTest{

        //arrange
        val errorMessage = "Erro ao buscar palavra"
        val request = mainRepository.getWord(fakeRepositories.word)
        whenever(request).thenReturn(ResultWrapper.Error(errorMessage))

        //act
        mainViewModel.getWord(fakeRepositories)

        //assert
        assertEquals(ResultWrapper.Error(errorMessage).message, errorMessage)
    }

    @Test
    fun `test getWordData returns expected value`() = runTest {

        //arrange
        val expectedValue = RepositoriesAction(fakeRepositories, Action.HISTORY)
        val request = mainRepository.getWordData(any())
        whenever(request).thenReturn(expectedValue.repositories)

        //act
        mainViewModel.getWordData(fakeRepositories.id, expectedValue.action)

        //assert

        assertEquals(expectedValue, mainViewModel.getWordData.value)
    }

    @Test
    fun `test updateRepository returns expected value`() = runTest {

        //arrange
        val request = mainRepository.updateRepositories(fakeRepositories)
        whenever(request).thenReturn(Unit)

        //act
        mainViewModel.updateRepositories(fakeRepositories).join()

        //assert

        verify(mainRepository, times(1)).updateRepositories(fakeRepositories)
    }

    @Test
    fun `test getFavoritesWords returns expected value`() = runTest {

        //arrange
        val expectedValue = Repositories(
            id = fakeRepositories.id,
            word = fakeRepositories.word,
            isFavorite = true,
            isHistory = true
        )
        val request = mainRepository.getFavoritesWords()
        whenever(request).thenReturn(listOf(expectedValue))

        //act
        mainViewModel.getFavoritesWords()

        //assert

        assertEquals(expectedValue, mainViewModel.listRepositories.value?.get(0))
    }

    @Test
    fun `test getHistoryWords returns expected value`() = runTest {

        //arrange
        val expectedValue = Repositories(
            id = fakeRepositories.id,
            word = fakeRepositories.word,
            isHistory = true
        )
        val request = mainRepository.getHistoryWords()
        whenever(request).thenReturn(listOf(expectedValue))

        //act
        mainViewModel.getHistoryWords()

        //assert

        assertEquals(expectedValue, mainViewModel.listRepositories.value?.get(0))
    }

    @Test
    fun `test getRepositories returns success`() = runTest {

        //arrange
        val request = mainRepository.getRepositories()
        whenever(request).thenReturn(ResultWrapper.Success(listOf(fakeRepositories)))

        //act
        mainViewModel.getRepositories()

        //assert
        assertEquals(mainViewModel.listRepositories.value?.get(0)?.word, fakeRepositories.word)
    }

    @Test
    fun `test getRepositories returns error`() = runTest {

        //arrange
        val errorMessage = "Erro ao buscar palavra"
        val request = mainRepository.getRepositories()
        whenever(request).thenReturn(ResultWrapper.Error(errorMessage))

        //act
        mainViewModel.getRepositories()

        //assert
        assertEquals(ResultWrapper.Error(errorMessage).message, errorMessage)
    }

    @Test
    fun `test searchWords returns success`() = runTest {

        //arrange
        val request = mainRepository.searchWords(fakeRepositories.word)
        whenever(request).thenReturn(ResultWrapper.Success(listOf(fakeRepositories)))

        //act
        mainViewModel.searchWords(fakeRepositories.word)

        //assert
        assertEquals(mainViewModel.listRepositories.value?.find {
            it.word == fakeRepositories.word
        }?.word, fakeRepositories.word)
    }

    @Test
    fun `test searchWords returns error`() = runTest {

        //arrange
        val errorMessage = "Erro ao buscar palavra"
        val request = mainRepository.searchWords(fakeRepositories.word)
        whenever(request).thenReturn(ResultWrapper.Error())

        //act
        mainViewModel.searchWords(fakeRepositories.word)

        //assert
        assertEquals(ResultWrapper.Error(errorMessage).message, errorMessage)
    }
}