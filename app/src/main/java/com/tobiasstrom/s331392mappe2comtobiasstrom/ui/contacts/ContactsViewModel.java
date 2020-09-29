package com.tobiasstrom.s331392mappe2comtobiasstrom.ui.contacts;

import android.widget.ListView;
import android.widget.TextView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tobiasstrom.s331392mappe2comtobiasstrom.ui.ContactsRecyclerViewAdapter;

public class ContactsViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private TextView txtContactsNo;
    private ListView lvContacts;
    private ContactsRecyclerViewAdapter adapter;

    public ContactsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
        //txtContactsNo.setText("Funker dette");
        //lvContacts.setAdapter(adapter = new ContactsListViewAdapter(this, R.layout.listview_row_contacts, DatabaseHandler.getAllContacts()));
    }

    public LiveData<String> getText() {
        return mText;
    }
}