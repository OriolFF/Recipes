package com.uriolus.recipes.core.data // Adjusted package temporarily

import android.content.Context
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.aead.AeadKeyTemplates
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import java.security.GeneralSecurityException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EncryptionManager @Inject constructor(@ApplicationContext private val context: Context) {

    private val keysetName = "master_keyset"
    private val preferenceFileName = "master_key_preference"
    private val masterKeyUri = "android-keystore://master_key"

    private val keysetHandle: KeysetHandle
    private val aead: Aead

    init {
        try {
            // Make sure AeadConfig.register() has been called in Application.onCreate()
            keysetHandle = AndroidKeysetManager.Builder()
                .withSharedPref(context, keysetName, preferenceFileName)
                .withKeyTemplate(AeadKeyTemplates.AES256_GCM) // Standard template
                .withMasterKeyUri(masterKeyUri)
                .build()
                .keysetHandle
            aead = keysetHandle.getPrimitive(Aead::class.java)
        } catch (e: GeneralSecurityException) {
            throw RuntimeException("Failed to initialize EncryptionManager: Tink Keyset error. Ensure Tink is initialized in Application class.", e)
        } catch (e: IOException) {
            throw RuntimeException("Failed to initialize EncryptionManager: Keyset I/O error.", e)
        }
    }

    fun encrypt(plaintext: ByteArray, associatedData: ByteArray? = null): ByteArray {
        return aead.encrypt(plaintext, associatedData ?: byteArrayOf())
    }

    fun decrypt(ciphertext: ByteArray, associatedData: ByteArray? = null): ByteArray {
        return aead.decrypt(ciphertext, associatedData ?: byteArrayOf())
    }
}
