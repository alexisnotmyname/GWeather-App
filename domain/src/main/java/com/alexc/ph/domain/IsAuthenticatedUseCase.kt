package com.alexc.ph.domain

import com.alexc.ph.domain.repository.AuthRepository

class IsAuthenticatedUseCase(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Boolean = authRepository.getToken() != null
}