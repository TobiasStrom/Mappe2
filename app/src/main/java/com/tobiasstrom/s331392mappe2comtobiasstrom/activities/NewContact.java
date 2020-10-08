package com.tobiasstrom.s331392mappe2comtobiasstrom.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import androidx.appcompat.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toolbar;

import com.tobiasstrom.s331392mappe2comtobiasstrom.data.DatabaseHandler;
import com.tobiasstrom.s331392mappe2comtobiasstrom.model.Contact;
import com.tobiasstrom.s331392mappe2comtobiasstrom.R;

import java.util.List;

public class NewContact extends AppCompatActivity {
    private static final String TAG = "NewContact";

    private List<Contact> contactsList;
    private List<Contact> listContact;
    private DatabaseHandler db;
    private ActionBar toolbar;

    private EditText editFirstName;
    private EditText editLastName;
    private EditText editEmail;
    private EditText editPhoneNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact);
        /*
        toolbar = (Toolbar) findViewById(R.id.toolbarNewContact);
        toolbar.setTitle("hei");
        setActionBar(toolbar);


         */
        toolbar = getSupportActionBar();
        toolbar.setTitle(getText(R.string.newCntact));

        db = new DatabaseHandler(this);

        editFirstName = findViewById(R.id.editFirstName);
        editLastName = findViewById(R.id.editLastName);
        editEmail = findViewById(R.id.editEmail);
        editPhoneNumber = findViewById(R.id.editPhoneNumber);

    }

    private void saveContactToDB(){
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
        //her burde være noe som notifydatasetupdate, slik at listen i kontakter skulle oppdatere automatisk slik det skjer ved fjerning

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contact_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.e(TAG, "onOptionsItemSelected: trykket " );
        if(!editFirstName.getText().toString().isEmpty() && !editLastName.getText().toString().isEmpty() && !editEmail.getText().toString().isEmpty() && !editPhoneNumber.getText().toString().isEmpty()){
            saveContactToDB();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    /*
    private void restartActivity(){
        Intent i = new Intent(getActivity(), PreferanseActivity.class);
        startActivity(i);
        getActivity().finish();
    }

     */

}