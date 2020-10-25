package com.tobiasstrom.s331392mappe2comtobiasstrom.activities;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;


import com.tobiasstrom.s331392mappe2comtobiasstrom.R;
import com.tobiasstrom.s331392mappe2comtobiasstrom.data.DatabaseHandler;
import com.tobiasstrom.s331392mappe2comtobiasstrom.model.Contact;
import com.tobiasstrom.s331392mappe2comtobiasstrom.util.Constants;

public class NewContact extends AppCompatActivity {
    private static final String TAG = "NewContact";

    //Oppretter verdien somo skan burkes.
    private DatabaseHandler db;
    private ActionBar toolbar;
    private EditText editFirstName;
    private EditText editLastName;
    private EditText editEmail;
    private EditText editPhoneNumber;
    private String outMessege;
    private TextView txtErrorFirstnameNewContact;
    private TextView txtErrorLastNameNewContact;
    private TextView txtErrorMailNewContact;
    private TextView txtErrorPhonenumberNewContact;
    private boolean inputValidation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        outMessege = "";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact);
        //Oppretter en toolbar
        toolbar = getSupportActionBar();
        toolbar.setTitle(getText(R.string.newCntact));

        //Oppretter en databaseHandler
        db = new DatabaseHandler(this);

        //Henter verdien vi trenger.
        editFirstName = findViewById(R.id.editFirstName);
        editLastName = findViewById(R.id.editLastName);
        editEmail = findViewById(R.id.editEmail);
        editPhoneNumber = findViewById(R.id.editPhoneNumber);
        txtErrorFirstnameNewContact = findViewById(R.id.txtErrorFirstnameNewContact);
        txtErrorLastNameNewContact = findViewById(R.id.txtErrorLastNameNewContact);
        txtErrorPhonenumberNewContact = findViewById(R.id.txtErrorPhonenumberNewContact);
        txtErrorMailNewContact = findViewById(R.id.txtErrorMailNewContact);
        txtErrorFirstnameNewContact.setVisibility(View.INVISIBLE);
        txtErrorLastNameNewContact.setVisibility(View.INVISIBLE);
        txtErrorMailNewContact.setVisibility(View.INVISIBLE);
        txtErrorPhonenumberNewContact.setVisibility(View.INVISIBLE);
    }
    //Lager oppretter en kontakt med verdien som er sattet og lagrer dem i db
    private void saveContactToDB(){
        Constants.changeContact = true;
        Contact contact = new Contact();
        String newFistName = editFirstName.getText().toString();
        String newLastName = editLastName.getText().toString();
        String newEmail = editEmail.getText().toString();
        String newPhoneNumber = editPhoneNumber.getText().toString();

        contact.setFirstName(newFistName);
        contact.setLastName(newLastName);
        contact.setEmail(newEmail);
        contact.setPhoneNumber(newPhoneNumber);

        db.addContacts(contact);

    }
    //Oppretter toolbaren
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contact_menu, menu);
        return true;
    }
    //Når du trykker på lagre kontakt
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        outMessege = "";
        //Skjekker om det er riktig input
        if(wrongInput()){
            //lagrer i db
            saveContactToDB();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    //Skjekker input.
    public boolean wrongInput(){
        //Starter med en verdis er true som betyr at det ikke en noe endring.
        inputValidation = true;
        //Teksen som skal skrives i info til brukere.
        outMessege = "";
        //Skjekker om noen av feltene er tomme og hvis de er det endrer den inputValidation til false og skriver ut en toast
        if(editFirstName.getText().toString().isEmpty() || editLastName.getText().toString().isEmpty() ||
                editEmail.getText().toString().isEmpty() || editPhoneNumber.getText().toString().isEmpty()){
            outMessege += getText(R.string.emtyField);
            inputValidation = false;
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, outMessege, duration);
            toast.show();
        }
        //Finner ut om fornavn har bare verdier som er godkjente og skriver ut melding hvis ikke.
        if (!editFirstName.getText().toString().matches("[a-zøæåA-ZÆØÅ_]+")) {
            txtErrorFirstnameNewContact.setVisibility(View.VISIBLE);
            txtErrorFirstnameNewContact.setText(getText(R.string.wrongName));
            inputValidation = false;
        }
        else {
            txtErrorFirstnameNewContact.setVisibility(View.INVISIBLE);
        }
        //Finner ut om etternavn har bare verdier som er godkjente og skriver ut melding hvis ikke.
        if (!editLastName.getText().toString().matches("[a-zøæåA-ZÆØÅ_]+")) {
            txtErrorLastNameNewContact.setText(getText(R.string.wrongNameLast));
            txtErrorLastNameNewContact.setVisibility(View.VISIBLE);
            inputValidation = false;
        }
        else {
            txtErrorLastNameNewContact.setVisibility(View.INVISIBLE);
        }
        //Finner ut om telefonummner har bare verdier som er godkjente og skriver ut melding hvis ikke.
        if (!editPhoneNumber.getText().toString().matches("[0-9+]+")) {
            txtErrorPhonenumberNewContact.setText(getText(R.string.wrongPhonenumber));
            txtErrorPhonenumberNewContact.setVisibility(View.VISIBLE);
            System.out.println("Invalid number");
            inputValidation = false;
        }else {
            txtErrorPhonenumberNewContact.setVisibility(View.INVISIBLE);
        }
        //Finner ut om lengden på telefonmunneret passer med det som er bestemt.
        if (editPhoneNumber.getText().toString().length() == 8 || editPhoneNumber.getText().toString().length() == 11 || editPhoneNumber.getText().toString().length() == 12) {
            txtErrorPhonenumberNewContact.setVisibility(View.INVISIBLE);
        }else{
            txtErrorPhonenumberNewContact.setText(getText(R.string.wrongPhonenumberLengt));
            txtErrorPhonenumberNewContact.setVisibility(View.VISIBLE);
            Log.e(TAG, "wrongInput: " + editPhoneNumber.getText().toString().length() );
            inputValidation = false;
        }
        //Finner ut om email har bare verdier som er godkjente og skriver ut melding hvis ikke.
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        if (!editEmail.getText().toString().matches(regex)) {
            txtErrorMailNewContact.setText(getText(R.string.wrongEmailFormat));
            txtErrorMailNewContact.setVisibility(View.VISIBLE);

            inputValidation = false;
        }
        else {
            txtErrorMailNewContact.setVisibility(View.INVISIBLE);
        }
        return inputValidation;
    }


}