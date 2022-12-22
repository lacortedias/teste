package com.example.teste.framework.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Repositories::class], version = 1, exportSchema = false)
abstract class Database(): RoomDatabase() {
    abstract fun initDatabase(): RepositoriesDao
}