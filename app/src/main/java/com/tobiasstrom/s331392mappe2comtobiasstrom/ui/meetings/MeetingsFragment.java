package com.tobiasstrom.s331392mappe2comtobiasstrom.ui.meetings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tobiasstrom.s331392mappe2comtobiasstrom.R;
import com.tobiasstrom.s331392mappe2comtobiasstrom.data.DatabaseHandler;
import com.tobiasstrom.s331392mappe2comtobiasstrom.model.Contact;
import com.tobiasstrom.s331392mappe2comtobiasstrom.model.Meeting;
import com.tobiasstrom.s331392mappe2comtobiasstrom.ui.ContactsRecyclerViewAdapter;
import com.tobiasstrom.s331392mappe2comtobiasstrom.ui.MeetingsRecyclerViewAdapter;
import com.tobiasstrom.s331392mappe2comtobiasstrom.ui.contacts.ContactsViewModel;

import java.util.ArrayList;
import java.util.List;

public class MeetingsFragment extends Fragment {
    
    private MeetingsRecyclerViewAdapter recyclerViewAdapter;
    private List<Meeting> meetingList;
    private DatabaseHandler db;
    View root;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        db = new DatabaseHandler(container.getContext());

        /*if (db.getMeetingCount() == 0 ){
            root = inflater.inflate(R.layout.fragment_no_meetings, container, false);
        } else {
            root = inflater.inflate(R.layout.fragment_meetings, container, false);

            recyclerView = root.findViewById(R.id.recyclerViewMeetingID);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));

            meetingList = db.getAllMeetings();

            recyclerViewAdapter = new MeetingsRecyclerViewAdapter(container.getContext(), meetingList);
            recyclerView.setAdapter(recyclerViewAdapter);

        }*/

        //Dette gjør at rooten er altid attached slik at den viser møte listen.
        //Dersom den blir ikke attached i tilfelde hvor det er ingen møte, etter at den første møte er opprettet er det umulig å vise den (fordi listen er ikke attached)
        //etter at ny møte har blitt opprettet kjøres det kun onresume som ikke har tilgang til inflater.
        root = inflater.inflate(R.layout.fragment_meetings, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.recyclerViewMeetingID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));

        meetingList = db.getAllMeetings();

        recyclerViewAdapter = new MeetingsRecyclerViewAdapter(container.getContext(), meetingList);
        recyclerView.setAdapter(recyclerViewAdapter);


        return root;
    }

    @Override
    public void onResume() {

        //her forventes det at meetingList og recyclerViewAdapter skapes uansett hva i onCreateView
        if (meetingList.size() < db.getAllMeetings().size()) { //dersom det er flere møter inn i db enn det vises
            //meetingList.add(db.getAllMeetings().get(0)); //legg til den manglende møte (dette vil ikke sortere nye verdien)

            //populate arrayet på nytt dersom listen med møter som hentes er sorter
            meetingList.clear();
            meetingList.addAll(db.getAllMeetings());
            recyclerViewAdapter.notifyDataSetChanged();
        }

        super.onResume();

    }
}