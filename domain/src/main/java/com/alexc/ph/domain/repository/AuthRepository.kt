package com.alexc.ph.domain.repository

import com.alexc.ph.domain.model.Result
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun authenticate(email: String, password: String): Result<String>
    suspend fun createAccount(email: String, password: String)
    fun signOut()
    fun saveToken(token: String): String
    fun getToken(): String?
}