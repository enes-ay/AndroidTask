package com.enesay.android_task.data.repository

import com.enesay.android_task.data.remote.dto.LoginRequest
import com.enesay.android_task.data.remote.ApiService
import com.enesay.android_task.domain.repository.AuthRepository
import com.enesay.android_task.utils.PASSWORD
import com.enesay.android_task.utils.helper.SecureTokenManager
import com.enesay.android_task.utils.USERNAME
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val secureTokenManager: SecureTokenManager,
) : AuthRepository {

    override suspend fun ensureValidToken(): Boolean {
        // Do we have token?
        val currentToken = secureTokenManager.getToken().firstOrNull()
        if (!currentToken.isNullOrBlank()) {
            return true
        }
        // If there is no valid token we need login and get a new one
        return try {
            val loginResponse = api.login(LoginRequest(USERNAME, PASSWORD))
            val newToken = loginResponse.oauth?.accessToken

            if (!newToken.isNullOrBlank()) {
                // Saving the new token in datastore for future use
                secureTokenManager.saveToken(newToken)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
