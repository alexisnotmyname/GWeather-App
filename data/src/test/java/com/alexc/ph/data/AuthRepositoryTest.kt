package com.alexc.ph.data

import com.alexc.ph.data.local.SecurePreferences
import com.alexc.ph.data.repository.AuthRepositoryImpl
import com.alexc.ph.domain.model.Result
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GetTokenResult
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class AuthRepositoryTest {

    private lateinit var repository: AuthRepositoryImpl
    @MockK(relaxed = true) private lateinit var firebaseAuth: FirebaseAuth
    @MockK(relaxed = true) private lateinit var securePreferences: SecurePreferences
    @MockK private lateinit var authResult: AuthResult
    @MockK private lateinit var firebaseUser: FirebaseUser
    @MockK private lateinit var getTokenResult: GetTokenResult

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = AuthRepositoryImpl(firebaseAuth, securePreferences)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `Should authenticate user`() = runTest {
        // Arrange
        val email = "test@example.com"
        val password = "password123"
        val token = "fake-jwt-token"

        every { firebaseAuth.signInWithEmailAndPassword(email, password) } returns Tasks.forResult(authResult)
        every { authResult.user } returns firebaseUser
        every { firebaseUser.getIdToken(true) } returns Tasks.forResult(getTokenResult)
        every { getTokenResult.token } returns token

        val result = repository.authenticate(email, password)

        assertTrue(result is Result.Success)
        assertEquals(token, (result as Result.Success).data)

        verify { firebaseAuth.signInWithEmailAndPassword(any(), any()) }
    }

    @Test
    fun `Should return error when authentication fails`() = runTest {
        val email = "test@example.com"
        val password = "wrongpassword"
        val exception = Exception("ERROR_INVALID_CREDENTIALS")

        every { firebaseAuth.signInWithEmailAndPassword(email, password) } returns Tasks.forException(exception)

        val result = repository.authenticate(email, password)

        assertTrue(result is Result.Error)
        assertEquals(exception, (result as Result.Error).exception)
    }

    @Test
    fun `Should clear authentication token on sign out`() {
        repository.signOut()
        verify { firebaseAuth.signOut() }
        verify { securePreferences.clearAuthToken() }
    }

    @Test
    fun `Should store token correctly on saveToken`() {
        val token = "saved-jwt-token"

        val result = repository.saveToken(token)

        assertEquals(token, result)
        verify { securePreferences.saveAuthToken(token) }
    }

    @Test
    fun `Should return token on getToken`() {
        val token = "retrieved-jwt-token"
        every { securePreferences.getAuthToken() } returns token

        val result = repository.getToken()

        assertEquals(token, result)
    }
}