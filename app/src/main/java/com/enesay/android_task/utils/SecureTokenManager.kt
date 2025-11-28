package com.enesay.android_task.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SecureTokenManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    companion object {
        private const val PREFS_FILE_NAME = "secure_app_prefs"
        private const val KEY_TOKEN = "auth_token"
    }

    // 1. Create MasterKey (Hardware-backed security)
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    // 2. Initialize EncryptedSharedPreferences
    // It encrypts keys and values automatically using AES-256
    private val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        PREFS_FILE_NAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    // Save token securely (Encryption happens automatically)
    fun saveToken(token: String) {
        sharedPreferences.edit()
            .putString(KEY_TOKEN, token)
            .apply()
    }

    // Read token as Flow (Decryption happens automatically)
    fun getToken(): Flow<String?> = callbackFlow {
        // Listener to react to changes
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { prefs, key ->
            if (key == KEY_TOKEN) {
                trySend(prefs.getString(KEY_TOKEN, null))
            }
        }

        // Register listener
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)

        // Emit initial value
        trySend(sharedPreferences.getString(KEY_TOKEN, null))

        // Cleanup when Flow is cancelled
        awaitClose {
            sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }
}