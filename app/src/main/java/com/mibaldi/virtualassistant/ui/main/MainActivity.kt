package com.mibaldi.virtualassistant.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import com.mibaldi.virtualassistant.ui.common.DatePickerUI
import com.mibaldi.virtualassistant.ui.common.UserViewModel
import com.mibaldi.virtualassistant.ui.common.goToBooking
import com.mibaldi.virtualassistant.ui.common.goToDetail
import com.mibaldi.virtualassistant.ui.common.goToHome
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalFoundationApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen(onNavigate = {
                if (it == 0){
                    goToBooking()
                }
            }) {
                userViewModel.setUserLoggedOut()
            }
        }

        viewModel.getEvents()
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



