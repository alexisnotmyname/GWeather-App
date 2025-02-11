package com.alexc.ph.domain

import com.alexc.ph.domain.common.InvalidEmailException
import com.alexc.ph.domain.common.PasswordBlankException
import com.alexc.ph.domain.repository.AuthRepository
import com.alexc.ph.domain.common.isValidEmail
import com.alexc.ph.domain.model.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class SignInUseCase(
    private val authRepository: AuthRepository
) {
    operator fun invoke(email: String, password: String): Flow<Result<String>> = flow {
        emit(Result.Loading)
        if (!email.isValidEmail()) {
            emit(Result.Error(InvalidEmailException()))
            return@flow
        }

        if (password.isBlank()) {
            emit(Result.Error(PasswordBlankException()))
            return@flow
        }

        when(val result = authRepository.authenticate(email, password)){
            is Result.Error -> emit(Result.Error(result.exception))
            Result.Loading -> emit(Result.Loading)
            is Result.Success -> emit(Result.Success(authRepository.saveToken(result.data)))
        }
    }
}