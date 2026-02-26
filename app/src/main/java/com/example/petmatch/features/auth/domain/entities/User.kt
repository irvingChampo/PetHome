package com.example.petmatch.features.auth.domain.entities

data class User(
    val token: String,
    val role: String // "admin" o "voluntario"
)