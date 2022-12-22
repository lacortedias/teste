package com.example.teste.framework.util

import com.example.teste.BuildConfig
import com.example.teste.framework.data.Repositories
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL

object FileUtil {

    suspend fun getWordsFromUrl(): ArrayList<Repositories> {

        return withContext(Dispatchers.IO) {
            val listWord: ArrayList<Repositories> = arrayListOf()
            var line: String?

            val url = URL(BuildConfig.WORD_LIST_URL);
            val file = BufferedReader(InputStreamReader(url.openStream()));
            while (file.readLine().also { line = it } != null) {
                line?.let {
                    listWord.add(Repositories(word = it))
                }
            }
            file.close()
            listWord
        }

    }

    const val KEY_BUNDLE = "keyBundle"

}