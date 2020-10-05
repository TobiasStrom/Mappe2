package com.tobiasstrom.s331392mappe2comtobiasstrom.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.tobiasstrom.s331392mappe2comtobiasstrom.data.DatabaseHandler;
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
        public androidx.constraintlayout.widget.ConstraintLayout pane;

        public int id;

        public ViewHolder(@NonNull View v, Context ctx) {
            super(v);
            context = ctx;
            this.tvTxtMetingName = v.findViewById(R.id.tvTxtMetingType);
            this.tvTxtDate = v.findViewById(R.id.tvTxtDate);
            this.tvTxtMetingPlace = v.findViewById(R.id.tvTxtMetingPlace);
            this.pane = v.findViewById(R.id.meetingPane);
            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Log.e(TAG, "onLongClick: Finker detter");
                    return false;
                }
            });

            pane.setOnClickListener(new View.OnClickListener() {
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

            pane.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
                    alertDialogBuilder.setTitle("Delete Meeting");
                    alertDialogBuilder.setMessage("Do you want to delete this meeting?");
                    alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //dersom brukeren ønsker seg å slette denne møten
                            int position = getAdapterPosition();
                            Meeting meeting = meetingItem.get(position);
                            DatabaseHandler db = new DatabaseHandler(context);

                            db.deleteMeeting(meeting.getMetingId());
                            meetingItem.remove(position);
                            notifyItemRemoved(position);
                        }
                    });
                    alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //do nothing
                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                    return true;
                }
            });


        }

    }
}
