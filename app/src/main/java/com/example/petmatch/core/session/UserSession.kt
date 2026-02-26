package com.example.petmatch.core.session

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserSession @Inject constructor() {
    var token: String? = null
        private set
    var role: String? = null
        private set

    fun saveSession(token: String, role: String) {
        this.token = token
        this.role = role
    }

    fun clearSession() {
        this.token = null
        this.role = null
    }

    fun isAdmin(): Boolean = role?.lowercase() == "admin"
    fun isVoluntario(): Boolean = role?.lowercase() == "voluntario"
}