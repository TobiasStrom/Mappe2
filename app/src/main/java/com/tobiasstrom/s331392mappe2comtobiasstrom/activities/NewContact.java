package com.tobiasstrom.s331392mappe2comtobiasstrom.activities;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.material.textfield.TextInputLayout;
import com.tobiasstrom.s331392mappe2comtobiasstrom.R;
import com.tobiasstrom.s331392mappe2comtobiasstrom.data.DatabaseHandler;
import com.tobiasstrom.s331392mappe2comtobiasstrom.model.Contact;



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
        errFirstName = findViewById(R.id.errFirstName);

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
        outMessege = "";
        Log.e(TAG, "onOptionsItemSelected: trykket " );
        wrongInput();
        if(inputValidation){
            saveContactToDB();
            finish();
        }
        else {
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, outMessege, duration);
            toast.show();
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

    public void wrongInput(){
        inputValidation = true;
        outMessege = "";
        outMessege += getText(R.string.needChange);
        if(editFirstName.getText().toString().isEmpty() || editLastName.getText().toString().isEmpty() ||
                editEmail.getText().toString().isEmpty() || editPhoneNumber.getText().toString().isEmpty()){
            outMessege += "\n" + getText(R.string.emtyField);
            inputValidation = false;

        }
        if (!editFirstName.getText().toString().matches("[a-zøæåA-ZÆØÅ_]+")) {
            //errFirstName.setError(getText(R.string.wrongName));
            //errFirstName.setErrorEnabled(true);
            outMessege += "\n" + getText(R.string.wrongName);
            inputValidation = false;
        }
        else {
            //errFirstName.setErrorEnabled(true);
        }
        if (!editLastName.getText().toString().matches("[a-zøæåA-ZÆØÅ_]+")) {
            outMessege += "\n" + getText(R.string.wrongNameLast);
            editLastName.setTextColor(getResources().getColor(R.color.red));
            inputValidation = false;
        }
        else {
            editLastName.setTextColor(getResources().getColor(R.color.black));
        }
        if (!editPhoneNumber.getText().toString().matches("[0-9+]+")) {
            outMessege += "\n" + getText(R.string.wrongPhonenumber);
            System.out.println("Invalid number");
            inputValidation = false;
        }
        if (editPhoneNumber.getText().toString().length() == 8 || editPhoneNumber.getText().toString().length() == 11 || editPhoneNumber.getText().toString().length() == 12) {

        }else{
            outMessege += "\n" + getText(R.string.wrongPhonenumberLengt);
            Log.e(TAG, "wrongInput: " + editPhoneNumber.getText().toString().length() );
            inputValidation = false;
        }
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        if (!editEmail.getText().toString().matches(regex)) {
            outMessege += "\n" + getText(R.string.wrongEmailFormat);
            inputValidation = false;
        }


    }

}