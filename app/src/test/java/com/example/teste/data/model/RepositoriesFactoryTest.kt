package com.example.teste.data.model

import com.example.teste.framework.data.Repositories

class RepositoriesFactoryTest {

    fun create(repositories: FakeRepositories) = when (repositories) {
        FakeRepositories.FakeRepositories1 -> Repositories(
            1,
            "fakeWords"
        )
    }

    sealed class FakeRepositories {
        object FakeRepositories1 : FakeRepositories()
    }
}