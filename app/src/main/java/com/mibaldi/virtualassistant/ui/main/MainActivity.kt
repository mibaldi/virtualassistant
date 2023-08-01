package com.mibaldi.virtualassistant.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.mibaldi.virtualassistant.MyAppComposable
import com.mibaldi.virtualassistant.ui.common.Greeting
import com.mibaldi.virtualassistant.ui.common.MainAppBar
import com.mibaldi.virtualassistant.ui.common.goToBack
import com.mibaldi.virtualassistant.ui.common.goToDetail
import com.mibaldi.virtualassistant.ui.common.launchAndCollect
import com.mibaldi.virtualassistant.ui.common.theme.VirtualAssistantTheme
import com.mibaldi.virtualassistant.ui.detail.DetailActivity
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalFoundationApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen(onNavigate = {goToDetail()}) {
                onBackPressedDispatcher.onBackPressed()
            }
        }
        viewModel.getEvents()

    }
}



