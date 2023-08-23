package com.mibaldi.virtualassistant.ui.common

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.app.ShareCompat
import com.mibaldi.virtualassistant.ui.bookings.BookingActivity
import com.mibaldi.virtualassistant.ui.chat.ChatActivity
import com.mibaldi.virtualassistant.ui.instagrams.InstagramsActivity
import com.mibaldi.virtualassistant.ui.login.LoginActivity
import com.mibaldi.virtualassistant.MainActivity



fun Context.goToMain(){
    startActivity(Intent(this, MainActivity::class.java))
}
fun Context.goToHome(){
    startActivity(Intent(this, LoginActivity::class.java))
}

fun Context.goToBooking(){
    startActivity(Intent(this, BookingActivity::class.java))
}

fun Context.goToChat(){
    startActivity(Intent(this, ChatActivity::class.java))
}
fun Context.goToInstagrams(){
    startActivity(Intent(this, InstagramsActivity::class.java))
}
fun Context.goToCalendar(){
    startActivity(Intent(this, InstagramsActivity::class.java))
}

fun Context.goToInstagram(perfilUsuario: String){
    val uri = Uri.parse("http://instagram.com/_u/$perfilUsuario")
    val intent = Intent(Intent.ACTION_VIEW, uri)
    intent.setPackage("com.instagram.android")
    try {
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        //No encontró la aplicación, abre la versión web.
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://instagram.com/$perfilUsuario")
            )
        )
    }
}
fun Context.whatsapp(message:String,phone:String=""){
    if (phone.isNotEmpty()){
        val phoneNumberWithCountryCode = phone
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(
                    String.format(
                        "https://api.whatsapp.com/send?phone=%s&text=%s",
                        phoneNumberWithCountryCode,
                        message
                    )
                )
            )
        )
    } else {
        val whatsappIntent = Intent(Intent.ACTION_SEND)
        whatsappIntent.type = "text/plain"
        whatsappIntent.setPackage("com.whatsapp")
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, message)
        try {
            startActivity(whatsappIntent)
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(applicationContext, "WhatsApp no esta instalado!", Toast.LENGTH_SHORT)
                .show()
        }
    }

}
fun Context.openPhone(numberPhone:String){
    val dialIntent = Intent(Intent.ACTION_DIAL)
    dialIntent.data = Uri.parse("tel:${numberPhone.replace(" ","")}")
    startActivity(dialIntent)
}
/*
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BookingNavHost(userViewModel: UserViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "booking"
    ) {
        composable("booking") {
            BookingScreen({
                Log.d("CLICK","elemento clicado $it")
            },{
                userViewModel.setUserLoggedOut()
            })
        }
        composable("calendar") {
            CalendarScreen({
                Log.d("CLICK","elemento clicado $it")
            }, navController) {
                userViewModel.setUserLoggedOut()
            }
        }
    }
}*/





fun share(context: Context,message: String){
    ShareCompat
        .IntentBuilder(context)
        .setType("text/plain")
        .setText(message)
        .intent
        .also(context::startActivity)
}