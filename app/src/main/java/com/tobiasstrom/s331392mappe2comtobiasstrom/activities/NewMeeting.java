package com.tobiasstrom.s331392mappe2comtobiasstrom.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.tobiasstrom.s331392mappe2comtobiasstrom.R;
import com.tobiasstrom.s331392mappe2comtobiasstrom.data.DatabaseHandler;
import com.tobiasstrom.s331392mappe2comtobiasstrom.model.Contact;
import com.tobiasstrom.s331392mappe2comtobiasstrom.model.Meeting;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class NewMeeting extends AppCompatActivity {

    private DatabaseHandler db;

    private static final String TAG = "NewMeeting";
    private TextView txtDateStart;
    private TextView txtTimeStart;
    private TextView txtDateEnd;
    private TextView txtTimeEnd;
    private EditText txtInputPlace;
    private EditText txtInputType;
    private Button btnAddParticipant;
    private Button btnSave;
    private RecyclerView tvParticipant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_meeting);

        db = new DatabaseHandler(this);

        txtDateStart = (TextView) findViewById(R.id.txtDateStart);
        txtTimeStart = (TextView) findViewById(R.id.txtTimeStart);
        txtDateEnd = (TextView) findViewById(R.id.txtDateEnd);
        txtTimeEnd = (TextView) findViewById(R.id.txtTimeEnd);
        txtInputPlace = (EditText) findViewById(R.id.txtInputPlace);
        txtInputType = (EditText) findViewById(R.id.txtIputType);
        btnAddParticipant = (Button) findViewById(R.id.btnAddParticipant);
        tvParticipant = (RecyclerView) findViewById(R.id.tvParticipant);
        btnSave = (Button) findViewById(R.id.btnSaveMeeting);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveMeetingToDB(view);
            }
        });

        txtDateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog(txtDateStart);
            }
        });

        txtDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog(txtDateEnd);
            }
        });
        txtTimeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimeDalog(txtTimeStart);
            }
        });
        txtTimeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimeDalog(txtTimeEnd);
            }
        });


    }

    private void showTimeDalog(final TextView txtTimeStarts) {
        final Calendar calendar = Calendar.getInstance();

        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                calendar.set(Calendar.HOUR_OF_DAY, i);
                calendar.set(Calendar.MINUTE, i1);
                Log.e(TAG, "onTimeSet: " + calendar.toString());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                String formatedTime = simpleDateFormat.format(calendar.getTime());
                txtTimeStarts.setText(formatedTime);
            }
        };
        new TimePickerDialog(NewMeeting.this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),true).show();
    }

    private void showDateDialog (final TextView txtDateStarts) {
        final Calendar calendar = Calendar.getInstance();
        Calendar calendarr = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        Log.e(TAG, "showDateDialog: " + simpleDateFormat.format(calendar.getTime()));
        Log.e(TAG, "showDateDialog: " + calendar.getTime() );
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(Calendar.YEAR, i);
                calendar.set(Calendar.MONTH, i1);
                calendar.set(Calendar.DAY_OF_MONTH, i2);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Log.e(TAG, "onDateSet: " + calendar.toString() );
                String formaterDate = simpleDateFormat.format(calendar.getTime());
                Log.e(TAG, "onDateSet: " + formaterDate);
                txtDateStarts.setText(formaterDate);
            }
        };
        new DatePickerDialog(NewMeeting.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void saveMeetingToDB(View v){
        Meeting meeting = new Meeting();

        String newStartDateTime = txtDateStart.getText().toString() + " " + txtTimeStart.getText().toString();
        String newEndDateTime = txtDateEnd.getText().toString() + " " + txtTimeEnd.getText().toString();
        String newPlace = txtInputPlace.getText().toString();
        String newType = txtInputType.getText().toString();

        meeting.setMeeting_start(newStartDateTime);
        meeting.setMeeting_end(newEndDateTime);
        meeting.setMeeting_place(newPlace);
        meeting.setMeeting_type(newType);

        db.addMeeting(meeting);
    }


}