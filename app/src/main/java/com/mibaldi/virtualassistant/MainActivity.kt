package com.mibaldi.virtualassistant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import com.mibaldi.virtualassistant.ui.VirtualAssistantApp
import com.mibaldi.virtualassistant.ui.common.UserViewModel
import com.mibaldi.virtualassistant.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VirtualAssistantApp()
        }
    }
}



