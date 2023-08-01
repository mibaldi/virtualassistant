package com.mibaldi.virtualassistant.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
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
import com.mibaldi.virtualassistant.ui.biometric.LevelAuthenticator
import com.mibaldi.virtualassistant.MyAppComposable
import com.mibaldi.virtualassistant.R
import com.mibaldi.virtualassistant.ui.biometric.authenticate
import com.mibaldi.virtualassistant.ui.biometric.setupPrompt
import com.mibaldi.virtualassistant.ui.common.MainAppBar
import com.mibaldi.virtualassistant.ui.common.goToMain
import dagger.hilt.android.AndroidEntryPoint


@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {


    private  var promptInfoStrong :PromptInfo?= null
    private var promptInfoWeak :PromptInfo?=null
    private var promptInfoWeakCredential :PromptInfo?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyAppComposable {
                Scaffold(
                    topBar = { MainAppBar(stringResource(id = R.string.app_name)) }
                ) { _ ->
                    Auth{
                        goToMain()
                    }
                }
            }
        }
        promptInfoStrong = setupPrompt(LevelAuthenticator.STRONG)
        promptInfoWeak = setupPrompt(LevelAuthenticator.WEAK)
        promptInfoWeakCredential = setupPrompt(LevelAuthenticator.WEAK_CREDENTIAL)
    }

    @Composable
    fun Auth(navigation:()->Unit){
        var auth by remember{ mutableStateOf(false) }
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
                if (auth){
                    auth = false
                }else{
                    authenticate(promptInfoStrong) {
                        auth = it
                        navigation()
                    }
                }
            }){
                Text(if (auth) "Cerrar" else "Autenticar Strong")
            }
            Button(onClick = {
                if (auth){
                    auth = false
                }else{
                    authenticate(promptInfoWeak) {
                        auth = it
                        navigation()
                    }
                }
            }){
                Text(if (auth) "Cerrar" else "Autenticar Weak")
            }
            Button(onClick = {
                if (auth){
                    auth = false
                }else{
                    authenticate(promptInfoWeakCredential) {
                        auth = it
                        navigation()
                    }
                }
            }){
                Text(if (auth) "Cerrar" else "Autenticar Weak Credential")
            }
        }
    }


}

