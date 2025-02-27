package com.example.multipageapp

import android.app.*
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

class MusicService : Service() {
    private var mediaPlayer: MediaPlayer? = null
    private var isPaused = false
    private val CHANNEL_ID = "music_channel"
    private val NOTIFICATION_ID = 1
    private lateinit var notificationManager: NotificationManager

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        notificationManager = getSystemService(NotificationManager::class.java)

        mediaPlayer = MediaPlayer.create(this, R.raw.elka)
        mediaPlayer?.isLooping = true

        startForeground(NOTIFICATION_ID, buildNotification("Music paused"))
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            "STOP" -> stopMusic()
            "PAUSE" -> pauseMusic()
            "PLAY" -> playMusic()
        }
        return START_STICKY
    }

    private fun playMusic() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.elka)
            mediaPlayer?.isLooping = true
        }
        if (!mediaPlayer!!.isPlaying) {
            mediaPlayer?.start()
            isPaused = false
            updateNotification("Music is playing...")
        }
    }

    private fun pauseMusic() {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
            isPaused = true
            updateNotification("Music paused")
        }
    }

    private fun stopMusic() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun buildNotification(contentText: String): Notification {
        val stopIntent = Intent(this, MusicService::class.java).apply { action = "STOP" }
        val pauseIntent = Intent(this, MusicService::class.java).apply { action = "PAUSE" }
        val playIntent = Intent(this, MusicService::class.java).apply { action = "PLAY" }

        val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0

        val stopPendingIntent = PendingIntent.getService(this, 1, stopIntent, flag)
        val pausePendingIntent = PendingIntent.getService(this, 2, pauseIntent, flag)
        val playPendingIntent = PendingIntent.getService(this, 3, playIntent, flag)

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Music player")
            .setContentText(contentText)
            .setSmallIcon(R.drawable.ic_music_note)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .addAction(R.drawable.ic_pause, "Pause", pausePendingIntent)
            .addAction(R.drawable.ic_play, "Play", playPendingIntent)
            .addAction(R.drawable.ic_stop, "Stop", stopPendingIntent)
            .setOngoing(true)
            .build()
    }

    private fun updateNotification(contentText: String) {
        val notification = buildNotification(contentText)
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Music Service",
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
