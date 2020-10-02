package com.tobiasstrom.s331392mappe2comtobiasstrom.ui;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.tobiasstrom.s331392mappe2comtobiasstrom.R;
import com.tobiasstrom.s331392mappe2comtobiasstrom.activities.NewMeeting;
import com.tobiasstrom.s331392mappe2comtobiasstrom.model.Contact;
import com.tobiasstrom.s331392mappe2comtobiasstrom.model.Meeting;

import java.util.List;

public class ContactsInMeetingRecyclerViewAdapter extends ArrayAdapter {
    private static final String TAG = "ContactsInMeetingRecycl";
    private final int layoutResource;
    private final LayoutInflater layoutInflater;
    private List<Contact> applications;

    public ContactsInMeetingRecyclerViewAdapter(@NonNull Context context, int resource, List<Contact> applications) {
        super(context, resource);
        this.layoutResource = resource;
        this.layoutInflater = LayoutInflater.from(context);
        this.applications = applications;
    }

    public int getCount() {
        return applications.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Contact contact = applications.get(position);
        ViewHolder viewHolder;
        if (convertView == null){
            convertView = layoutInflater.inflate(layoutResource, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvTxtLastNameMeeting.setText(contact.getFirstName());
        viewHolder.tvTxtFirstNameMeeting.setText(contact.getLastName());

        return convertView;
    }

    public class ViewHolder{
        final TextView tvTxtFirstNameMeeting;
        final TextView tvTxtLastNameMeeting;
        public int id;

        public ViewHolder(View v) {
            this.tvTxtFirstNameMeeting = v.findViewById(R.id.tvTxtFirstNameMeeting);
            this.tvTxtLastNameMeeting = v.findViewById(R.id.tvTxtLastNameMeeting);
            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Log.e(TAG, "onLongClick: Finker detter");
                    return false;
                }
            });
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //ListView lv = ();
                    Log.e(TAG, "onClick: " + view);
                    Log.e(TAG, "onClick: kort trykk");
                    //int position = ;
                }
            });

        }

    }
}
