package com.mibaldi.virtualassistant.ui.instagrams

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import com.mibaldi.virtualassistant.ui.common.UserViewModel
import com.mibaldi.virtualassistant.ui.common.goToHome
import com.mibaldi.virtualassistant.ui.common.goToInstagram
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalFoundationApi::class)
@AndroidEntryPoint
class InstagramsActivity : ComponentActivity() {
    private val viewModel: InstagramsViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InstagramsScreen(onNavigate = {
                goToInstagram(it)
            }) {
                userViewModel.setUserLoggedOut()
            }
        }
        userViewModel.isLoggedIn.observe(this){
                isLoggedIn ->
            if (!isLoggedIn) {
                // User is logged out, perform necessary actions
                goToHome()
                finish()
            }
        }

    }
}