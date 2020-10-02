package com.tobiasstrom.s331392mappe2comtobiasstrom.ui;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tobiasstrom.s331392mappe2comtobiasstrom.R;
import com.tobiasstrom.s331392mappe2comtobiasstrom.activities.NewMeeting;
import com.tobiasstrom.s331392mappe2comtobiasstrom.model.Meeting;

import java.util.List;

public class MeetingsRecyclerViewAdapter extends RecyclerView.Adapter<MeetingsRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "MeetingsRecyclerViewAda";
    private Context context;
    private LayoutInflater layoutInflater;
    private List<Meeting> meetingItem;

    public MeetingsRecyclerViewAdapter(Context context, List<Meeting> meetingItem) {
        this.context = context;
        this.meetingItem = meetingItem;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listview_row_meetings, parent, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Meeting meeting = meetingItem.get(position);
        holder.tvTxtMetingName.setText(meeting.getMeeting_type());
        holder.tvTxtDate.setText(meeting.getMeeting_start());
        holder.tvTxtMetingPlace.setText(meeting.getMeeting_place());
    }

    @Override
    public int getItemCount() {
        return meetingItem.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        final TextView tvTxtMetingName;
        final TextView tvTxtDate;
        final TextView tvTxtMetingPlace;


        public int id;

        public ViewHolder(@NonNull View v, Context ctx) {
            super(v);
            context = ctx;
            this.tvTxtMetingName = v.findViewById(R.id.tvTxtMetingType);
            this.tvTxtDate = v.findViewById(R.id.tvTxtDate);
            this.tvTxtMetingPlace = v.findViewById(R.id.tvTxtMetingPlace);
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
                    int position = getAdapterPosition();

                    Meeting meeting = meetingItem.get(position);
                    Intent intent = new Intent(context, NewMeeting.class);
                    Log.e(TAG, "onClick: boi" + meeting.getMetingId() );
                    intent.putExtra("meeting_id", String.valueOf(meeting.getMetingId()));
                    intent.putExtra("meeting_start", meeting.getMeeting_start());
                    intent.putExtra("meeting_end", meeting.getMeeting_end());
                    intent.putExtra("meeting_type", meeting.getMeeting_type());
                    intent.putExtra("meeting_place", meeting.getMeeting_place());
                    context.startActivity(intent);
                }
            });

        }

    }
}
