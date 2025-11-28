package com.enesay.android_task.data.remote.interceptors

import com.enesay.android_task.data.remote.ApiService
import com.enesay.android_task.data.remote.dto.LoginRequest
import com.enesay.android_task.utils.PASSWORD
import com.enesay.android_task.utils.helper.SecureTokenManager
import com.enesay.android_task.utils.USERNAME
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Provider

class TokenAuthenticator @Inject constructor(
    private val tokenDatastore: SecureTokenManager, private val apiServiceProvider: Provider<ApiService>
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {

        /* Since the API never returns a 401 (unauthorized or invalid token) response
        I couldn't check the validity of the token. The possible solution may be apply a worker to renew
        the token by using its expire duration before it becomes obsolete.
        */
        if (response.request.url.toString().contains("/login")) {
            return null
        }
        // Getting a new token from remote
        val newToken = runBlocking {
            try {
                val loginResponse = apiServiceProvider.get().login(
                    LoginRequest(USERNAME, PASSWORD)
                )

                val token = loginResponse.oauth?.accessToken
                if (token != null) tokenDatastore.saveToken(token)
                token
            } catch (e: Exception) {
                null
            }
        }
        // Header overriding with new valid token
        return if (newToken != null) {
            response.request.newBuilder().header(
                    "Authorization", "Bearer $newToken"
                )
                .build()
        } else {
            null
        }
    }
}