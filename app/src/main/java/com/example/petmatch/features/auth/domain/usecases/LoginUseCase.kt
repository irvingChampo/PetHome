package com.example.petmatch.features.auth.domain.usecases

import com.example.petmatch.features.auth.domain.repositories.AuthRepository
import com.example.petmatch.features.auth.domain.entities.User
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(e: String, p: String): Result<User> = repository.login(e, p)
}