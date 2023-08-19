package com.mibaldi.virtualassistant.ui.detail

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import com.mibaldi.virtualassistant.ui.common.UserViewModel
import com.mibaldi.virtualassistant.ui.common.goToHome
import com.mibaldi.virtualassistant.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint


@OptIn(ExperimentalFoundationApi::class)
@AndroidEntryPoint
class DetailActivity : ComponentActivity() {
    private val vm : MainViewModel by viewModels()
    private val userViewModel : UserViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DetailScreen(onBackPressed =  {
                userViewModel.setUserLoggedOut()
            }){
                Toast.makeText(this,it,Toast.LENGTH_SHORT).show()
            }
        }
        vm.getName()
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