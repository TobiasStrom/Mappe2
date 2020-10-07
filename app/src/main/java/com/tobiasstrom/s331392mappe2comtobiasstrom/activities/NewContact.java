package com.tobiasstrom.s331392mappe2comtobiasstrom.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tobiasstrom.s331392mappe2comtobiasstrom.data.DatabaseHandler;
import com.tobiasstrom.s331392mappe2comtobiasstrom.model.Contact;
import com.tobiasstrom.s331392mappe2comtobiasstrom.R;

import java.util.List;

public class NewContact extends AppCompatActivity {
    private Button btnSave;
    private List<Contact> contactsList;
    private List<Contact> listContact;
    private DatabaseHandler db;

    private EditText editFirstName;
    private EditText editLastName;
    private EditText editEmail;
    private EditText editPhoneNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact);
        getSupportActionBar().hide();

        db = new DatabaseHandler(this);

        editFirstName = findViewById(R.id.editFirstName);
        editLastName = findViewById(R.id.editLastName);
        editEmail = findViewById(R.id.editEmail);
        editPhoneNumber = findViewById(R.id.editPhoneNumber);

        btnSave = findViewById(R.id.btnUpdate);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!editFirstName.getText().toString().isEmpty() && !editLastName.getText().toString().isEmpty() && !editEmail.getText().toString().isEmpty() && !editPhoneNumber.getText().toString().isEmpty()){
                    saveContactToDB(view);
                    finish();
                }
            }
        });
    }

    private void saveContactToDB(View v){
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
    /*
    private void restartActivity(){
        Intent i = new Intent(getActivity(), PreferanseActivity.class);
        startActivity(i);
        getActivity().finish();
    }

     */

}