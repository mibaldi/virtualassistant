package com.mibaldi.virtualassistant.data.managers

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

private fun keyGenParameterSpec(): KeyGenParameterSpec {
    return KeyGenParameterSpec.Builder("KEY_NAME", KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
        .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
        .setUserAuthenticationRequired(true)
        // Invalidate the keys if the user has registered a new biometric
        // credential, such as a new fingerprint. Can call this method only
        // on Android 7.0 (API level 24) or higher.
        .setInvalidatedByBiometricEnrollment(true)
        .build()
}
private fun generateSecretKey(keyGenParameterSpec: KeyGenParameterSpec) {
    val keyGenerator = KeyGenerator.getInstance(
        KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
    keyGenerator.init(keyGenParameterSpec)
    keyGenerator.generateKey()
}

private fun getSecretKey(): SecretKey {
    val keyStore = KeyStore.getInstance("AndroidKeyStore")

    // Before the keystore can be accessed, it must be loaded.
    keyStore.load(null)
    return keyStore.getKey("KEY_NAME", null) as SecretKey
}

private fun getCipher(): Cipher {
    return Cipher.getInstance(
        KeyProperties.KEY_ALGORITHM_AES + "/"
            + KeyProperties.BLOCK_MODE_CBC + "/"
            + KeyProperties.ENCRYPTION_PADDING_PKCS7)
}

fun initCipher(){
    val cipher = getCipher()
    val secretKey = getSecretKey()
    cipher.init(Cipher.ENCRYPT_MODE, secretKey)
}