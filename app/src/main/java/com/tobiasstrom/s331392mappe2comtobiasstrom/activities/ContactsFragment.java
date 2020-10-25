package com.tobiasstrom.s331392mappe2comtobiasstrom.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tobiasstrom.s331392mappe2comtobiasstrom.data.DatabaseHandler;
import com.tobiasstrom.s331392mappe2comtobiasstrom.model.Contact;
import com.tobiasstrom.s331392mappe2comtobiasstrom.R;
import com.tobiasstrom.s331392mappe2comtobiasstrom.ui.ContactsRecyclerViewAdapter;
import com.tobiasstrom.s331392mappe2comtobiasstrom.util.Constants;

import java.util.List;

public class ContactsFragment extends Fragment {

    private static final String TAG = "ContactsFragment";
    private ContactsRecyclerViewAdapter recyclerViewAdapter;
    private List<Contact> contactList;
    private DatabaseHandler db;
    View root;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        db = new DatabaseHandler(container.getContext());

        if (db.getContactCount() == 0 ) {//Hvis du ikke har noe kontakter så kan skrives det ut en toast
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(this.getContext(), R.string.noContact, duration);
            toast.setGravity(Gravity.CENTER, 0, -150);
            toast.show();
        }

        //Setter root til fragmet_contact
        root = inflater.inflate(R.layout.fragment_contacts, container, false);
        //Oprpetter recyklerview
        RecyclerView recyclerView = root.findViewById(R.id.recyclerViewID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        contactList = db.getAllContacts();

        recyclerViewAdapter = new ContactsRecyclerViewAdapter(container.getContext(), contactList);
        recyclerView.setAdapter(recyclerViewAdapter);

        return root;
    }
    //Oppdaterer recyclerview når den må oppdateres
    @Override
    public void onResume() {
        //her forventes det at contactList og recyclerViewAdapter skapes uansett hva i onCreateView
        if (Constants.changeContact) { //Gjør det bra
            Constants.changeContact = false;
            //populate arrayet på nytt dersom listen med kontakter som hentes er sorter
            contactList.clear();
            contactList.addAll(db.getAllContacts());
            recyclerViewAdapter.notifyDataSetChanged();
        }
        super.onResume();
    }

}