package com.tobiasstrom.s331392mappe2comtobiasstrom.ui.contacts;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tobiasstrom.s331392mappe2comtobiasstrom.activities.NewContact;
import com.tobiasstrom.s331392mappe2comtobiasstrom.data.DatabaseHandler;
import com.tobiasstrom.s331392mappe2comtobiasstrom.model.Contact;
import com.tobiasstrom.s331392mappe2comtobiasstrom.R;
import com.tobiasstrom.s331392mappe2comtobiasstrom.ui.ContactsRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ContactsFragment extends Fragment {

    private static final String TAG = "ContactsFragment";
    private ContactsRecyclerViewAdapter recyclerViewAdapter;
    private List<Contact> contactList;
    private DatabaseHandler db;
    View root;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        db = new DatabaseHandler(container.getContext());

        /*if (db.getContactCount() == 0 ) {
            root = inflater.inflate(R.layout.fragment_no_contacts, container, false);

        } else {
            root = inflater.inflate(R.layout.fragment_contacts, container, false);

            RecyclerView recyclerView = root.findViewById(R.id.recyclerViewID);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));

            contactList = db.getAllContacts();

            recyclerViewAdapter = new ContactsRecyclerViewAdapter(container.getContext(), contactList);
            recyclerView.setAdapter(recyclerViewAdapter);
        }*/

        //Dette gjør at rooten er altid attached slik at den viser kontakt listen.
        //Dersom den blir ikke attached i tilfelde hvor det er ingen kontakt, etter at den første kontakt er opprettet er det umulig å vise den (fordi listen er ikke attached)
        //etter at ny kontakt har blitt opprettet kjøres det kun onresume som ikke har tilgang til inflater.
        root = inflater.inflate(R.layout.fragment_contacts, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.recyclerViewID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));

        contactList = db.getAllContacts();

        recyclerViewAdapter = new ContactsRecyclerViewAdapter(container.getContext(), contactList);
        recyclerView.setAdapter(recyclerViewAdapter);

        return root;
    }

    @Override
    public void onResume() {

        //her forventes det at contactList og recyclerViewAdapter skapes uansett hva i onCreateView
        if (contactList.size() < db.getAllContacts().size()) { //dersom det er flere kontakter inn i db enn det vises
            //contactList.add(db.getAllContacts().get(0)); //legg til den manglende kontakt (dette vil ikke sortere nye verdien)

            //populate arrayet på nytt dersom listen med kontakter som hentes er sorter
            contactList.clear();
            contactList.addAll(db.getAllContacts());
            recyclerViewAdapter.notifyDataSetChanged();
        }

        super.onResume();

    }

}