package com.tobiasstrom.s331392mappe2comtobiasstrom.ui;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tobiasstrom.s331392mappe2comtobiasstrom.activities.ContactDetailsActivity;
import com.tobiasstrom.s331392mappe2comtobiasstrom.data.DatabaseHandler;
import com.tobiasstrom.s331392mappe2comtobiasstrom.model.Contact;
import com.tobiasstrom.s331392mappe2comtobiasstrom.R;

import java.util.List;

public class ContactsRecyclerViewAdapter extends RecyclerView.Adapter<ContactsRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "ContactsListViewAdapter";
    private Context context;
    //private Context context;
    private  int layoutResource;
    private  LayoutInflater layoutInflater;
    private List<Contact> contactItems;

    public ContactsRecyclerViewAdapter(Context context, List<Contact> contactItems) {
        this.context = context;
        this.contactItems = contactItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listview_row_contacts, parent, false);

        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Contact contact = contactItems.get(position);
        holder.tvTxtFirstName.setText(contact.getFirstName());
        holder.tvTxtLastName.setText(contact.getLastName());
        holder.tvTxtEmail.setText(contact.getEmail());
        holder.tvTxtPhone.setText(contact.getPhoneNumber());
    }

    @Override
    public int getItemCount() {
        return contactItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final TextView tvTxtFirstName;
        final TextView tvTxtLastName;
        final TextView tvTxtEmail;
        final TextView tvTxtPhone;
        public ImageButton btnDelete;
        public androidx.constraintlayout.widget.ConstraintLayout pane;

        public int id;

        public ViewHolder(@NonNull View v, Context ctx) {
            super(v);
            context = ctx;
            this.tvTxtFirstName = v.findViewById(R.id.tvTxtFirstName);
            this.tvTxtLastName = v.findViewById(R.id.tvTxtLastName);
            this.tvTxtEmail = v.findViewById(R.id.tvTxtEmail);
            this.tvTxtPhone = v.findViewById(R.id.tvTxtPhone);
            this.pane = v.findViewById(R.id.contactPane);
            //this.btnDelete = v.findViewById(R.id.btnDelete);
            //btnDelete.setOnClickListener(this);

            //vanlig onClick listener, den avfyres dersom kontakt panellen har ble trykket på
            //dette brukes for å endre kontaktens informasjon
            pane.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Contact contact = contactItems.get(position);
                    Intent intent = new Intent(context, ContactDetailsActivity.class);
                    intent.putExtra("firstname", contact.getFirstName());
                    intent.putExtra("lastname", contact.getLastName());
                    intent.putExtra("phone", contact.getPhoneNumber());
                    intent.putExtra("email", contact.getEmail());
                    context.startActivity(intent);
                }
                
            });

            //trykk og hold på listener, den avfyres dersom kontakt panellen har ble trykket og hold
            //dette brukes for å slette kontaktens informsjon
            pane.setOnLongClickListener(new View.OnLongClickListener() {

                public boolean onLongClick(View view) {
                    int position = getAdapterPosition();
                    Contact contact = contactItems.get(position);
                    DatabaseHandler db = new DatabaseHandler(context);
                    //delete item
                    db.deleteContact(contact.getContactId());
                    contactItems.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    return true;
                }

            });


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

                    Contact contact = contactItems.get(position);
                    Intent intent = new Intent(context, ContactDetailsActivity.class);
                    intent.putExtra("firstname", contact.getFirstName());
                    intent.putExtra("lastname", contact.getLastName());
                    intent.putExtra("phone", contact.getPhoneNumber());
                    intent.putExtra("email", contact.getEmail());
                    context.startActivity(intent);
                }
            });

        }

        //@Override
        public void onClick(View v) {
            /*switch (v.getId()){
                case R.id.btnDelete:

                    int position = getAdapterPosition();
                    Contact contact = contactItems.get(position);
                    DatabaseHandler db = new DatabaseHandler(context);
                    //delete item
                    db.deleteContact(contact.getContactId());
                    contactItems.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    break;
            }*/
        }

        public void onLongClick(View v){
            
        }

    }


}
