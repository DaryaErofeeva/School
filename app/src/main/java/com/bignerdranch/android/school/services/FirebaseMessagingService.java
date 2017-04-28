package com.bignerdranch.android.school.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.bignerdranch.android.school.MainActivity;
import com.bignerdranch.android.school.R;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Ero on 21.04.2017.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService{
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        showNotification(remoteMessage.getData().get("message"));
    }

    private void showNotification(String message) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent=PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder=new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle("FCM Test")
                .setContentText(message)
                .setSmallIcon(R.drawable.accountcircle)
                .setContentIntent(pendingIntent);

        NotificationManager manager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }
}
