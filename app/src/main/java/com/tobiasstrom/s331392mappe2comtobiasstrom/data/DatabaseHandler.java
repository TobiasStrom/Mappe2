package com.tobiasstrom.s331392mappe2comtobiasstrom.data;

import android.annotation.SuppressLint;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.tobiasstrom.s331392mappe2comtobiasstrom.model.Contact;
import com.tobiasstrom.s331392mappe2comtobiasstrom.model.Meeting;
import com.tobiasstrom.s331392mappe2comtobiasstrom.util.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHandler";

    public DatabaseHandler(Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
    }
    //Bruker ikke onCreate eller on Upgrade da disse er lagret i ContentProviderHandler
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    //Legger til en kontakt
    public void addContacts(Contact contact){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_CONTACT_EMAIL, contact.getEmail());
        values.put(Constants.KEY_CONTACT_LASTNAME, contact.getLastName());
        values.put(Constants.KEY_CONTACT_FIRSTNAME, contact.getFirstName());
        values.put(Constants.KEY_CONTACT_PHONENUMBER, contact.getPhoneNumber());

        db.insert(Constants.TABLE_CONTACT, null, values);
        //db.close();
    }
    //Henter ut en kontakt med id.
    public Contact getAContact(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(Constants.TABLE_CONTACT, new String[]{Constants.KEY_CONTACT_ID, Constants.KEY_CONTACT_FIRSTNAME, Constants.KEY_CONTACT_LASTNAME, Constants.KEY_CONTACT_PHONENUMBER, Constants.KEY_CONTACT_EMAIL},
                Constants.KEY_CONTACT_ID+ "=?",
                new String[]{String.valueOf(id)},null,null,null,null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
            Contact contact = new Contact();
            contact.setContactId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_CONTACT_ID))));
            contact.setFirstName(cursor.getString(cursor.getColumnIndex(Constants.KEY_CONTACT_FIRSTNAME)));
            contact.setLastName(cursor.getString(cursor.getColumnIndex(Constants.KEY_CONTACT_LASTNAME)));
            contact.setPhoneNumber(cursor.getString(cursor.getColumnIndex(Constants.KEY_CONTACT_PHONENUMBER)));
            contact.setEmail(cursor.getString(cursor.getColumnIndex(Constants.KEY_CONTACT_EMAIL)));

        return contact;
    }

    //Henter en liste med alle kontaktene.
    public List<Contact> getAllContacts(){
        SQLiteDatabase db = this.getReadableDatabase();

        List <Contact> contactList = new ArrayList<>();

        Cursor cursor = db.query(Constants.TABLE_CONTACT, new String[]{
                Constants.KEY_CONTACT_ID, Constants.KEY_CONTACT_FIRSTNAME, Constants.KEY_CONTACT_LASTNAME, Constants.KEY_CONTACT_PHONENUMBER, Constants.KEY_CONTACT_EMAIL
        }, null, null, null, null, Constants.KEY_CONTACT_LASTNAME);
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setContactId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_CONTACT_ID))));
                contact.setFirstName(cursor.getString(cursor.getColumnIndex(Constants.KEY_CONTACT_FIRSTNAME)));
                contact.setLastName(cursor.getString(cursor.getColumnIndex(Constants.KEY_CONTACT_LASTNAME)));
                contact.setPhoneNumber(cursor.getString(cursor.getColumnIndex(Constants.KEY_CONTACT_PHONENUMBER)));
                contact.setEmail(cursor.getString(cursor.getColumnIndex(Constants.KEY_CONTACT_EMAIL)));

                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return contactList;
    }

    //Henter ut antall kontakter som brukers til å srive ut
    public int getContactCount(){
        String countQuery = "SELECT * FROM " + Constants.TABLE_CONTACT;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(countQuery, null);
        int i = cursor.getCount();
        //db.close();
        return i;
    }

    //Slette en kontakt
    public void deleteContact(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        //slettes rekord fra kontakter tabell
        db.delete(Constants.TABLE_CONTACT, Constants.KEY_CONTACT_ID + " =? ", new String[]{String.valueOf(id)});
        //slettes rekord fra combo tabell
        db.delete(Constants.TABLE_COMBO, Constants.KEY_CONTACTTBL_ID + " =? ", new String[]{String.valueOf(id)});
        //db.close();
    }

    //Oppdatere en kontakt
    public int updateContact(Contact contact){

        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.KEY_CONTACT_FIRSTNAME, contact.getFirstName());
        values.put(Constants.KEY_CONTACT_LASTNAME, contact.getLastName());
        values.put(Constants.KEY_CONTACT_PHONENUMBER, contact.getPhoneNumber());
        values.put(Constants.KEY_CONTACT_EMAIL, contact.getEmail());

        return db.update(Constants.TABLE_CONTACT, values, Constants.KEY_CONTACT_ID + " =?" , new String[]{String.valueOf(contact.getContactId())});
    }

    //Hente alle møtene
    public List<Meeting> getAllMeetings(){
        SQLiteDatabase db = this.getReadableDatabase();
        List<Meeting> meetingList = new ArrayList<>();
        Cursor cursor = db.query(Constants.TABLE_MEETING, new String[]{
                Constants.KEY_MEETING_ID, Constants.KEY_MEETING_START, Constants.KEY_MEETING_END, Constants.KEY_MEETING_PLACE, Constants.KEY_MEETING_TYPE
        }, null, null, null, null, Constants.KEY_MEETING_START);

        if (cursor.moveToFirst()){
            do {
                Meeting meeting = new Meeting();
                String date = cursor.getString(cursor.getColumnIndex(Constants.KEY_MEETING_START));
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:MM:SS", Locale.getDefault());
                meeting.setMetingId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_MEETING_ID))));
                meeting.setMeeting_start(cursor.getString(cursor.getColumnIndex(Constants.KEY_MEETING_START)));
                meeting.setMeeting_end(cursor.getString(cursor.getColumnIndex(Constants.KEY_MEETING_END)));
                meeting.setMeeting_place(cursor.getString(cursor.getColumnIndex(Constants.KEY_MEETING_PLACE)));
                meeting.setMeeting_type(cursor.getString(cursor.getColumnIndex(Constants.KEY_MEETING_TYPE)));

                meetingList.add(meeting);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return meetingList;
    }

    //Henter antall møter.
    public int getMeetingCount(){
        String countQuery = "SELECT count(*) MeetingCount FROM " + Constants.TABLE_MEETING+";";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(countQuery, null);
        int ant = 0;
        if(cursor.moveToFirst())
        {
            ant = cursor.getInt(cursor.getColumnIndex("MeetingCount"));
        }
        //db.close();
        return ant;
    }
    //Sletter en kontakt fra et møte
    public void deleteContactsFromMeeting(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Constants.TABLE_COMBO, Constants.KEY_MEETINGTBL_ID + " =? ", new String[]{String.valueOf(id)});
        //db.close();
    }
    //Sletter møte
    public void deleteMeeting(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        //slettes rekord fra møte tabell
        db.delete(Constants.TABLE_MEETING, Constants.KEY_MEETING_ID + " =? ", new String[]{String.valueOf(id)});
        //slettes rekord fra combo tabell
        db.delete(Constants.TABLE_COMBO, Constants.KEY_MEETINGTBL_ID + " =? ", new String[]{String.valueOf(id)});
    }
    //Legger til møte
    public void addMeeting(Meeting meeting){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Constants.KEY_MEETING_START, meeting.getMeeting_start());
        values.put(Constants.KEY_MEETING_END, meeting.getMeeting_end());
        values.put(Constants.KEY_MEETING_PLACE, meeting.getMeeting_place());
        values.put(Constants.KEY_MEETING_TYPE, meeting.getMeeting_type());
        Log.e(TAG, "addMeeting: " + meeting.getMeeting_start() );
        Log.e(TAG, "addMeeting: " + meeting.getMeeting_end() );
        Log.e(TAG, "addMeeting: " + meeting.getMeeting_place() );
        Log.e(TAG, "addMeeting: " + meeting.getMeeting_type() );
        Log.e(TAG, "addMeeting: " + db.insert(Constants.TABLE_MEETING,null, values));

    }
    //Oppdaterer møte med ny informsjon.
    public int updateMeeting(Meeting meeting){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_MEETING_START, meeting.getMeeting_start());
        values.put(Constants.KEY_MEETING_END, meeting.getMeeting_end());
        values.put(Constants.KEY_MEETING_PLACE, meeting.getMeeting_place());
        values.put(Constants.KEY_MEETING_TYPE, meeting.getMeeting_type());
        int i = db.update(Constants.TABLE_MEETING, values, Constants.KEY_MEETING_ID + " =? ", new String[]{String.valueOf(meeting.getMetingId())});
        //db.close();
        return i;
    }
    //Henter ut siste møte får å kunne sette id på møte
    public Meeting getLastMeeting() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlquerry = "Select * FROM " + Constants.TABLE_MEETING + " ORDER BY " + Constants.KEY_MEETING_ID + " DESC limit 1;";
        Cursor cursor = db.rawQuery(sqlquerry, null);
        cursor.moveToFirst();
        Meeting meeting = new Meeting(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4));
        cursor.close();
        //db.close();
        return meeting;
    }

    //Legger til en kontakt i møte
    public void addContactToMeeting(int meetingID, int contactID){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_CONTACTTBL_ID, contactID);
        values.put(Constants.KEY_MEETINGTBL_ID, meetingID);
        try {
            db.insert(Constants.TABLE_COMBO, null, values);
        } catch (SQLiteConstraintException e) {
            e.printStackTrace();
        }
    }
    //Henter ut id til kontaktene i møte.
    public List<Integer> getContatctIdInMeeting(int meetingID) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Integer> contactId = new ArrayList<>();
        //select * from comboTBL where meetingTBL_ID = id;
        String findPersonInMeeting = "SELECT * FROM "+Constants.TABLE_COMBO+" WHERE " + Constants.KEY_MEETINGTBL_ID+" = "+meetingID+";";

        Cursor cursor = db.rawQuery(findPersonInMeeting, null);
        if (cursor.moveToFirst()){
            do {
                Integer i = cursor.getInt(0);
                contactId.add(i);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return contactId;

    }
    //Henter ut kontakten i møte med en spellsile id.
    public List<Contact> getContactInMeeting(int meetingID){
        SQLiteDatabase db = this.getReadableDatabase();
        List<Contact> contactsList = new ArrayList<>();
        String findPersonInMeeting = "SELECT *" +
                " FROM "+ Constants.TABLE_CONTACT + " CROSS JOIN " + Constants.TABLE_COMBO + " ON "+ Constants.TABLE_CONTACT+"."+ Constants.KEY_CONTACT_ID + " = "+ Constants.TABLE_COMBO + "." + Constants.KEY_CONTACTTBL_ID +
                " WHERE "+ Constants.TABLE_COMBO+ "."+ Constants.KEY_MEETINGTBL_ID + " = " + meetingID + ";";

        Cursor cursor = db.rawQuery(findPersonInMeeting, null);
        if(cursor.moveToFirst()){
            do {
                Contact contact = new Contact();
                contact.setContactId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_CONTACT_ID))));
                contact.setFirstName(cursor.getString(cursor.getColumnIndex(Constants.KEY_CONTACT_FIRSTNAME)));
                contact.setLastName(cursor.getString(cursor.getColumnIndex(Constants.KEY_CONTACT_LASTNAME)));
                contact.setPhoneNumber(cursor.getString(cursor.getColumnIndex(Constants.KEY_CONTACT_PHONENUMBER)));
                contact.setEmail(cursor.getString(cursor.getColumnIndex(Constants.KEY_CONTACT_EMAIL)));
                contactsList.add(contact);

            }while (cursor.moveToNext());
        }
        cursor.close();

        return contactsList;

    }
    //Finner ut hvilke kontakter som ikke er i møte
    public List<Contact> getContactNotInMeeting(int meetingID){ // NOTE: denne metoden virker men resultatet blir ikke fulstending riktig.
        SQLiteDatabase db = this.getReadableDatabase();
        List<Contact> contactsList = new ArrayList<>();
        String findPersonInMeeting = "SELECT * FROM "+Constants.TABLE_CONTACT +
                " LEFT JOIN "+Constants.TABLE_COMBO+" ON "+Constants.TABLE_CONTACT+"."+Constants.KEY_CONTACT_ID+" = "+Constants.TABLE_COMBO+"."+Constants.KEY_CONTACTTBL_ID+
                " WHERE "+Constants.TABLE_COMBO+"."+Constants.KEY_MEETINGTBL_ID+" != "+meetingID+" OR "+Constants.TABLE_COMBO+"."+Constants.KEY_MEETINGTBL_ID+
                " IS NULL GROUP BY "+Constants.TABLE_CONTACT+"."+Constants.KEY_CONTACT_ID+";";
        Cursor cursor = db.rawQuery(findPersonInMeeting, null);
        if(cursor.moveToFirst()){
            do {
                Contact contact = new Contact();
                contact.setContactId(cursor.getInt(0));
                contact.setFirstName(cursor.getString(1));
                contact.setLastName(cursor.getString(2));
                contact.setPhoneNumber(cursor.getString(3));
                contact.setEmail(cursor.getString(4));
                contactsList.add(contact);
                /*contact.setContactId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_CONTACT_ID))));
                contact.setFirstName(cursor.getString(cursor.getColumnIndex(Constants.KEY_CONTACT_FIRSTNAME)));
                contact.setLastName(cursor.getString(cursor.getColumnIndex(Constants.KEY_CONTACT_LASTNAME)));
                contact.setPhoneNumber(cursor.getString(cursor.getColumnIndex(Constants.KEY_CONTACT_PHONENUMBER)));
                contact.setEmail(cursor.getString(cursor.getColumnIndex(Constants.KEY_CONTACT_EMAIL)));*/
            }while (cursor.moveToNext());
        }
        cursor.close();
        return contactsList;

    }
}
