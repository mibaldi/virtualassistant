package com.mibaldi.virtualassistant.ui.common

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.mibaldi.virtualassistant.ui.detail.DetailActivity
import com.mibaldi.virtualassistant.ui.main.MainActivity

fun Context.goToDetail(){
    startActivity(Intent(this, DetailActivity::class.java))
}
fun Context.goToMain(){
    startActivity(Intent(this, MainActivity::class.java))
}
fun Context.goToBack(){

}

