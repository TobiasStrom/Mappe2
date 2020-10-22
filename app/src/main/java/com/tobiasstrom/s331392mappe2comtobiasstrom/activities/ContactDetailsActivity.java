package com.tobiasstrom.s331392mappe2comtobiasstrom.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tobiasstrom.s331392mappe2comtobiasstrom.R;
import com.tobiasstrom.s331392mappe2comtobiasstrom.data.DatabaseHandler;
import com.tobiasstrom.s331392mappe2comtobiasstrom.model.Contact;
import com.tobiasstrom.s331392mappe2comtobiasstrom.util.Constants;

public class ContactDetailsActivity extends AppCompatActivity {
    private static final String TAG = "ContactDetailsActivity";

    //Oppretter veriden så de kan brukes senere i hele klassen.
    private DatabaseHandler db;
    private TextView txtDetails;
    private EditText txtFirstname;
    private EditText txtLastname;
    private EditText txtEmail;
    private EditText txtPhoneNumber;
    private Button btnUpdate;
    private ActionBar toolbar;
    private String firstName;
    private String lastName;
    private String email;
    private String phonenumber;
    private int id;
    private TextView txtErrorFirstname;
    private TextView txtErrorLastName;
    private TextView txtErrorMail;
    private TextView txtErrorPhonenumber;
    private String outMessege;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);

        //Oppretter en databasehandeler.
        db = new DatabaseHandler(this);

        //Setter inn en Actionbar og setter tittel
        toolbar = getSupportActionBar();
        toolbar.setTitle(getText(R.string.updateContact));

        //Gjør sånn at det er mulig å burke verdiene.
        txtFirstname = (EditText) findViewById(R.id.txtUpdateFirstname);
        txtLastname = (EditText) findViewById(R.id.txtUpdateLastname);
        txtEmail = (EditText) findViewById(R.id.txtUpdateEmail);
        txtPhoneNumber =(EditText) findViewById(R.id.txtUpdatePhoneNumber);
        txtErrorFirstname = findViewById(R.id.txtErrorFirstname);
        txtErrorLastName = findViewById(R.id.txtErrorLastName);
        txtErrorPhonenumber = findViewById(R.id.txtErrorPhonenumber);
        txtErrorMail = findViewById(R.id.txtErrorMail);
        txtErrorFirstname.setVisibility(View.INVISIBLE);
        txtErrorLastName.setVisibility(View.INVISIBLE);
        txtErrorMail.setVisibility(View.INVISIBLE);
        txtErrorPhonenumber.setVisibility(View.INVISIBLE);

        //Henter verdien som ble sende over fra ContactRecyklerViewAdapter
        Bundle bundle = getIntent().getExtras();

        if(bundle != null){
            //Hvis den er det så setter den verdien og fyller feltene.
            id = bundle.getInt("id");
            firstName = bundle.getString("firstname");
            lastName = bundle.getString("lastname");
            phonenumber = bundle.getString("phone");
            email = bundle.getString("email");

            txtFirstname.setText(firstName);
            txtLastname.setText(lastName);
            txtEmail.setText(email);
            txtPhoneNumber.setText(phonenumber);
            Log.e(TAG, "onOptionsItemSelected: " + id );

        }
    }
    //Skjekker im noen av verdiene er endret så man ikke tregner å kalde på db
    public boolean changed(){
        boolean value = false;
        if(!firstName.equals(txtFirstname.getText().toString())){
            return true;
        }
        if(!lastName.equals(txtLastname.getText().toString())){
            return true;
        }
        if(!phonenumber.equals(txtPhoneNumber.getText().toString())){
            return true;
        }
        if(!email.equals(txtEmail.getText().toString())){
            return true;
        }

        return value;
    }

    //Oppretter topp mennyen som skal brukes.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.meeting_menu, menu);
        return true;
    }

    //Skjekker hvilken icon som er trykket på.
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            //Hvis man trykker på slette knappen
            case R.id.icon_detete:
                //Oppretter en AlertDialog som spør om man er sikker på at man ånsker å slette
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ContactDetailsActivity.this);
                alertDialogBuilder.setTitle(R.string.delete_contact);
                alertDialogBuilder.setMessage(R.string.delete_contact_text);
                alertDialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Dersom brukeren ønsker seg å slette denne kontaktet
                        Constants.changeContact = true;
                        db.deleteContact(id);
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

                break;

            //Hvis man trykker på legg til.
            case R.id.icon_add:
                //Oppretter en kontakt med verdien som er i felten
                Contact contact = new Contact();
                contact.setContactId(id);
                contact.setFirstName(txtFirstname.getText().toString());
                contact.setLastName(txtLastname.getText().toString());
                contact.setEmail(txtEmail.getText().toString());
                contact.setPhoneNumber(txtPhoneNumber.getText().toString());
                //Trenger contexct og text for å lage tost hvis det ikke er noe endring.
                Context context = getApplicationContext();
                CharSequence text = getText(R.string.noChange);
                int duration = Toast.LENGTH_LONG;
                //Skjekker om det er noe feil i inputten
                if (wrongInput()) {
                    //Skjekker om det er noe endring.
                    if (changed()) {
                        text = getText(R.string.editContact) + " " + contact.getFirstName();
                        db.updateContact(contact);
                        Constants.changeContact = changed();
                    }
                    //Viser tost lukker vinduet.
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    finish();
                }
        }

        return super.onOptionsItemSelected(item);
    }
    //Skjekker inputt.
    public boolean wrongInput(){
        //Starter med en verdis er true som betyr at det ikke en noe endring.
        boolean inputValidation = true;
        //Teksen som skal skrives i info til brukere.
        outMessege = "";
        //Skjekker om noen av feltene er tomme og hvis de er det endrer den inputValidation til false og skriver ut en toast
        if(txtFirstname.getText().toString().isEmpty() || txtLastname.getText().toString().isEmpty() ||
                txtEmail.getText().toString().isEmpty() || txtPhoneNumber.getText().toString().isEmpty()){
            outMessege += getText(R.string.emtyField);
            inputValidation = false;
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, outMessege, duration);
            toast.show();

        }
        //Finner ut om fornavn har bare verdier som er godkjente og skriver ut melding hvis ikke.
        if (!txtFirstname.getText().toString().matches("[a-zøæåA-ZÆØÅ_]+")) {
            txtErrorFirstname.setVisibility(View.VISIBLE);
            txtErrorFirstname.setText(getText(R.string.wrongName));
            inputValidation = false;
        }
        else {
            txtErrorFirstname.setVisibility(View.INVISIBLE);
        }
        //Finner ut om etternavn har bare verdier som er godkjente og skriver ut melding hvis ikke.
        if (!txtLastname.getText().toString().matches("[a-zøæåA-ZÆØÅ_]+")) {
            txtErrorLastName.setText(getText(R.string.wrongNameLast));
            txtErrorLastName.setVisibility(View.VISIBLE);
            inputValidation = false;
        }
        else {
            txtErrorLastName.setVisibility(View.INVISIBLE);
        }
        //Finner ut om telefonummner har bare verdier som er godkjente og skriver ut melding hvis ikke.
        if (!txtPhoneNumber.getText().toString().matches("[0-9+]+")) {
            txtErrorPhonenumber.setText(getText(R.string.wrongPhonenumber));
            txtErrorPhonenumber.setVisibility(View.VISIBLE);
            inputValidation = false;
        }else {
            txtErrorPhonenumber.setVisibility(View.INVISIBLE);
        }
        //Finner ut om lengden på telefonmunneret passer med det som er bestemt.
        if (txtPhoneNumber.getText().toString().length() == 8 || txtPhoneNumber.getText().toString().length() == 11 || txtPhoneNumber.getText().toString().length() == 12) {
            txtErrorPhonenumber.setVisibility(View.INVISIBLE);
        }else{
            txtErrorPhonenumber.setText(getText(R.string.wrongPhonenumberLengt));
            txtErrorPhonenumber.setVisibility(View.VISIBLE);
            Log.e(TAG, "wrongInput: " + txtPhoneNumber.getText().toString().length() );
            inputValidation = false;
        }
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        //Finner ut om email har bare verdier som er godkjente og skriver ut melding hvis ikke.
        if (!txtEmail.getText().toString().matches(regex)) {
            txtErrorMail.setText(getText(R.string.wrongEmailFormat));
            txtErrorMail.setVisibility(View.VISIBLE);
            inputValidation = false;
        }
        else {
            txtErrorMail.setVisibility(View.INVISIBLE);
        }

        return inputValidation;
    }
}