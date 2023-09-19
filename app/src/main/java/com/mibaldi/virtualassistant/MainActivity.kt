package com.mibaldi.virtualassistant

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback
import com.google.firebase.messaging.FirebaseMessaging
import com.mibaldi.virtualassistant.ui.VirtualAssistantApp
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    var rewardedInterstitialAd: RewardedInterstitialAd? = null
    lateinit var tokenid: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VirtualAssistantApp()
        }
        setupAds()
        if (Build.VERSION.SDK_INT > 32) {
            if (!shouldShowRequestPermissionRationale("112")) {
                getNotificationPermission();
            }
        }

    }

    fun setupAds() {
        MobileAds.initialize(this) { initializationStatus ->
            RewardedInterstitialAd.load(this,
                "ca-app-pub-3940256099942544/5354046379",
                AdRequest.Builder().build(),
                object : RewardedInterstitialAdLoadCallback() {
                    override fun onAdLoaded(ad: RewardedInterstitialAd) {
                        super.onAdLoaded(ad)
                        rewardedInterstitialAd = ad
                    }

                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        super.onAdFailedToLoad(adError)
                        rewardedInterstitialAd = null
                    }
                })
        }
    }

    fun getNotificationPermission() {
        try {
            if (Build.VERSION.SDK_INT > 32) {
                ActivityCompat.requestPermissions(
                    this, arrayOf<String>(Manifest.permission.POST_NOTIFICATIONS),
                    112
                )
            }
        } catch (e: Exception) {
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            112 -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    // allow
                } else {
                    //deny
                }
                return
            }
        }
    }


    /* // Declare the launcher at the top of your Activity/Fragment:
     private val requestPermissionLauncher = registerForActivityResult(
         ActivityResultContracts.RequestPermission(),
     ) { isGranted: Boolean ->
         if (isGranted) {
             // FCM SDK (and your app) can post notifications.
         } else {
             // TODO: Inform user that that your app will not show notifications.
         }
     }

     private fun askNotificationPermission() {
         // This is only necessary for API level >= 33 (TIRAMISU)
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
             if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                 PackageManager.PERMISSION_GRANTED
             ) {
                 // FCM SDK (and your app) can post notifications.
             } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                 // TODO: display an educational UI explaining to the user the features that will be enabled
                 //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                 //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                 //       If the user selects "No thanks," allow the user to continue without notifications.
             } else {
                 // Directly ask for the permission
                 requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
             }
         }
     }*/

    override fun onResume() {
        super.onResume()
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (it.isComplete) {
                tokenid = it.result.toString()
                Log.i("TOKEN", tokenid)
                //actualizarinformacion()
            }
        }
    }
}



