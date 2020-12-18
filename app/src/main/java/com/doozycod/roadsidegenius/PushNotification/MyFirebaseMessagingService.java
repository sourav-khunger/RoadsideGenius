package com.doozycod.roadsidegenius.PushNotification;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.doozycod.roadsidegenius.Activities.Admin.Navigation.DashboardAdminActivity;
import com.doozycod.roadsidegenius.R;
import com.doozycod.roadsidegenius.SplashActivity;
import com.doozycod.roadsidegenius.Utils.SharedPreferenceMethod;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Date;
import java.util.Random;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FCM Service";
    private NotificationChannel mChannel;
    private NotificationManager notifManager;
    Bundle bundle = new Bundle();
    public static OnTokenReceive tokenInterface;
    SharedPreferenceMethod sharedPreferenceMethod;

    public static void setTokenInterface(OnTokenReceive onTokenReceive) {
        tokenInterface = onTokenReceive;
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getNotification() != null) {
            // Since the notification is received directly from
            // FCM, the title and the body can be fetched
            // directly as below.
            showNotification(
                    remoteMessage.getNotification().getTitle(),
                    remoteMessage.getNotification().getBody());
        }
        Log.e(TAG, "onMessageReceived: Data " + remoteMessage.getData());
//        Log.e(TAG, "onMessageReceived: " + remoteMessage.getNotification().getTitle());
        Log.e(TAG, "onMessageReceived: " + remoteMessage.getNotification().getBody());
//        if (!remoteMessage.getNotification().getTitle().equals("")) {
//            Intent notificationIntent = new Intent(getApplicationContext(), SplashActivity.class);
////                notificationIntent.putExtra("NotificationMessage", "I am from Notification");
//            notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//            notificationIntent.setAction(Intent.ACTION_MAIN);
//            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(notificationIntent);
//            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//            String channelId = "Default";
//            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
//                    .setSmallIcon(R.mipmap.ic_launcher)
//                    .setContentTitle(remoteMessage.getNotification().getTitle())
//                    .setContentText(remoteMessage.getNotification().getBody())
//                    .setAutoCancel(true)
//                    .setContentIntent(pendingIntent);
//            ;
//            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
//                manager.createNotificationChannel(channel);
//            }
//            manager.notify(0, builder.build());
//        }

    }

    @Override
    public void onNewToken(@NonNull String s) {
        Log.e(TAG, "onNewToken: " + s);
        sharedPreferenceMethod = new SharedPreferenceMethod(this);
        sharedPreferenceMethod.saveToken(s);
        super.onNewToken(s);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
    }

    // Method to display the notifications
    public void showNotification(String title,
                                 String message) {
        // Pass the intent to switch to the MainActivity
        Intent intent
                = new Intent(this, DashboardAdminActivity.class);
        // Assign channel ID
        String channel_id = "Default channel";
        int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        final int not_nu = generateRandom();
        // Here FLAG_ACTIVITY_CLEAR_TOP flag is set to clear
        // the activities present in the activity stack,
        // on the top of the Activity that is to be launched
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Pass the intent to PendingIntent to start the
        // next Activity
        PendingIntent pendingIntent
                = PendingIntent.getActivity(
                this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        // Create a Builder object using NotificationCompat
        // class. This will allow control over all the flags
        NotificationCompat.Builder builder
                = new NotificationCompat
                .Builder(getApplicationContext(),
                channel_id)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000, 1000,
                        1000, 1000})
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent);

        // A customized design for the notification can be
        // set only for Android versions 4.1 and above. Thus
        // condition for the same is checked here.
//        if (Build.VERSION.SDK_INT
//                >= Build.VERSION_CODES.JELLY_BEAN) {
//            builder = builder.setContent(
//                    getCustomDesign(title, message));
//        } // If Android Version is lower than Jelly Beans,
//        // customized layout cannot be used and thus the
//        // layout is set as follows
//        else {
        builder = builder.setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_launcher_foreground);
//        }
        // Create an object of NotificationManager class to
        // notify the
        // user of events that happen in the background.
        NotificationManager notificationManager
                = (NotificationManager) getSystemService(
                Context.NOTIFICATION_SERVICE);
        // Check if the Android Version is greater than Oreo
        if (Build.VERSION.SDK_INT
                >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel
                    = new NotificationChannel(
                    channel_id, "Default channel",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(
                    notificationChannel);
        }

        notificationManager.notify(not_nu, builder.build());
    }

    public int generateRandom() {
        Random random = new Random();
        return random.nextInt(9999 - 1000) + 1000;
    }
}
