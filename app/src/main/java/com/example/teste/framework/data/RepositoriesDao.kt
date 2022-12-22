package com.example.teste.framework.data

import androidx.room.*

@Dao
interface RepositoriesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRepository(words: ArrayList<Repositories>)

    @Query("SELECT * FROM word_table WHERE id = :id")
    suspend fun getWordData(id: Int): Repositories

    @Query("SELECT * FROM word_table WHERE isFavorite = 1")
    suspend fun getFavoritesWords(): List<Repositories>

    @Query("SELECT * FROM word_table WHERE isHistory = 1")
    suspend fun getHistoryWords(): List<Repositories>

    @Query("SELECT * FROM word_table")
    suspend fun getRepositories(): List<Repositories>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateRepositories(repositories: Repositories)

}