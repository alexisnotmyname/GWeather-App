package com.alexc.ph.data.repository

import com.alexc.ph.data.local.SecurePreferences
import com.alexc.ph.domain.model.Result
import com.alexc.ph.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val securePreferences: SecurePreferences
): AuthRepository {

    override suspend fun authenticate(email: String, password: String): Result<String>  {
        return try  {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            result.user?.getIdToken(true)?.await()?.token?.let {
                Result.Success(it)
            } ?: Result.Error(Exception("Failed to retrieve token"))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun createAccount(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).await()
    }

    override fun signOut() {
        firebaseAuth.signOut()
        securePreferences.clearAuthToken()
    }

    override fun saveToken(token: String): String {
        securePreferences.saveAuthToken(token)
        return token
    }

    override fun getToken(): String? {
        return securePreferences.getAuthToken()
    }
}