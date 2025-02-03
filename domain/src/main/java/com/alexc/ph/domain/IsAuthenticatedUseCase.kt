package com.alexc.ph.domain

import com.alexc.ph.domain.repository.AuthRepository
import javax.inject.Inject

class IsAuthenticatedUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Boolean = authRepository.getToken() != null
}