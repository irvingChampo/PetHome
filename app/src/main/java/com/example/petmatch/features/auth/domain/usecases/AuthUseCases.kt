package com.example.petmatch.features.auth.domain.usecases
import com.example.petmatch.features.auth.domain.repositories.AuthRepository

class LoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(e: String, p: String) = repository.login(e, p)
}

class RegisterUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(n: String, e: String, p: String, t: String) = repository.registro(n, e, p, t)
}