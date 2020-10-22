package com.tobiasstrom.s331392mappe2comtobiasstrom.ui.meetings;

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

import com.tobiasstrom.s331392mappe2comtobiasstrom.R;
import com.tobiasstrom.s331392mappe2comtobiasstrom.data.DatabaseHandler;
import com.tobiasstrom.s331392mappe2comtobiasstrom.model.Meeting;
import com.tobiasstrom.s331392mappe2comtobiasstrom.ui.MeetingsRecyclerViewAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;



public class MeetingsFragment extends Fragment {
    private static final String TAG = "MeetingsFragment";
    private MeetingsRecyclerViewAdapter recyclerViewAdapter;
    private List<Meeting> meetingList;
    private DatabaseHandler db;
    View root;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        db = new DatabaseHandler(container.getContext());

        if (db.getMeetingCount() == 0 ){
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(this.getContext(), R.string.noMeeting, duration);
            toast.setGravity(Gravity.CENTER, 0, -150);
            toast.show();
        }

        //Setter root til fragmet_meeting
        root = inflater.inflate(R.layout.fragment_meetings, container, false);
        //Oprpetter recyklerview
        RecyclerView recyclerView = root.findViewById(R.id.recyclerViewMeetingID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));

        meetingList = db.getAllMeetings();

        recyclerViewAdapter = new MeetingsRecyclerViewAdapter(container.getContext(), meetingList);
        recyclerView.setAdapter(recyclerViewAdapter);

        //Skjker om teksten skal være rød og at møte er feridg.
        Date date;
        int teller = 0;
        final Calendar calendar = Calendar.getInstance();
        for (Meeting meeting : meetingList){
            try {
                date = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(meeting.getMeeting_start());
                if(date.before(calendar.getTime())){
                    teller++;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        recyclerView.getLayoutManager().scrollToPosition(teller);
        return root;
    }

    @Override
    public void onResume() {
        //Oppdaterer recyclerview
        meetingList.clear();
        meetingList.addAll(db.getAllMeetings());
        recyclerViewAdapter.notifyDataSetChanged();
        super.onResume();
    }
}