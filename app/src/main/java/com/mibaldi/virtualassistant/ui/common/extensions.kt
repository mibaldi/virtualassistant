package com.mibaldi.virtualassistant.ui.common

import android.content.Context
import android.widget.ImageView
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