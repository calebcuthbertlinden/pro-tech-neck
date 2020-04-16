package com.example.protechneck.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.example.protechneck.R;
import com.example.protechneck.ui.TechNeckActivity;

public class NotificationService extends Service {

    private static final String TAG_FOREGROUND_SERVICE = "FOREGROUND_SERVICE";
    public static final String ACTION_START_FOREGROUND_SERVICE = "ACTION_START_FOREGROUND_SERVICE";
    public static final String ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE";
    public static final String ACTION_OPEN = "ACTION_OPEN";

    public NotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG_FOREGROUND_SERVICE, "My notification service onCreate().");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        if (intent != null) {
            String action = intent.getAction();
            switch (action) {
                case ACTION_START_FOREGROUND_SERVICE:
                    if (!pref.getBoolean("TECH_NECK_RUNNING", false)) {
                        initiateNotification();
                    }
                    break;
                case ACTION_STOP_FOREGROUND_SERVICE:
                    stopForegroundService();
                    break;
                case ACTION_OPEN:
                    Intent techNeckIntent = new Intent(this, TechNeckActivity.class);
                    techNeckIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    techNeckIntent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                    startActivity(techNeckIntent);
                    stopForegroundService();
                    break;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void initiateNotification() {
        Log.d(TAG_FOREGROUND_SERVICE, "Start foreground service.");

        // Create notification default intent.
        Intent intent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        // Make notification show big text.
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle("ProTechNeck");
        bigTextStyle.bigText("We have noticed you are holding your device at a bad angle for your neck. Click open to address it.");
        // Set big text style.
        builder.setStyle(bigTextStyle);

        builder.setWhen(System.currentTimeMillis());
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setPriority(Notification.PRIORITY_MAX);
        builder.setFullScreenIntent(pendingIntent, true);

        builder.addAction(getOpenAction());
        builder.addAction(getCloseAction());

        // Build the notification.
        Notification notification = builder.build();
        startForeground(1, notification);
    }

    /**
     * This intent will open the Tech Neck application
     *
     * @return the open action for the notification
     */
    private NotificationCompat.Action getOpenAction() {
        Intent playIntent = new Intent(this, NotificationService.class);
        playIntent.setAction(ACTION_OPEN);
        PendingIntent pendingPlayIntent = PendingIntent.getService(this, 0, playIntent, 0);
        return new NotificationCompat.Action(android.R.drawable.ic_media_play, "Open", pendingPlayIntent);
    }

    /**
     * This intent will close the notification
     *
     * @return the close action for the notification
     */
    private NotificationCompat.Action getCloseAction() {
        Intent closeIntent = new Intent(this, NotificationService.class);
        closeIntent.setAction(ACTION_STOP_FOREGROUND_SERVICE);
        PendingIntent pendingPrevIntent = PendingIntent.getService(this, 0, closeIntent, 0);
        return new NotificationCompat.Action(android.R.drawable.ic_menu_close_clear_cancel, "Not now", pendingPrevIntent);
    }

    private void stopForegroundService() {
        Log.d(TAG_FOREGROUND_SERVICE, "Stop foreground service.");
        stopForeground(true);
        stopSelf();
    }
}
