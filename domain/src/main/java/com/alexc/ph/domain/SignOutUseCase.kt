package com.alexc.ph.domain

import com.alexc.ph.domain.repository.AuthRepository

class SignOutUseCase(
    private val authRepository: AuthRepository
) {
    operator fun invoke() {
        authRepository.signOut()
    }
}