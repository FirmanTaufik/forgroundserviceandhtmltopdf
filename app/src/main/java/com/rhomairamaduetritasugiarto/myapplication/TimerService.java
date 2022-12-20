package com.rhomairamaduetritasugiarto.myapplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class TimerService extends Service {
    public static final int timerRunningId = 1;
    public static final String CHANNEL_ID = "exampleServiceChannel";

    private Timer timer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
   //     startForeground(timerRunningId, timerRunningNotification("Timer starting..."));

        createNotificationChannel();
        super.onCreate();

    }

    int inike =1;

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // timer logic, ignored for brevity but it would be calculate the timestamp needed for method below
                inike++;

                startForeground(1, getNotification(String.valueOf(inike)));
            }
        }, 0, TimeUnit.MINUTES.toMillis(1));
        return START_REDELIVER_INTENT;
    }

    private Notification getNotification(String s) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Example Service")
                .setContentText(s)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .build();
        return notification;
    }


    private void updateNotification(Notification notification ) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(timerRunningId, notification);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Example Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}
