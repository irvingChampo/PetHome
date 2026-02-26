package com.example.petmatch.features.auth.domain.usescases

import com.example.petmatch.features.auth.domain.repositories.AuthRepository
import com.example.petmatch.features.auth.domain.entities.User
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(n: String, e: String, p: String, t: String): Result<User> =
        repository.registro(n, e, p, t)
}