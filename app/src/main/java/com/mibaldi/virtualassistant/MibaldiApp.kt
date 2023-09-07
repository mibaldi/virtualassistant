package com.mibaldi.virtualassistant

import android.app.Application
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mibaldi.virtualassistant.ui.common.theme.VirtualAssistantTheme
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App:Application()


@Composable
fun MyAppComposable(content: @Composable () -> Unit) {

    VirtualAssistantTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            content()
        }
    }
}