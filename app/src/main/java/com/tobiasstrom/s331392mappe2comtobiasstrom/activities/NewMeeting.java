package com.tobiasstrom.s331392mappe2comtobiasstrom.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.tobiasstrom.s331392mappe2comtobiasstrom.R;
import com.tobiasstrom.s331392mappe2comtobiasstrom.data.DatabaseHandler;
import com.tobiasstrom.s331392mappe2comtobiasstrom.model.Contact;
import com.tobiasstrom.s331392mappe2comtobiasstrom.model.Meeting;
import com.tobiasstrom.s331392mappe2comtobiasstrom.ui.ContactsInMeetingRecyclerViewAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;



public class NewMeeting extends AppCompatActivity {

    private DatabaseHandler db;

    private static final String TAG = "NewMeeting";

    private Dialog myDialog;
    private TextView txtDateStart;
    private TextView txtTimeStart;
    private TextView txtDateEnd;
    private TextView txtTimeEnd;
    private EditText txtInputPlace;
    private EditText txtInputType;
    private Button btnAddParticipant;
    private Button btnSave;
    private ListView tvParticipant;
    private List<Contact> contactList;
    private List<Contact> listItem;
    private int id;
    private String newMeetingStart;
    private String newMeetingEnd;
    private String newMeetingType;
    private String newMeetingPlace;
    private ArrayList<Integer> selectedItems;
    private String[] participants;
    private boolean[] selected;
    List<Contact> contacts;


    View root;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
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
        Log.d(TAG, "onCreate: before bunde check");

