package com.alexc.ph.weatherapp

import android.app.Activity
import androidx.credentials.CreatePasswordRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetPasswordOption
import androidx.credentials.PasswordCredential
import androidx.credentials.exceptions.CreateCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException

class AccountManager(private val activity: Activity) {
    private val credentialManager = CredentialManager.create(activity)

    suspend fun signUp(username: String, password: String): SignUpResult {
        return try {
            credentialManager.createCredential(
                context = activity,
                request = CreatePasswordRequest(
                    id = username,
                    password = password
                )
            )
            SignUpResult.Success(username)
        } catch (e: Exception) {
            e.printStackTrace()
            if(e is CreateCredentialCancellationException) {
                SignUpResult.Cancelled
            } else {
                SignUpResult.Failure(e)
            }
        }
    }

    suspend fun signIn(): SignInResult {
        return try {
            val credentialResponse = credentialManager.getCredential(
                context = activity,
                request = GetCredentialRequest(
                    credentialOptions = listOf(GetPasswordOption())
                )
            )

            val credential = credentialResponse.credential as? PasswordCredential
                ?: return SignInResult.Failure

            SignInResult.Success(credential.id, credential.password)
        } catch(e: GetCredentialCancellationException) {
            e.printStackTrace()
            SignInResult.Cancelled
        } catch(e: NoCredentialException) {
            e.printStackTrace()
            SignInResult.NoCredentials
        } catch(e: GetCredentialException) {
            e.printStackTrace()
            SignInResult.Failure
        }
    }
}


sealed interface SignUpResult {
    data class Success(val username: String) : SignUpResult
    data class Failure(val error: Throwable) : SignUpResult
    data object Cancelled: SignUpResult
}

sealed interface SignInResult {
    data class Success(val email: String, val password: String): SignInResult
    data object Cancelled: SignInResult
    data object Failure: SignInResult
    data object NoCredentials: SignInResult
}