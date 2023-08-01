package com.mibaldi.virtualassistant.ui.biometric

import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.PromptInfo
import androidx.core.content.ContextCompat

fun AppCompatActivity.setupPrompt(levelAuthenticator: LevelAuthenticator): PromptInfo? {
    var promptInfo: PromptInfo? = null
    if (BiometricManager.from(this).canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL
        ) == BiometricManager.BIOMETRIC_SUCCESS) {

        val promptInfoBuilder = PromptInfo.Builder()
            .setTitle("Autenticacion Biometrica")
            .setSubtitle("Autenticate utiliznado el sensor biometrico")
        val negativeButtonText = "Cancel"
        when (levelAuthenticator){
            LevelAuthenticator.STRONG -> {
                promptInfoBuilder.setNegativeButtonText(negativeButtonText)
                promptInfoBuilder.setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
                promptInfo = promptInfoBuilder.build()
            }
            LevelAuthenticator.WEAK -> {
                promptInfoBuilder.setNegativeButtonText(negativeButtonText)
                promptInfoBuilder.setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_WEAK)
                promptInfo = promptInfoBuilder.build()

            }
            LevelAuthenticator.WEAK_CREDENTIAL -> {
                promptInfoBuilder.setAllowedAuthenticators(
                    BiometricManager.Authenticators.BIOMETRIC_WEAK or BiometricManager.Authenticators.DEVICE_CREDENTIAL
                )
                promptInfo = promptInfoBuilder.build()
            }
        }
    }
    return promptInfo
}

fun AppCompatActivity.authenticate(promptInfo: PromptInfo?, auth: (auth:Boolean)-> Unit){
    if (promptInfo != null){
        BiometricPrompt(this, ContextCompat.getMainExecutor(this),object: BiometricPrompt.AuthenticationCallback(){
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult){
                super.onAuthenticationSucceeded(result)
                auth(true)
            }
        }).authenticate(promptInfo)
    } else {
        auth(true)
    }
}
sealed class LevelAuthenticator{
    object STRONG : LevelAuthenticator()
    object WEAK : LevelAuthenticator()
    object WEAK_CREDENTIAL : LevelAuthenticator()
}
