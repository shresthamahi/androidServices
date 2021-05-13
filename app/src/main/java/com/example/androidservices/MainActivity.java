package com.example.androidservices;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    Button b1,b2,b3;
    public static final int notificationId=2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b1= (Button) findViewById(R.id.buttonStart);
        b2 = (Button) findViewById(R.id.buttonStop);
        b3= (Button)findViewById(R.id.buttonNotification);

        b1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                startService();
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService();
            }
        });


        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNotificationChannel();
                addNotification();
            }
        });

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



    private void startService() {
        //startService(new Intent(this,NewService.class));
        Intent serviceIntent = new Intent(this, NewService.class);
        //startForegroundService(new Intent(this,NewService.class));
        ContextCompat.startForegroundService(this,serviceIntent);
    }
    private void stopService() {
        stopService(new Intent(this,NewService.class));

    }

       private void addNotification()
       {
           NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                   .setSmallIcon(R.drawable.ic_launcher_foreground)
                   .setContentTitle("My notification")
                   .setContentText("Much longer text that cannot fit one line...")
                   .setStyle(new NotificationCompat.BigTextStyle()
                           .bigText("Much longer text that cannot fit one line..."))
                   .setPriority(NotificationCompat.PRIORITY_DEFAULT);

           NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
           notificationManager.notify(notificationId, builder.build());
       }
}