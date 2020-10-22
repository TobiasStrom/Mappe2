package com.tobiasstrom.s331392mappe2comtobiasstrom.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.tobiasstrom.s331392mappe2comtobiasstrom.R;
import com.tobiasstrom.s331392mappe2comtobiasstrom.data.DatabaseHandler;
import com.tobiasstrom.s331392mappe2comtobiasstrom.model.Contact;
import com.tobiasstrom.s331392mappe2comtobiasstrom.model.Meeting;
import com.tobiasstrom.s331392mappe2comtobiasstrom.ui.ContactsInMeetingRecyclerViewAdapter;
import com.tobiasstrom.s331392mappe2comtobiasstrom.util.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NewMeeting extends AppCompatActivity {
    private static final String TAG = "NewMeeting";

    //Oppretter verdien somo skan burkes.
    private DatabaseHandler db;
    private ActionBar toolbar;
    private TextView txtDateStart;
    private TextView txtTimeStart;
    private TextView txtDateEnd;
    private TextView txtTimeEnd;
    private EditText txtInputPlace;
    private EditText txtInputType;
    private ImageButton btnAddParticipant;
    private ListView tvParticipant;
    private int id = -1;
    private String newMeetingStart;
    private String newMeetingEnd;
    private String newMeetingType;
    private String newMeetingPlace;
    private String dateEndFormat;
    private String timeEndFormat;
    private String timeStartFormat;
    private String dateStartFormat;
    private Date dateStart;
    private Date dateEnd;
    private Date date;
    private boolean newMeeting;
    //lagrer metoder for å formatere tid og dato
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    private String[] participants;
    private int[] participantsID;
    private boolean[] selected;
    private List<Contact> contacts;
    private List<Contact> meetingContacts;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_meeting);
        meetingContacts = new ArrayList<>();//Oppretter en liste med kontakter
        newMeeting = false; //Setter veriden newMeeting til false
        db = new DatabaseHandler(this); //Oppretter en databasehandeler
        toolbar = getSupportActionBar(); //Henter toolbar

        //Henter de verdien vi trenger
        txtDateStart = (TextView) findViewById(R.id.txtDateStart);
        txtTimeStart = (TextView) findViewById(R.id.txtTimeStart);
        txtDateEnd = (TextView) findViewById(R.id.txtDateEnd);
        txtTimeEnd = (TextView) findViewById(R.id.txtTimeEnd);
        txtInputPlace = (EditText) findViewById(R.id.txtInputPlace);
        txtInputType = (EditText) findViewById(R.id.txtIputType);
        btnAddParticipant = (ImageButton) findViewById(R.id.btnAddParticipant);
        tvParticipant = (ListView) findViewById(R.id.tvParticipant);
        tvParticipant.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick (AdapterView < ? > adapter, View view,int position, long arg){
                     Dialog dialog = onCreateDialog();
                     dialog.show();
                 }
             }
        );


        //Henter ut informasjonen når du trykker på et ellement i Recilkelview
        final Bundle bundle = getIntent().getExtras();
        //Hvis det er en kontakt som finnes fra før.
        if(bundle != null){
            //Setter veriden til det de er.
            newMeetingStart = bundle.getString("meeting_start");
            newMeetingEnd = bundle.getString("meeting_end");
            newMeetingPlace = bundle.getString("meeting_place");
            newMeetingType = bundle.getString("meeting_type");
            id = Integer.parseInt(bundle.getString("meeting_id"));
            meetingContacts =  db.getContactInMeeting(id);
            txtInputPlace.setText(newMeetingPlace);
            txtInputType.setText(newMeetingType);
            toolbar.setTitle(getText(R.string.editMeeting));
            toolbar.setIcon(R.drawable.ic_baseline_delete__white_24);
            //Konverterer om string datoen til noe som kan brukes
            try {
                dateStart = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(newMeetingStart);
                dateStartFormat = dateFormat.format(dateStart.getTime());
                timeStartFormat = timeFormat.format(dateStart.getTime());
                txtTimeStart.setText(timeStartFormat);
                txtDateStart.setText(dateStartFormat);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //Konverterer om string datoen til noe som kan brukes
            try {
                dateEnd = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(newMeetingEnd);
                dateEndFormat = dateFormat.format(dateEnd.getTime());
                timeEndFormat = timeFormat.format(dateEnd.getTime());
                txtTimeEnd.setText(timeEndFormat);
                txtDateEnd.setText(dateEndFormat);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        //Hvis kontakten ikke finnes fra før
        else {
            meetingContacts = new ArrayList<>();
            newMeeting = true;
            date= new Date();

            Calendar calendar = Calendar.getInstance(); //Oppretter en calender objekt
            calendar.setTime(date);//Setter calender til akkurat tidspungt
            toolbar.setTitle(getText(R.string.newMeeting)); //Skriver ut teksten
            dateStart = calendar.getTime(); //Setter dateStart med dagens tid.
            String dateDate = dateFormat.format(calendar.getTime()); //Formaterer dagens dato
            txtDateStart.setText(dateDate); //setter start dato i appen
            txtDateEnd.setText(dateDate); // setter slutt dat i appen
            String time = timeFormat.format(calendar.getTime()); //formateres dagen tid
            txtTimeStart.setText(time); //setter start tid i appen
            calendar.add(Calendar.MINUTE, 30); //Legger til en halv time som default tid på møte
            dateEnd = calendar.getTime(); //setter slutttid med dagens tid
            time = timeFormat.format(calendar.getTime());//formaterer slutt tid
            txtTimeEnd.setText(time);//setter slutt tid i appen
        }

        contacts = db.getAllContacts(); //Henter ut alle kontaktene.
        participants = new String[contacts.size()];//alle kontakter (String[] er nødvendig for dialog)
        participantsID = new int[contacts.size()]; //alle kontaktidene trenger det for å skrive dem ut i listen
        selected = new boolean[contacts.size()]; //array som inneholder inforamsjon om hvilken verdier burde være checked i dialogen

        if (id != 0) { //dersom det skal oppdateres en møte
            List<Integer> contactsInMeeting = db.getContatcsIdInMeeting(id); //henter de som er i en møte allerede
            for (int i = 0; i < participants.length; i++) { //skaper array med navn og array med valgte elementer for dialogbox
                participants[i] = contacts.get(i).getFirstName() + " " + contacts.get(i).getLastName();

                int id = contacts.get(i).getContactId();
                participantsID[i] = id;
                selected[i] = contactsInMeeting.indexOf(id) != -1;
            }
        } else { //dersom det skal dannes en ny møte
            for (int i = 0; i < participants.length; i++) {
                int id = contacts.get(i).getContactId();
                participantsID[i] = id;
                participants[i] = contacts.get(i).getFirstName() + " " + contacts.get(i).getLastName();
            }
        }

        //Hvis du trykker på start datoen så vil den åpne et vindu hvor du kan velge dato

        txtDateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { showDateDialog(txtDateStart, true);
            }
        });
        //Hvis du trykker på slutt datoen så vil den åpne et vindu hvor du kan velge dato
        txtDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog(txtDateEnd, false);
            }
        });
        //Hvis du trykker på start tid så vil den åpne et vindu hvor du kan velge tid
        txtTimeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimeDalog(txtTimeStart, true);
            }
        });
        //Hvis du trykker på slutt tid så vil den åpne et vindu hvor du kan velge tid
        txtTimeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimeDalog(txtTimeEnd, false);
            }
        });
        //Åpner en dialog når du ønsker å legge til deltagere
        btnAddParticipant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showPopup();
                //skaper dialog ved hjelp av onCreateDialog metode
                Dialog dialog = onCreateDialog();
                dialog.show();

            }
        });
        //Oppretter en arratadapter med personer.
        ArrayAdapter<Contact> arrayAdapter = new ArrayAdapter<Contact>(this,android.R.layout.simple_list_item_1,meetingContacts);
        tvParticipant.setAdapter(arrayAdapter);

    }
    //Oprpetter en showTimeMetode som kan brukes flere steder.
    private void showTimeDalog(final TextView txtTimeStarts, final boolean start) {
        final Calendar calendar = Calendar.getInstance(); //Oppreter en calender
        //Oppretter en timepicker
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                calendar.set(Calendar.HOUR_OF_DAY, i); //Setter time i kalender
                calendar.set(Calendar.MINUTE, i1); //Setter minnut i kalender
                //Formatertid så den kan skrives ut
                String formatedTime = timeFormat.format(calendar.getTime());
                txtTimeStarts.setText(formatedTime);
                //Finner ut om det er start tid eller slutt tid og lagerer dem der den skal være
                if(start){
                    dateStart = calendar.getTime();
                    timeStartFormat = formatedTime;
                }
                else {
                    dateEnd = calendar.getTime();
                    timeEndFormat = formatedTime;
                }

            }
        };
        //Kan være tre ulike verdier i timepicker når du oppretter den.
        if(newMeeting){ //Hvis det er ett nytt møte så skal tiden starte med tiden nå.
            new TimePickerDialog(NewMeeting.this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),true).show();
        }else if(start){//Skal vises startiden i møte som er valgt.
            calendar.setTime(dateStart);
            new TimePickerDialog(NewMeeting.this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),true).show();
        }
        else {//Skalv vise slutttiden i møte som er satt.
            calendar.setTime(dateEnd);
            new TimePickerDialog(NewMeeting.this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),true).show();
        }
    }
    //Akuratt den samme som i tid men bare med dato.
    private void showDateDialog (final TextView txtDateStarts, final boolean start) {
        final Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(Calendar.YEAR, i);
                calendar.set(Calendar.MONTH, i1);
                calendar.set(Calendar.DAY_OF_MONTH, i2);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

                String formaterDate = simpleDateFormat.format(calendar.getTime());

                txtDateStarts.setText(formaterDate);

                if(start){
                    dateStart = calendar.getTime();
                    dateStartFormat = formaterDate;
                }
                else {
                    dateEnd = calendar.getTime();
                    dateEndFormat = formaterDate;
                }
            }
        };
        if(newMeeting){
            new DatePickerDialog(NewMeeting.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();

        }else if(start){
            calendar.setTime(dateStart);

            new DatePickerDialog(NewMeeting.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        }else {
            calendar.setTime(dateEnd);
            new DatePickerDialog(NewMeeting.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        }
    }
    //Metode som gjør det som må gjøres med dataen før den kan lagres.
    private void saveMeetingToDB(){
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

        if (Constants.selectedItems != null) {
            int id = db.getLastMeeting().getMetingId(); //dette spår om hvilken id skal denne møten få i databasen, dette er sikkelig yikes men fungerer for nå
            for (Integer i : Constants.selectedItems) {//looper gjennom nye verdier
                db.addContactToMeeting(id, contacts.get(i).getContactId());
            }
        }
        finish(); //lukke aktiviteten etter at møte har blitt endret
    }
    //Metode som gjør det den trenger før dataen skal uppdateres
    private void updateMeeting() {
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
        if (Constants.selectedItems != null) {
            db.deleteContactsFromMeeting(id);//nullstille hvem som er knyttet til denne møte
            for (Integer i : Constants.selectedItems) { //loope gjennom nye verdier
                db.addContactToMeeting(id, contacts.get(i).getContactId());
            }
            for (int i = 0; i < selected.length; i++) { //loope gjennom verdier som er satt fra før
                if (selected[i]) {
                    try {
                        db.addContactToMeeting(id, contacts.get(i).getContactId());
                    } catch (SQLiteConstraintException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        finish();
    }

    //noe som har blitt hentet fra android.com
    //https://developer.android.com/guide/topics/ui/dialogs#java
    //dette skaper en dialog med en multichoice list og lagrer valgte elementer til selectedItems arrayList<Integer>
    private Dialog onCreateDialog() {
        Constants.selectedItems = new ArrayList<Integer>();  // Where we track the selected items
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Set the dialog title
        builder.setTitle(R.string.chooseParticipants)
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setMultiChoiceItems(participants, selected,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                if (isChecked) {
                                    // If the user checked the item, add it to the selected items
                                    Constants.selectedItems.add(which);
                                    //meetingContacts.add(findContact(which));
                                } else if (Constants.selectedItems.contains(which)) {
                                    // Else, if the item is already in the array, remove it
                                    Constants.selectedItems.remove(Integer.valueOf(which));
                                }
                            }
                        })
                // Set the action buttons
                .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        meetingContacts.clear();
                        for (int i = 0; i < participants.length; i++){
                            if (selected[i]){
                                meetingContacts.add(findContact(participantsID[i]));
                            }
                        }
                        // User clicked OK, so save the selectedItems results somewhere
                        uptateView();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        return builder.create();
    }
    //Henter ut den toolbaren som er ønsket.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.meeting_menu, menu);
        return true;
    }
    //Oppfatere view når man trykker ok.
    public void uptateView(){
        ArrayAdapter<Contact> arrayAdapter = new ArrayAdapter<Contact>(this,android.R.layout.simple_list_item_1,meetingContacts);
        tvParticipant.setAdapter(arrayAdapter);
    }
    //Har to alterinativer i toolbaren.
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            //Vis man ønsker å slette
            case R.id.icon_detete:
                //Skjekker om det er opprettet er møte. og hvis ikke går det ikke ann å slette den
                if(id < 0){
                    Context context = getApplicationContext();
                    CharSequence text = getText(R.string.notMeeting);
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                } else { //Hvis møte ikke eksisterer fra før.
                    //vis dialog om brukeren vil virkelig slette denne kontakten
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(NewMeeting.this);
                    alertDialogBuilder.setTitle(R.string.delete_meeting);
                    alertDialogBuilder.setMessage(R.string.delete_meeting_text);
                    alertDialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Dersom brukeren ønsker seg å slette denne kontaktet
                            db.deleteMeeting(id);
                            finish();
                        }
                    });
                    alertDialogBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //dersom brukeren ønsker seg ikke å slette denne kontakten
                            //do nothing
                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
                break;
            case R.id.icon_add: //Hvis det skal lagres ett møte
                //Skjekker om feltene er tomme. Ingen imputvalidering fordi type og sted kan bestå av alle muligge kombinasjoen av bokstaver og symboler
                if (txtInputType.getText().toString().isEmpty() || txtInputPlace.getText().toString().isEmpty()) {
                    int duration = Toast.LENGTH_LONG;
                    Context context = getApplicationContext();
                    Toast toast = Toast.makeText(context, R.string.meetingEmty, duration);
                    toast.show(); //Skrive ut en tost hvis det er tomme felter
                } else {//Hvis feltene har noe i seg
                    if (dateStart.before(dateEnd)) {//Skjekker om slutt tid er før start tid.
                        txtTimeEnd.setTextColor(getResources().getColor(R.color.black));
                        txtDateEnd.setTextColor(getResources().getColor(R.color.black));
                        if (!newMeeting) {//Hvis det ikke er et nytt møte så bli møte oppdatert.
                            //oppdatere eksiterende møte
                            updateMeeting();
                        } else {//Hvis det er et nytt møte så blir det opprettet.
                            saveMeetingToDB();
                        }
                    } else {//Setter teksten rød og skriver ut en toast hvis datoen ikke stemmer
                        txtTimeEnd.setTextColor(getResources().getColor(R.color.red));
                        txtDateEnd.setTextColor(getResources().getColor(R.color.red));
                        int duration = Toast.LENGTH_LONG;
                        Context context = getApplicationContext();
                        Toast toast = Toast.makeText(context, R.string.wrong_date, duration);
                        toast.show();
                    }
                    break;
                }
        }
        return super.onOptionsItemSelected(item);
    }
    //Henter ut en kontakt utifra id.
    public Contact findContact(int id){
        Contact contact;
        contact = db.getAContact(id);

        return contact;
    }

}