        btnSave = (Button) findViewById(R.id.btnSaveMeeting);
        final Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            newMeetingStart = bundle.getString("meeting_start");
            newMeetingEnd = bundle.getString("meeting_end");
            newMeetingPlace = bundle.getString("meeting_place");
            newMeetingType = bundle.getString("meeting_type");
            id = Integer.parseInt(bundle.getString("meeting_id"));
            Log.d(TAG, "onCreate: id for meeting"+id);
            txtInputPlace.setText(newMeetingPlace);
            txtInputType.setText(newMeetingType);

        } else {
            //id = db.getMeetingCount()+1; //skapes en id som skal være det samme som sql vil lage
            Date date= new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:MM");
            String dateDate = dateFormat.format(calendar.getTime());
            txtDateStart.setText(dateDate);
            txtDateEnd.setText(dateDate);
            String time = timeFormat.format(calendar.getTime());
            Log.e(TAG, "onCreate: " + time );
            txtTimeStart.setText(time);
            calendar.add(Calendar.MINUTE, 30);
            time = timeFormat.format(calendar.getTime());
            txtTimeEnd.setText(time);
        }

        contacts = db.getAllContacts();
        participants = new String[contacts.size()];//alle kontakter (String[] er nødvendig for dialog)
        selected = new boolean[contacts.size()]; //array som inneholder inforamsjon om hvilken verdier burde være checked i dialogen

        if (id != 0) { //dersom det skal oppdateres en møte
            List<Integer> contactsInMeeting = db.getContatctIdInMeeting(id); //henter de som er i en møte allerede
            for (int i = 0; i < participants.length; i++) { //skaper array med navn og array med valgte elementer for dialogbox
                participants[i] = contacts.get(i).getFirstName() + " " + contacts.get(i).getLastName();
                int id = contacts.get(i).getContactId();
                selected[i] = contactsInMeeting.indexOf(id) != -1;
            }

        } else { //dersom det skal dannes en ny møte

            for (int i = 0; i < participants.length; i++) {
                participants[i] = contacts.get(i).getFirstName() + " " + contacts.get(i).getLastName();
            }
        }




        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Bundle bundle = getIntent().getExtras();
                if (bundle != null) {
                    //oppdatere eksiterende møte
                    updateMeeting(view);
                } else {
                    //create new instance
                    saveMeetingToDB(view);

                }
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
        btnAddParticipant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //showPopup();
                //skaper dialog ved hjelp av onCreateDialog metode
                Dialog dialog = onCreateDialog(savedInstanceState);
                dialog.show();
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
                Date date = new Date();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                String formatedTime = simpleDateFormat.format(calendar.getTime());
                txtTimeStart.setText(formatedTime);
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

        if (selectedItems != null) {
            int id = db.getLastMeeting().getMetingId(); //dette spår om hvilken id skal denne møten få i databasen, dette er sikkelig yikes men fungerer for nå
            for (Integer i : selectedItems) {//looper gjennom nye verdier
                db.addContactToMeeting(id, contacts.get(i).getContactId());
            }

        }

        finish(); //lukke aktiviteten etter at møte har blitt endret
    }

    private void updateMeeting(View v) {
        Meeting meeting = new Meeting();

        String newStartDateTime = txtDateStart.getText().toString() + " " + txtTimeStart.getText().toString();
        String newEndDateTime = txtDateEnd.getText().toString() + " " + txtTimeEnd.getText().toString();
        String newPlace = txtInputPlace.getText().toString();
        String newType = txtInputType.getText().toString();

        meeting.setMetingId(id);
        meeting.setMeeting_start(newStartDateTime);
        meeting.setMeeting_end(newEndDateTime);
        meeting.setMeeting_place(newPlace);
        meeting.setMeeting_type(newType);

        db.updateMeeting(meeting);

        if (selectedItems != null) {

            db.deleteContactsFromMeeting(id);//nullstille hvem som er knyttet til denne møte

            for (Integer i : selectedItems) { //loope gjennom nye verdier
                db.addContactToMeeting(id, contacts.get(i).getContactId());
            }
            for (int i = 0; i < selected.length; i++) { //loope gjennom verdier som er satt fra før
                if (selected[i]) {
                    db.addContactToMeeting(id, contacts.get(i).getContactId());
                }
            }
        }

        finish();

    }

    public void showPopup(){
        //Oppretter en dialogbox og setter verdien til den custom_pop_up boks
        myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.custon_pop_add_contact);
        //Henter ut informasjonen av det som er i popupen
        tvParticipant = (ListView) myDialog.findViewById(R.id.tvParticipant);
        //Oppretter en StatestikAdaper for å hvise listen på den måten jeg ønsker

        //Oppretter listen med en bestemt adapter

        contactList = new ArrayList<>();
        listItem = new ArrayList<>();
        contactList = db.getAllContacts();
        Log.e(TAG, "showPopup: " + contactList.size() );
        for (Contact c : contactList) {
            Contact contact = new Contact();
            contact.setFirstName(c.getFirstName());
            contact.setLastName(c.getLastName());
            contact.setContactId(c.getContactId());
            contact.setEmail(c.getEmail());
            contact.setPhoneNumber(c.getPhoneNumber());

            listItem.add(contact);
        }
        ContactsInMeetingRecyclerViewAdapter cinrva = new ContactsInMeetingRecyclerViewAdapter(this, R.layout.listview_row_contacts_in_meeting, listItem);

        tvParticipant.setAdapter(cinrva);


        //viser dialogboken.
        myDialog.show();
    }

    //noe som har blitt hentet fra android.com
    //https://developer.android.com/guide/topics/ui/dialogs#java
    //dette skaper en dialog med en multichoice list og lagrer valgte elementer til selectedItems arrayList<Integer>
    private Dialog onCreateDialog(Bundle savedInstanceState) {
        selectedItems = new ArrayList<Integer>();  // Where we track the selected items
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Set the dialog title
        builder.setTitle("Choose participants")
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setMultiChoiceItems(participants, selected,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                if (isChecked) {
                                    // If the user checked the item, add it to the selected items
                                    selectedItems.add(which);
                                } else if (selectedItems.contains(which)) {
                                    // Else, if the item is already in the array, remove it
                                    selectedItems.remove(Integer.valueOf(which));
                                }
                            }
                        })
                // Set the action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK, so save the selectedItems results somewhere
                        // or return them to the component that opened the dialog
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        return builder.create();
    }





}