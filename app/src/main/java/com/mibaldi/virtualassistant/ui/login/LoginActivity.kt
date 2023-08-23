package com.mibaldi.virtualassistant.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.biometric.BiometricPrompt.PromptInfo
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mibaldi.virtualassistant.ui.biometric.LevelAuthenticator
import com.mibaldi.virtualassistant.MyAppComposable
import com.mibaldi.virtualassistant.R
import com.mibaldi.virtualassistant.ui.biometric.authenticate
import com.mibaldi.virtualassistant.ui.biometric.setupPrompt
import com.mibaldi.virtualassistant.ui.common.MainAppBar
import com.mibaldi.virtualassistant.ui.common.UserViewModel
import com.mibaldi.virtualassistant.ui.common.goToBooking
import com.mibaldi.virtualassistant.ui.common.goToMain
import com.mibaldi.virtualassistant.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint


@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {


    private  var promptInfoStrong :PromptInfo?= null
    private var promptInfoWeak :PromptInfo?=null
    private var promptInfoWeakCredential :PromptInfo?= null
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyAppComposable {
                Scaffold(
                    topBar = { MainAppBar(stringResource(id = R.string.app_name)) }
                ) { _ ->
                    Auth(viewModel){
                        goToMain()
                    }
                }
            }
        }
        promptInfoStrong = setupPrompt(LevelAuthenticator.STRONG)
        promptInfoWeak = setupPrompt(LevelAuthenticator.WEAK)
        promptInfoWeakCredential = setupPrompt(LevelAuthenticator.WEAK_CREDENTIAL)

        viewModel.getCurrentUserStatus()
    }

    @Composable
    fun Auth(vm: LoginViewModel,userViewModel: UserViewModel = hiltViewModel(),navigation:()->Unit){
        val auth by vm.isLogged.collectAsState()
        if (auth) goToMain()
        Column(
            Modifier
                .background(if (auth) Color.Green else Color.Red)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(if (auth) "Estas autenticado" else "Necesitas autenticarte", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(9.dp))
            Button(onClick = {
                extracted(auth, vm,userViewModel,promptInfoStrong, navigation)
            }){
                Text(if (auth) "Cerrar" else "Autenticar Strong")
            }
            Button(onClick = {
                extracted(auth, vm,userViewModel,promptInfoWeak, navigation)
            }){
                Text(if (auth) "Cerrar" else "Autenticar Weak")
            }
            Button(onClick = {
                extracted(auth, vm,userViewModel,promptInfoWeakCredential, navigation)
            }){
                Text(if (auth) "Cerrar" else "Autenticar Weak Credential")
            }
        }
    }


    private fun extracted(
        auth: Boolean,
        vm: LoginViewModel,
        userViewModel: UserViewModel,
        promptInfo: PromptInfo?,
        navigation: () -> Unit
    ) {
        if (auth) {
            vm.setCurrentUserStatus(false)
            userViewModel.setUserLoggedOut()
        } else {
            authenticate(promptInfo) {
                userViewModel.setUserLoggedIn()
                vm.setCurrentUserStatus(it)
                navigation()
            }
        }
    }


}

