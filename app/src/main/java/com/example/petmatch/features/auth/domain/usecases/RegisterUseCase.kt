package com.example.petmatch.features.auth.domain.usecases // Asegúrate que no tenga la 's' extra

import com.example.petmatch.features.auth.domain.repositories.AuthRepository
import com.example.petmatch.features.auth.domain.entities.User
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(n: String, e: String, p: String, t: String, r: String): Result<User> =
        repository.registro(n, e, p, t, r)
}