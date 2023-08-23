package com.mibaldi.virtualassistant.ui.common

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.mibaldi.virtualassistant.R
import com.mibaldi.virtualassistant.domain.MyError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

fun <T> LifecycleOwner.launchAndCollect(
    flow: Flow<T>,
    state: Lifecycle.State = Lifecycle.State.STARTED,
    body: (T) -> Unit
) {
    lifecycleScope.launch {
        this@launchAndCollect.repeatOnLifecycle(state) {
            flow.collect(body)
        }
    }
}

fun ImageView.loadUrl(url: String) {
    Glide.with(context)
        .load(url)
        .error(R.drawable.no_image_available)
        .into(this)
}
fun Context.errorToString(error: MyError) = when (error) {
    MyError.Connectivity -> getString(R.string.connectivity_error)
    is MyError.Server -> getString(R.string.server_error) + error.code
    is MyError.Unknown -> getString(R.string.unknown_error) + error.message
}


// Function to show an explanation dialog
fun Context.showPermissionExplanationDialog(getAccountLauncher: ActivityResultLauncher<String>) {
    AlertDialog.Builder(this)
        .setTitle("Permission Required")
        .setMessage("This app needs to access your Google account (via Contacts).")
        .setPositiveButton("OK") { _, _ ->
            // Request the permission again
            getAccountLauncher.launch(android.Manifest.permission.GET_ACCOUNTS)
        }
        .setNegativeButton("Cancel") { _, _ ->
            // Handle the user's cancellation or take appropriate action
        }
        .show()
}
// Function to show a dialog guiding the user to app settings
fun Context.showAppSettingsDialog() {
    AlertDialog.Builder(this)
        .setTitle("Permission Required")
        .setMessage("To use this feature, you need to grant the camera permission. Please go to the app settings to enable it.")
        .setPositiveButton("Settings") { _, _ ->
            // Open the app settings for the user to manually grant the permission
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivity(intent)
        }
        .setNegativeButton("Cancel") { _, _ ->
            // Handle the user's cancellation or take appropriate action
        }
        .show()
}