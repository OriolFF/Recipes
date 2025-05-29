package com.uriolus.recipes

import android.app.Application
import com.google.crypto.tink.Aead
import com.google.crypto.tink.aead.AeadConfig
import dagger.hilt.android.HiltAndroidApp
import java.security.GeneralSecurityException

@HiltAndroidApp
class RecipeApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        try {
            AeadConfig.register()
        } catch (e: GeneralSecurityException) {
            // Handle Tink initialization error, e.g., log it or crash
            throw RuntimeException("Failed to initialize Tink", e)
        }
    }
}
