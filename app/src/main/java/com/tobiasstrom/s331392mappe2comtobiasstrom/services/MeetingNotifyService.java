package com.tobiasstrom.s331392mappe2comtobiasstrom.services;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import com.tobiasstrom.s331392mappe2comtobiasstrom.R;
import com.tobiasstrom.s331392mappe2comtobiasstrom.activities.MainActivity;
import com.tobiasstrom.s331392mappe2comtobiasstrom.data.DatabaseHandler;
import com.tobiasstrom.s331392mappe2comtobiasstrom.model.Contact;
import com.tobiasstrom.s331392mappe2comtobiasstrom.model.Meeting;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MeetingNotifyService extends Service {
    private static final String TAG = "MeetingNotifyService";

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        int sendNotificationHour = 7; //dette er tiden notifikasjonen skal komme opp i døynet

        //hente lagret verdier fra sharedpreferences eller definere default verdier dersom verdier fantes ikke i sharedpreferences
        boolean smsServiceOn = getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE).getBoolean("smsServiceIsOn", false); //burde gi false verdi som deafult verdi
        int smsServiceHour = getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE).getInt("smsSendTimeHour", 0);
        int smsServiceMinutes = getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE).getInt("smsSendTimeMinutes", 0);

        //Hente ut nåværende timen og minutter
        java.util.Calendar calendar = Calendar.getInstance(); //gir nåvarende dato
        SimpleDateFormat hour = new SimpleDateFormat("HH", Locale.US);
        SimpleDateFormat minutes = new SimpleDateFormat("mm", Locale.US);
        int currentHour = Integer.parseInt(hour.format(calendar.getTime())); // skal gi int med nåværende time
        int currentMinutes = Integer.parseInt(minutes.format(calendar.getTime())); // skal gi int med nåværende minutter

        // TODO: 22.10.2020 endre slik at den viser notifikasjon kun engang
        //sendNotificationHour = currentHour; //for debugging, slik vil notifikasjonen vises hver gang denne servisen refreshes, det vil si hver minutt


        if (currentHour == sendNotificationHour) { //it's time to show notification
            //send notification
            showNotification(calendar);
        }

        if (smsServiceOn) { // if services is on
            if (currentHour == smsServiceHour && currentMinutes == smsServiceMinutes) { //it's time to spam inbox
                //send sms
                sendMessages(calendar);
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private List<Meeting> getUpcomingMeetingsToday(Calendar calendar) {
        DatabaseHandler db = new DatabaseHandler(this);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        SimpleDateFormat timeFormat = new SimpleDateFormat("HHmm",Locale.US);

        String dateToCompare = dateFormat.format(calendar.getTime());
        int hourNumber = Integer.parseInt(timeFormat.format(calendar.getTime()));

        List<Meeting> allMeetings = db.getAllMeetings();
        List<Meeting> relevantMeetings = new ArrayList<>();

        for (Meeting meeting : allMeetings) {
            if (meeting.getMeeting_start().contains(dateToCompare)) { // sjekk også timen, dersom det er en møte som har vært, skal ikke den med.
                String meetingsHourNumber = meeting.getMeeting_start().substring(11,13) + meeting.getMeeting_start().substring(14,16); //parse timen og minutter fra møtens dato lagret i databasen
                if (hourNumber < Integer.parseInt(meetingsHourNumber)) { // sjekk om møten skal skje eller har skjedd, møte som har skjedd skal ikke tas inn
                    relevantMeetings.add(meeting);
                }
            }
        }
        return relevantMeetings;
    }

    private List<Contact> getContactsRelatedToUpcomingMeetings(Calendar calendar) {
        DatabaseHandler db = new DatabaseHandler(this);
        List<Meeting> relevantMeetings = getUpcomingMeetingsToday(calendar);
        List<Contact> relevantContacts = new ArrayList<>();
        for (Meeting meeting : relevantMeetings) {
            relevantContacts.addAll(db.getContactInMeeting(meeting.getMetingId()));
        }

        return relevantContacts;
    }

    private void showNotification(Calendar calendar) {
        List<Meeting> meetings = getUpcomingMeetingsToday(calendar);
        for (Meeting meeting : meetings) { //looper gjennom alle møter
            Intent i = new Intent(this, MainActivity.class);
            PendingIntent p = PendingIntent.getActivity(this, 0, i, 0);
            String message = meeting.getMeeting_start() + " " + meeting.getMeeting_place();
            Notification notification = new Notification.Builder(this)
                    .setContentTitle("Kommende møte!")
                    .setContentText(message)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(p)
                    .build();

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(meeting.getMetingId(), notification);
        }
    }

    private void sendMessages (Calendar calendar) {
        int myPermissionsRequestSendSms = ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);

        if (myPermissionsRequestSendSms == PackageManager.PERMISSION_GRANTED) { //dersom applikasjonen har permission til å sende sms
            List<Contact> contacts = getContactsRelatedToUpcomingMeetings(calendar);
            for (Contact contact : contacts) {
                String message = getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE).getString("smsMessage", "Du er invitert ti et møte");
                SmsManager smsManager = SmsManager.getDefault();
              
                //contact.getPhoneNumber();
                //smsManager.sendTextMessage(contact.getPhoneNumber(), null, message, null, null); // TODO: 22.10.2020 endre destinasjonAddresset til kontaktens tlf nummer
                Log.e(TAG, "Send meldig til " + contact.getFirstName() + " med telefonmr " + contact.getPhoneNumber() );
            }

        }

    }

}


