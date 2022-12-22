package com.example.teste.framework.data.model

import com.example.teste.framework.data.Repositories
import com.example.teste.framework.enum.Action

data class RepositoriesAction(
    val repositories: Repositories,
    val action: Action
)
