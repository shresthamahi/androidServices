package com.example.androidservices;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NewService extends Service {

    public static final String CHANNEL_ID = "ForegroundServiceChannels";
    public static final int notificationIdA=1;
    public static final int notificationId=3;
    float lightLimit;

    int LightInformed=0;

    private SensorManager sensorManager;
    Sensor lightSensor;

    SensorEventListener lightListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float[] values = event.values;
            lightLimit=values[0];
            if (lightLimit<5)
            {
                if (LightInformed==0){
                addNotification();
                LightInformed ++; }

            }
            else
            {
                LightInformed=0;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();
        addNotificationGeneral();
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);


        sensorManager.registerListener(lightListener,lightSensor,SensorManager.SENSOR_DELAY_NORMAL);

        return START_STICKY;
    }

    private void addNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Light Notification")

                .setContentText("There is not enough light")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("There is not enough light"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(notificationId, builder.build());
    }

    private void addNotificationGeneral() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("General Notification")
                .setContentText("This is a regular notification")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Monitoring is started"))
                .setPriority(NotificationCompat.PRIORITY_LOW);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(notificationIdA, builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(lightListener); //this isn't causing trouble
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
       // throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }
}