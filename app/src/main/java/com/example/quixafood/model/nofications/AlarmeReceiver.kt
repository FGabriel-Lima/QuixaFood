package com.example.quixafood.model.nofications

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.widget.Toast
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.quixafood.MainActivity
import com.example.quixafood.R

class AlarmReceiver : BroadcastReceiver() {
    @SuppressLint("MissingPermission")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onReceive(context: Context, intent: Intent) {
        Toast.makeText(context, "Seu pedido está pronto!",
            Toast.LENGTH_LONG).show()
        val channelId = "order_ready_channel"
        val channelName = "Pedido Pronto"
        val notificationId = 1

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notificações sobre o status do pedido"
            }

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.logo)

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setLargeIcon(bitmap)
            .setStyle(NotificationCompat.BigPictureStyle().bigPicture(bitmap).bigLargeIcon(bitmap))
            .setContentTitle("Pedido Pronto!")
            .setContentText("Seu pedido está pronto para ser retirado.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(notificationId, notification)
    }
}