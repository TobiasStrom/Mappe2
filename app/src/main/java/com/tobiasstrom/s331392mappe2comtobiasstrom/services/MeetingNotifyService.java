package com.tobiasstrom.s331392mappe2comtobiasstrom.services;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import com.tobiasstrom.s331392mappe2comtobiasstrom.R;

import java.util.Calendar;

public class MeetingNotifyService extends Service {
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("servicen kjøres :>");

        java.util.Calendar calendar = Calendar.getInstance();
        Intent intent1 = new Intent(this, MeetingNotifyService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent1, 0);
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60 * 1000, pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder notificationCompat = new NotificationCompat.Builder(this);
        notificationCompat.setContentTitle("Hello");
        notificationCompat.setContentText("This is your notification");
        notificationCompat.setSmallIcon(R.mipmap.ic_launcher);
        Notification notification = notificationCompat.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(0,notification);

        return super.onStartCommand(intent,flags,startId);
    }
}
