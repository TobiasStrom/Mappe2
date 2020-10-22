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
import com.tobiasstrom.s331392mappe2comtobiasstrom.activities.ContactDetailsActivity;
import com.tobiasstrom.s331392mappe2comtobiasstrom.data.DatabaseHandler;
import com.tobiasstrom.s331392mappe2comtobiasstrom.model.Contact;
import com.tobiasstrom.s331392mappe2comtobiasstrom.R;

import java.util.List;
//Trenger denne klassen får å kunne legge inn den informsjonen som vi trenger i recyclerview
//På den måten jeg ønsker
public class ContactsRecyclerViewAdapter extends RecyclerView.Adapter<ContactsRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "ContactsListViewAdapter";
    //opprette de variablene vi trenger
    private Context context;
    private List<Contact> contactItems;
    private Contact contact;
    //Konstruktør
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
    //Setter teksten ut i viewene
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
    //Trenger denne får å hente ut alle ViewIndexene som vi trenger få å setet inn.
    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvTxtFirstName;
        final TextView tvTxtLastName;
        final TextView tvTxtEmail;
        final TextView tvTxtPhone;
        public androidx.constraintlayout.widget.ConstraintLayout pane;

        public int id;

        public ViewHolder(@NonNull final View v, Context ctx) {
            super(v);
            context = ctx;
            this.tvTxtFirstName = v.findViewById(R.id.tvTxtFirstNameMeeting);
            this.tvTxtLastName = v.findViewById(R.id.tvTxtLastNameMeeting);
            this.tvTxtEmail = v.findViewById(R.id.tvTxtEmail);
            this.tvTxtPhone = v.findViewById(R.id.tvTxtPhone);
            this.pane = v.findViewById(R.id.contactPane);

            //vanlig onClick listener, den avfyres dersom kontakt panellen har ble trykket på
            //dette brukes for å endre kontaktens informasjon
            pane.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    int position = getAdapterPosition();
                    contact = contactItems.get(position);
                    Intent intent = new Intent(context, ContactDetailsActivity.class);
                    intent.putExtra("id", contact.getContactId());
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
                    //vis dialog om brukeren vil virkelig slette denne kontakten
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
                    alertDialogBuilder.setTitle(R.string.delete_contact);
                    alertDialogBuilder.setMessage(R.string.delete_contact_text);
                    alertDialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Dersom brukeren ønsker seg å slette denne kontaktet
                            int position = getAdapterPosition();
                            Contact contact = contactItems.get(position);
                            DatabaseHandler db = new DatabaseHandler(context);
                            //delete item
                            db.deleteContact(contact.getContactId());
                            contactItems.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());

                        }
                    });
                    alertDialogBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //dersom brukeren ønsker seg ikke å slette denne kontakten
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
