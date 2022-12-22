package com.example.teste.framework.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.teste.main.data.response.ListDataWords
import java.io.Serializable

@Entity(tableName = "word_table")
data class Repositories(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "word") val word: String,
    @ColumnInfo(name = "isFavorite") val isFavorite: Boolean = false,
    @ColumnInfo(name = "isHistory") val isHistory: Boolean = false

): Serializable