package com.enesay.android_task.domain.repository

interface AuthRepository {
    suspend fun ensureValidToken () : Boolean
}