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

import com.tobiasstrom.s331392mappe2comtobiasstrom.data.DatabaseHandler;
import com.tobiasstrom.s331392mappe2comtobiasstrom.model.Contact;
import com.tobiasstrom.s331392mappe2comtobiasstrom.R;
import com.tobiasstrom.s331392mappe2comtobiasstrom.ui.ContactsRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class ContactsFragment extends Fragment {
    private static final String TAG = "ContactsFragment";

    private ContactsViewModel contactsViewModel;
    private RecyclerView recyclerView;
    private ContactsRecyclerViewAdapter recyclerViewAdapter;
    private ListView lvContacts;
    private List<Contact> contactList;
    private List<Contact> listItem;
    private DatabaseHandler db;
    View root;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        db = new DatabaseHandler(activity);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        contactsViewModel =
                ViewModelProviders.of(this).get(ContactsViewModel.class);
        //root = inflater.inflate(R.layout.fragment_contacts, container, false);

        db = new DatabaseHandler(container.getContext());
        Log.e(TAG, "onCreateView: " + db.getGroceryCount() );
        if(db.getGroceryCount() <= 0 ){
            root = inflater.inflate(R.layout.fragment_no_contacts, container, false);
        }else {
            root = inflater.inflate(R.layout.fragment_contacts, container, false);

            recyclerView = (RecyclerView) root.findViewById(R.id.recyclerViewID);
            System.out.println();
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));

            contactList = new ArrayList<>();
            listItem = new ArrayList<>();
            contactList = db.getAllContacts();

            for (Contact c : contactList) {
                Contact contact = new Contact();
                contact.setFirstName(c.getFirstName());
                contact.setLastName(c.getLastName());
                contact.setContactId(c.getContactId());
                contact.setEmail(c.getEmail());
                contact.setPhoneNumber(c.getPhoneNumber());

                listItem.add(contact);
            }

            recyclerViewAdapter = new ContactsRecyclerViewAdapter(container.getContext(), listItem);
            recyclerView.setAdapter(recyclerViewAdapter);
            recyclerViewAdapter.notifyDataSetChanged();

        }

        return root;
    }


}