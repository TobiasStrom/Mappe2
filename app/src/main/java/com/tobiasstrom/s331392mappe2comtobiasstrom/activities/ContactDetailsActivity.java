package com.tobiasstrom.s331392mappe2comtobiasstrom.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tobiasstrom.s331392mappe2comtobiasstrom.R;
import com.tobiasstrom.s331392mappe2comtobiasstrom.model.Contact;

public class ContactDetailsActivity extends AppCompatActivity {
    private static final String TAG = "ContactDetailsActivity";
    private TextView txtDetails;
    private EditText txtFirstname;
    private EditText txtLastname;
    private EditText txtEmail;
    private EditText txtPhoneNumber;
    private Button btnUpdate;
    String firstName;
    String lastName;
    String email;
    String phonenumber;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);

        txtDetails = (TextView) findViewById(R.id.txtDetails);
        txtFirstname = (EditText) findViewById(R.id.txtUpdateFirstname);
        txtLastname = (EditText) findViewById(R.id.txtUpdateLastname);
        txtEmail = (EditText) findViewById(R.id.txtUpdateEmail);
        txtPhoneNumber =(EditText) findViewById(R.id.txtUpdatePhoneNumber);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);

        Bundle bundle = getIntent().getExtras();

        if(bundle != null){
            firstName = bundle.getString("firstname");
            lastName = bundle.getString("lastname");
            phonenumber = bundle.getString("phone");
            email = bundle.getString("email");

            txtFirstname.setText(firstName);
            txtLastname.setText(lastName);
            txtDetails.setText(firstName);
            txtEmail.setText(email);
            txtPhoneNumber.setText(phonenumber);

        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Contact contact  = new Contact();
                contact.setFirstName(txtFirstname.getText().toString());
                contact.setLastName(txtLastname.getText().toString());
                contact.setEmail(txtEmail.getText().toString());
                contact.setPhoneNumber(txtPhoneNumber.getText().toString());

                //updateContact(contact);

            }
        });


    }
}