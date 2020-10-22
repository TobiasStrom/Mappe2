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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
//Trenger denne klassen får å kunne legge inn den informsjonen som vi trenger i recyclerview
//På den måten jeg ønsker
public class MeetingsRecyclerViewAdapter extends RecyclerView.Adapter<MeetingsRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "MeetingsRecyclerViewAda";
    private Context context;
    private List<Meeting> meetingItem;
    private Meeting meeting;
    //Konstruktør
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
    //Setter teksten ut i viewholderen
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Date date;
        final Calendar calendar = Calendar.getInstance();

        Meeting meeting = meetingItem.get(position);
        try {
            date = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(meeting.getMeeting_start());

            if(date.before(calendar.getTime())){
                holder.tvTxtMetingName.setTextColor(context.getResources().getColor(R.color.red));
                holder.tvTxtDate.setTextColor(context.getResources().getColor(R.color.red));
                holder.tvTxtMetingPlace.setTextColor(context.getResources().getColor(R.color.red));
                holder.tvTxtMetingName.setText("Type: " + meeting.getMeeting_type());
                holder.tvTxtDate.setText("Var ferdig: " + meeting.getMeeting_end());
                holder.tvTxtMetingPlace.setText("Sted: " + meeting.getMeeting_place());

            }else {
                holder.tvTxtMetingName.setText("Type: " +meeting.getMeeting_type());
                holder.tvTxtDate.setText("Start tid: " + meeting.getMeeting_start());
                holder.tvTxtMetingPlace.setText("Sted: " + meeting.getMeeting_place());
                holder.tvTxtMetingName.setTextColor(context.getResources().getColor(R.color.black));
                holder.tvTxtDate.setTextColor(context.getResources().getColor(R.color.black));
                holder.tvTxtMetingPlace.setTextColor(context.getResources().getColor(R.color.black));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
    //Må ha antall får å kunne opprette
    @Override
    public int getItemCount() {
        return meetingItem.size();
    }

    //Trenger denne får å hente ut alle ViewIndexene som vi trenger få å setet inn.
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

            pane.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    meeting = meetingItem.get(position);
                    Intent intent = new Intent(context, NewMeeting.class);
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
                    alertDialogBuilder.setTitle(R.string.delete_meeting);
                    alertDialogBuilder.setMessage(R.string.delete_meeting_text);
                    alertDialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
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
                    alertDialogBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
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
