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

    private ContactsViewModel contactsViewModel;
    private RecyclerView recyclerView;
    private MeetingsRecyclerViewAdapter recyclerViewAdapter;
    private ListView lvContacts;
    private List<Meeting> meetingList;
    private List<Meeting> meetingItem;
    private DatabaseHandler db;
    View root;

    private MeetingsViewModel meetingsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        meetingsViewModel =
                ViewModelProviders.of(this).get(MeetingsViewModel.class);

        db = new DatabaseHandler(container.getContext());

        if (db.getMeetingCount() <= 0 ){
            root = inflater.inflate(R.layout.fragment_no_meetings, container, false);
        }else{
            root = inflater.inflate(R.layout.fragment_meetings, container, false);

            recyclerView = (RecyclerView) root.findViewById(R.id.recyclerViewMeetingID);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));

            meetingList = new ArrayList<>();
            meetingItem = new ArrayList<>();
            meetingList = db.getAllMeetings();

            for(Meeting m : meetingList){
                Meeting meeting = new Meeting();
                meeting.setMetingId(m.getMetingId());
                meeting.setMeeting_start(m.getMeeting_start());
                meeting.setMeeting_end(m.getMeeting_end());
                meeting.setMeeting_type(m.getMeeting_type());
                meeting.setMeeting_place(m.getMeeting_place());

                meetingItem.add(meeting);
            }
            recyclerViewAdapter = new MeetingsRecyclerViewAdapter(container.getContext(), meetingItem);
            recyclerView.setAdapter(recyclerViewAdapter);
            recyclerViewAdapter.notifyDataSetChanged();


        }


        return root;
    }

    @Override
    public void onResume() {
        //oppdaterer meetinger inn i meeting fragmentet etter at en ny meeting har blitt oprettet
        /*List<Meeting> list = db.getAllMeetings();

        if (list.size() != meetingList.size()) { //hvis det er flere meetings inn i databasen enn i fragmentet
            meetingItem.add(list.get(list.size()-1)); //legg den siste meetingen til meeting listen på fragmentet
        }*/
        super.onResume();

    }
}