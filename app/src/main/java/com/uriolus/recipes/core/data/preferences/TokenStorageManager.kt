package com.uriolus.recipes.core.data.preferences

import android.content.Context
import android.util.Base64
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.uriolus.recipes.core.data.EncryptionManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.nio.charset.StandardCharsets
import javax.inject.Inject
import javax.inject.Singleton

// Define the DataStore instance at the top level
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session_prefs")

@Singleton
class TokenStorageManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val encryptionManager: EncryptionManager
) {

    private object PreferencesKeys {
        val ACCESS_TOKEN = stringPreferencesKey("encrypted_access_token")
    }

    val accessTokenFlow: Flow<String?> = context.dataStore.data
        .map {
            val encryptedToken = it[PreferencesKeys.ACCESS_TOKEN]
            encryptedToken?.let {
                try {
                    val decryptedBytes = encryptionManager.decrypt(Base64.decode(it, Base64.DEFAULT))
                    String(decryptedBytes, StandardCharsets.UTF_8)
                } catch (e: Exception) {
                    clearAccessToken()
                    null
                }
            }
        }

    suspend fun saveAccessToken(token: String) {
        try {
            val encryptedTokenBytes = encryptionManager.encrypt(token.toByteArray(StandardCharsets.UTF_8))
            val encryptedTokenBase64 = Base64.encodeToString(encryptedTokenBytes, Base64.DEFAULT)
            context.dataStore.edit {
                it[PreferencesKeys.ACCESS_TOKEN] = encryptedTokenBase64
            }
        } catch (e: Exception) {
        }
    }

    suspend fun clearAccessToken() {
        context.dataStore.edit {
            it.remove(PreferencesKeys.ACCESS_TOKEN)
        }
    }
}
