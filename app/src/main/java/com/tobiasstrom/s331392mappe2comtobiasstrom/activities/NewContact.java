package com.tobiasstrom.s331392mappe2comtobiasstrom.activities;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.content.Intent;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


import com.google.android.material.textfield.TextInputLayout;
import com.tobiasstrom.s331392mappe2comtobiasstrom.R;
import com.tobiasstrom.s331392mappe2comtobiasstrom.data.DatabaseHandler;
import com.tobiasstrom.s331392mappe2comtobiasstrom.model.Contact;
import com.tobiasstrom.s331392mappe2comtobiasstrom.R;
import com.tobiasstrom.s331392mappe2comtobiasstrom.ui.ContactsRecyclerViewAdapter;
import com.tobiasstrom.s331392mappe2comtobiasstrom.ui.contacts.ContactsFragment;


import java.util.List;

public class NewContact extends AppCompatActivity {
    private static final String TAG = "NewContact";

    private List<Contact> contactsList;
    private List<Contact> listContact;
    private DatabaseHandler db;
    private ActionBar toolbar;
    private TextInputLayout errFirstName;
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
        txtErrorFirstnameNewContact = findViewById(R.id.txtErrorFirstnameNewContact);
        txtErrorLastNameNewContact = findViewById(R.id.txtErrorLastNameNewContact);
        txtErrorPhonenumberNewContact = findViewById(R.id.txtErrorPhonenumberNewContact);
        txtErrorMailNewContact = findViewById(R.id.txtErrorMailNewContact);
        txtErrorFirstnameNewContact.setVisibility(View.INVISIBLE);
        txtErrorLastNameNewContact.setVisibility(View.INVISIBLE);
        txtErrorMailNewContact.setVisibility(View.INVISIBLE);
        txtErrorPhonenumberNewContact.setVisibility(View.INVISIBLE);


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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contact_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        outMessege = "";
        Log.e(TAG, "onOptionsItemSelected: trykket " );
        wrongInput();
        if(inputValidation){
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


    public void wrongInput(){
        inputValidation = true;
        outMessege = "";
        if(editFirstName.getText().toString().isEmpty() || editLastName.getText().toString().isEmpty() ||
                editEmail.getText().toString().isEmpty() || editPhoneNumber.getText().toString().isEmpty()){
            outMessege += getText(R.string.emtyField);
            inputValidation = false;
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, outMessege, duration);
            toast.show();

        }
        if (!editFirstName.getText().toString().matches("[a-zøæåA-ZÆØÅ_]+")) {
            txtErrorFirstnameNewContact.setVisibility(View.VISIBLE);
            txtErrorFirstnameNewContact.setText(getText(R.string.wrongName));
            inputValidation = false;
        }
        else {
            txtErrorFirstnameNewContact.setVisibility(View.INVISIBLE);
        }
        if (!editLastName.getText().toString().matches("[a-zøæåA-ZÆØÅ_]+")) {
            txtErrorLastNameNewContact.setText(getText(R.string.wrongNameLast));
            txtErrorLastNameNewContact.setVisibility(View.VISIBLE);
            inputValidation = false;
        }
        else {
            txtErrorLastNameNewContact.setVisibility(View.INVISIBLE);
        }
        if (!editPhoneNumber.getText().toString().matches("[0-9+]+")) {
            txtErrorPhonenumberNewContact.setText(getText(R.string.wrongPhonenumber));
            txtErrorPhonenumberNewContact.setVisibility(View.VISIBLE);
            System.out.println("Invalid number");
            inputValidation = false;
        }else {
            txtErrorPhonenumberNewContact.setVisibility(View.INVISIBLE);
        }
        if (editPhoneNumber.getText().toString().length() == 8 || editPhoneNumber.getText().toString().length() == 11 || editPhoneNumber.getText().toString().length() == 12) {
            txtErrorPhonenumberNewContact.setVisibility(View.INVISIBLE);
        }else{
            txtErrorPhonenumberNewContact.setText(getText(R.string.wrongPhonenumberLengt));
            txtErrorPhonenumberNewContact.setVisibility(View.VISIBLE);
            Log.e(TAG, "wrongInput: " + editPhoneNumber.getText().toString().length() );
            inputValidation = false;
        }
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        if (!editEmail.getText().toString().matches(regex)) {
            txtErrorMailNewContact.setText(getText(R.string.wrongEmailFormat));
            txtErrorMailNewContact.setVisibility(View.VISIBLE);

            inputValidation = false;
        }
        else {
            txtErrorMailNewContact.setVisibility(View.INVISIBLE);
        }
    }

}