package com.example.teste.main.data.response

import com.example.teste.framework.data.Repositories
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class DataWords(

    @SerializedName("word")
    val word: String?,

    @SerializedName("phonetics")
    val phonetics: List<Phonetics>?,

    @SerializedName("meanings")
    val meanings: List<Meaning>?,


) : Serializable

data class Phonetics(

    @SerializedName("audio")
    val audio: String?,

    @SerializedName("text")
    val text: String?

) : Serializable

data class Meaning(

    @SerializedName("definitions")
    val definitions: List<Definition>?,

    @SerializedName("partOfSpeech")
    val partOfSpeech: String?

) : Serializable

data class Definition(

    @SerializedName("definition")
    val definition: String?

) : Serializable

data class ListDataWords(

    val responseApi: List<DataWords>,
    val repositories: Repositories

) : Serializable