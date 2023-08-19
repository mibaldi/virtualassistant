package com.mibaldi.virtualassistant.ui.common

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.mibaldi.virtualassistant.ui.bookings.BookingActivity
import com.mibaldi.virtualassistant.ui.detail.DetailActivity
import com.mibaldi.virtualassistant.ui.login.LoginActivity
import com.mibaldi.virtualassistant.ui.main.MainActivity

fun Context.goToDetail(){
    startActivity(Intent(this, DetailActivity::class.java))
}
fun Context.goToMain(){
    startActivity(Intent(this, MainActivity::class.java))
}
fun Context.goToHome(){
    startActivity(Intent(this, LoginActivity::class.java))
}


fun Context.goToBooking(){
    startActivity(Intent(this, BookingActivity::class.java))
}

