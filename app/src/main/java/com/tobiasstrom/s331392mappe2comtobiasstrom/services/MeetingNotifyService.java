package com.tobiasstrom.s331392mappe2comtobiasstrom.services;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.tobiasstrom.s331392mappe2comtobiasstrom.R;
import com.tobiasstrom.s331392mappe2comtobiasstrom.data.DatabaseHandler;
import com.tobiasstrom.s331392mappe2comtobiasstrom.model.Meeting;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MeetingNotifyService extends Service {
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        java.util.Calendar calendar = Calendar.getInstance(); //gir nåvarende dato

        DatabaseHandler db = new DatabaseHandler(this);
        List<Meeting> meetings = db.getAllMeetings();
        //dd/MM/yyyy HH:MM - formatet på dato i meeting objekt

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        String dateToCompare = dateFormat.format(calendar.getTime());

        for (Meeting meeting : meetings) { //looper gjennom alle møter
            if (meeting.getMeeting_start().contains(dateToCompare)) {//dersom start dato stemmer med dagens
                String message = meeting.getMeeting_start() + " " + meeting.getMeeting_place();
                Notification notification = new Notification.Builder(this)
                        .setContentTitle("Meeting happening today")
                        .setContentText(message)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .build();

                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                notificationManager.notify(meeting.getMetingId(),notification);
            }
        }


        return super.onStartCommand(intent,flags,startId);
    }

}
