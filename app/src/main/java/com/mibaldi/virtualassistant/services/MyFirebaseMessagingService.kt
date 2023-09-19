package com.mibaldi.virtualassistant.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mibaldi.virtualassistant.MainActivity
import com.mibaldi.virtualassistant.R
import com.mibaldi.virtualassistant.data.local.database.notifications.Notificacion
import com.mibaldi.virtualassistant.data.local.database.notifications.VirtualAssistantDatabase
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import javax.inject.Inject

@AndroidEntryPoint
class MyFirebaseMessagingService :
    FirebaseMessagingService() {
    @Inject
    lateinit var db: VirtualAssistantDatabase

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: ${remoteMessage.from}")
        // Check if message contains a data payload.
        //showNotification(title, texto)
        createOnDB(remoteMessage)
        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
        }
    }

    fun createOnDB(remoteMessage: RemoteMessage) {
        Log.d("CREATEONDB", "Message data payload: ${remoteMessage}")

        /*if (remoteMessage.notification != null) {
            val title = remoteMessage.notification!!.title!!
            val body = remoteMessage.notification!!.body!!
            runBlocking(Dispatchers.IO) {
                val count = db.notificacionDao().count().first()
                db.notificacionDao().insert(
                    Notificacion(
                        count + 1L,
                        "",
                        "",
                        "",
                        0,
                        body,
                        0,
                        title
                    )
                )
            }

            showNotification(title, body)

        } else*/ if (remoteMessage.data.isNotEmpty()) {
            // Check if message contains a data payload.
            Log.d("CREATEONDB", "Message data payload: ${remoteMessage.data}")
            val params: Map<String, String> = remoteMessage.data
            val objectjson = JSONObject(params)
            objectjson?.let {
                val idnotificacion: String = objectjson.optString("idnotificacion", "0")
                val codigo: String = objectjson.optString("codigo", "")
                val path: String = objectjson.optString("path", "")
                val read: String = objectjson.optString("read", "0")
                val tipo: String = objectjson.optString("tipo", "0")
                val texto: String = objectjson.optString("texto", "")
                val title: String = objectjson.optString("title", "")
                val createat: String = objectjson.optString("createat", "")
                db.notificacionDao().insert(
                    Notificacion(
                        idnotificacion.toLong(),
                        codigo,
                        createat,
                        path,
                        read.toInt(),
                        texto,
                        tipo.toInt(),
                        title
                    )
                )
                showNotification(title, texto)
            }
        }


    }

    private fun showNotification(title: String?, body: String?) {
        createNotificationChannel()
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder =
            NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())


    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        val name = "TestChannel"
        val descriptionText = "TestDescription"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance)
        channel.description = descriptionText
        // Register the channel with the system
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    // [END receive_message]

    // [START on_new_token]
    /**
     * Called if the FCM registration token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the
     * FCM registration token is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        sendRegistrationToServer(token)
    }
    // [END on_new_token]

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private fun handleNow() {
        Log.d(TAG, "Short lived task is done.")
    }


    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM registration token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private fun sendRegistrationToServer(token: String?) {
        // TODO: Implement this method to send token to your app server.
        Log.d(TAG, "sendRegistrationTokenToServer($token)")
    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
        const val NOTIFICATION_ID = 101
        const val CHANNEL_ID = "channelID"
    }
}
