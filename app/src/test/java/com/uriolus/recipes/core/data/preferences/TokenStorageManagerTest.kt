package com.uriolus.recipes.core.data.preferences // You'll need to move this file to the correct package structure

import android.content.Context
import android.util.Base64
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore // Import for mocking
import androidx.datastore.preferences.core.MutablePreferences
import com.uriolus.recipes.core.data.EncryptionManager
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import io.mockk.junit5.MockKExtension
import java.nio.charset.StandardCharsets
import kotlin.properties.PropertyDelegateProvider
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

@ExtendWith(MockKExtension::class)
class TokenStorageManagerTest {

    @MockK
    private lateinit var mockContext: Context

    @MockK
    private lateinit var mockDataStore: DataStore<Preferences>

    @MockK
    private lateinit var mockEncryptionManager: EncryptionManager

    private lateinit var tokenStorageManager: TokenStorageManager

    @BeforeEach
    fun setUp() {
        mockkStatic("androidx.datastore.preferences.PreferenceDataStoreDelegateKt")

        val mockReadOnlyProperty =
            ReadOnlyProperty<Context, DataStore<Preferences>> { thisRef, property -> mockDataStore }

        every { preferencesDataStore(name = "session_prefs") } returns mockReadOnlyProperty
        
        tokenStorageManager = TokenStorageManager(mockContext, mockEncryptionManager)
    }

    @AfterEach
    fun tearDown() {
        unmockkStatic("androidx.datastore.preferences.PreferenceDataStoreDelegateKt")
        unmockkAll()
    }

    @Test
    fun `saveAccessToken encrypts and stores token`() = runTest {
        val rawToken = "test_token_123"
        val rawTokenBytes = rawToken.toByteArray(StandardCharsets.UTF_8)
        val encryptedBytes = "encrypted_token_bytes".toByteArray(StandardCharsets.UTF_8)
        val encryptedBase64 = Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
        val prefsKey = stringPreferencesKey("encrypted_access_token")

        coEvery { mockEncryptionManager.encrypt(rawTokenBytes, null) } returns encryptedBytes
        
        val capturedEditLambda = slot<suspend (MutablePreferences) -> Unit>()
        val mockMutablePreferences = mockk<MutablePreferences>(relaxed = true)
        coEvery { mockDataStore.edit(capture(capturedEditLambda)) } coAnswers { 
            capturedEditLambda.captured.invoke(mockMutablePreferences)
            mockk<Preferences>(relaxed = true) // Return a mock Preferences object as per 'edit' signature
        }

        tokenStorageManager.saveAccessToken(rawToken)

        coVerify { mockEncryptionManager.encrypt(rawTokenBytes, null) }
        coVerify { mockDataStore.edit(any()) }
        verify { mockMutablePreferences[prefsKey] = encryptedBase64 }
    }

    @Test
    fun `accessTokenFlow decrypts and emits token`() = runTest {
        val rawToken = "test_token_123"
        val rawTokenBytes = rawToken.toByteArray(StandardCharsets.UTF_8)
        val encryptedBytes = "encrypted_token_bytes".toByteArray(StandardCharsets.UTF_8)
        val encryptedBase64 = Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
        val prefsKey = stringPreferencesKey("encrypted_access_token")

        val mockPreferences = mockk<Preferences>()
        every { mockPreferences[prefsKey] } returns encryptedBase64
        every { mockDataStore.data } returns flowOf(mockPreferences)
        coEvery { mockEncryptionManager.decrypt(Base64.decode(encryptedBase64, Base64.DEFAULT), null) } returns rawTokenBytes

        val retrievedToken = tokenStorageManager.accessTokenFlow.first()

        assertEquals(rawToken, retrievedToken)
        coVerify { mockEncryptionManager.decrypt(Base64.decode(encryptedBase64, Base64.DEFAULT), null) }
    }

    @Test
    fun `accessTokenFlow emits null and clears token on decryption error`() = runTest {
        val corruptedBase64Token = "corrupted_base64_token"
        val decodedCorruptedBytes = Base64.decode(corruptedBase64Token, Base64.DEFAULT)
        val prefsKey = stringPreferencesKey("encrypted_access_token")

        val mockPreferences = mockk<Preferences>()
        every { mockPreferences[prefsKey] } returns corruptedBase64Token
        every { mockDataStore.data } returns flowOf(mockPreferences)
        coEvery { mockEncryptionManager.decrypt(decodedCorruptedBytes, null) } throws RuntimeException("Decryption failed")
        
        val capturedEditLambda = slot<suspend (MutablePreferences) -> Unit>()
        val mockMutablePreferences = mockk<MutablePreferences>(relaxed = true)
        coEvery { mockDataStore.edit(capture(capturedEditLambda)) } coAnswers { 
            capturedEditLambda.captured.invoke(mockMutablePreferences)
            mockk<Preferences>(relaxed = true)
        }

        val retrievedToken = tokenStorageManager.accessTokenFlow.first()

        assertNull(retrievedToken)
        coVerify { mockEncryptionManager.decrypt(decodedCorruptedBytes, null) }
        coVerify { mockDataStore.edit(any()) }
        verify { mockMutablePreferences.remove(prefsKey) }
    }

    @Test
    fun `clearAccessToken removes token`() = runTest {
        val prefsKey = stringPreferencesKey("encrypted_access_token")
        val capturedEditLambda = slot<suspend (MutablePreferences) -> Unit>()
        val mockMutablePreferences = mockk<MutablePreferences>(relaxed = true)

        coEvery { mockDataStore.edit(capture(capturedEditLambda)) } coAnswers { 
            capturedEditLambda.captured.invoke(mockMutablePreferences)
            mockk<Preferences>(relaxed = true)
        }

        tokenStorageManager.clearAccessToken()

        coVerify { mockDataStore.edit(any()) }
        verify { mockMutablePreferences.remove(prefsKey) }
    }
}
