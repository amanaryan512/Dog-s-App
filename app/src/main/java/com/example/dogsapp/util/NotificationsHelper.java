package com.example.dogsapp.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.dogsapp.R;
import com.example.dogsapp.view.MainActivity;

import java.security.PublicKey;

public class NotificationsHelper {
    private static final String CHANNEL_ID="Dogs channel id";
    private static final int NOTIFICATION_ID=123;

    private Context context;
    private static NotificationsHelper instance;

    private NotificationsHelper(Context context){
        this.context=context;
    }
    public static NotificationsHelper getInstance(Context context){
        if(instance==null){
            instance=new NotificationsHelper(context);

        }
        return instance;
    }
    public void createNotification(){
        createNotificationChannel();
        Intent intent=new Intent(context, MainActivity.class);  // when user clicks on notifiaction mainAcivity will show.
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent=PendingIntent.getActivity(context,0,intent,0);
        Bitmap icon= BitmapFactory.decodeResource(context.getResources(), R.drawable.dog);

        Notification notification=new NotificationCompat.Builder(context,CHANNEL_ID)
                .setSmallIcon(R.drawable.dog_icon)
                .setLargeIcon(icon)
                .setContentTitle("Dog retrived")
                .setContentText("This is a information to let you know that dogs informat00ion is retrived")
                .setStyle(new NotificationCompat.BigPictureStyle()
                .bigPicture(icon)
                .bigLargeIcon(null)
                )
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();
        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID,notification);


    }
    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String name=CHANNEL_ID;
            String description ="dog retreived notification channel";
            int importance= NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel=new NotificationChannel(CHANNEL_ID,name,importance);
            NotificationManager notificationManager= (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
