package com.example.teste.framework.data

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase

abstract class AppDatabase : RoomDatabase() {

    companion object {
        fun getDatabase(application: Application): Database {
            return Room.databaseBuilder(
                    application,
                    Database::class.java,
                    "word_table"
                ).build()
        }

        fun provideRepositoriesDao(db: Database): RepositoriesDao{
            return db.initDatabase()
        }
    }
}