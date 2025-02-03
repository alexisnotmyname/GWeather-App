package com.alexc.ph.domain

import com.alexc.ph.domain.common.InvalidEmailException
import com.alexc.ph.domain.common.PasswordFormatInvalidException
import com.alexc.ph.domain.common.PasswordNotMatchException
import com.alexc.ph.domain.repository.AuthRepository
import com.alexc.ph.domain.common.isValidEmail
import com.alexc.ph.domain.common.isValidPassword
import com.alexc.ph.domain.common.passwordMatches
import com.alexc.ph.domain.model.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(email: String, password: String, repeatPassword: String): Flow<Result<Unit>> = flow {
        emit(Result.Loading)
        if (!email.isValidEmail()) {
            emit(Result.Error(InvalidEmailException()))
            return@flow
        }

        if (!password.isValidPassword()) {
            emit(Result.Error(PasswordFormatInvalidException()))
            return@flow
        }

        if (!password.passwordMatches(repeatPassword)) {
            emit(Result.Error(PasswordNotMatchException()))
            return@flow
        }
        emit(Result.Success(authRepository.createAccount(email, password)))
    }
}

