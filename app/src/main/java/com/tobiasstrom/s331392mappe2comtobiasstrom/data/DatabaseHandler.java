package com.tobiasstrom.s331392mappe2comtobiasstrom.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + Constants.TABLE_CONTACT + " (" +
                Constants.KEY_CONTACT_ID + " INTEGER PRIMARY KEY, " +
                Constants.KEY_CONTACT_FIRSTNAME + " NVARCHAR(200), " +
                Constants.KEY_CONTACT_LASTNAME + " NVARCHAR(200), " +
                Constants.KEY_CONTACT_PHONENUMBER + " NVARCHAR(200), " +
                Constants.KEY_CONTACT_EMAIL + " NVARCHAR(200));";

        String CREATE_MEETING_TABLE = "CREATE TABLE " + Constants.TABLE_MEETING + " (" +
                Constants.KEY_MEETING_ID + " INTEGER PRIMARY KEY, " +
                Constants.KEY_MEETING_START + " NVARCHAR(200), "+
                Constants.KEY_MEETING_END + " NVARCHAR(200), " +
                Constants.KEY_MEETING_PLACE + " NVARCHAR(200), " +
                Constants.KEY_MEETING_TYPE + " TEXT);";


        String CREATE_COMBO_TABLE = "CREATE TABLE " + Constants.TABLE_COMBO + " (" +
                Constants.KEY_CONTACTTBL_ID + " INTEGER, " +
                Constants.KEY_MEETINGTBL_ID + " INTEGER, " +
                "PRIMARY KEY(" + Constants.KEY_MEETINGTBL_ID +", "+ Constants.KEY_CONTACTTBL_ID +")," +
                "FOREIGN KEY (" + Constants.KEY_CONTACTTBL_ID  + ") " +
                "REFERENCES " + Constants.TABLE_CONTACT + "(" + Constants.KEY_CONTACT_ID + ")" +
                "ON DELETE NO " +
                "ACTION ON UPDATE NO ACTION,"+
                "FOREIGN KEY (" + Constants.KEY_CONTACTTBL_ID  + ") " +
                "REFERENCES " + Constants.TABLE_MEETING + "(" + Constants.KEY_CONTACT_ID + ")" +
                "ON DELETE NO ACTION " +
                "ON UPDATE NO ACTION"+
                ");";


        db.execSQL(CREATE_COMBO_TABLE);
        db.execSQL(CREATE_MEETING_TABLE);
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVerison, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_CONTACT);
        onCreate(db);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_MEETING);
        onCreate(db);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_COMBO);
        onCreate(db);
    }


    //add Contact
    public void addContacts(Contact contact){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_CONTACT_EMAIL, contact.getEmail());
        values.put(Constants.KEY_CONTACT_LASTNAME, contact.getLastName());
        values.put(Constants.KEY_CONTACT_FIRSTNAME, contact.getFirstName());
        values.put(Constants.KEY_CONTACT_PHONENUMBER, contact.getPhoneNumber());

        db.insert(Constants.TABLE_CONTACT, null, values);
    }
    //get all Grocery
    public List<Contact> getAllContacts(){
        SQLiteDatabase db = this.getReadableDatabase();

        List <Contact> contactList = new ArrayList<>();

        Cursor cursor = db.query(Constants.TABLE_CONTACT, new String[]{
                Constants.KEY_CONTACT_ID, Constants.KEY_CONTACT_FIRSTNAME, Constants.KEY_CONTACT_LASTNAME, Constants.KEY_CONTACT_PHONENUMBER, Constants.KEY_CONTACT_EMAIL
        }, null, null, null, null, Constants.KEY_CONTACT_LASTNAME + " DESC");
        if (cursor.moveToFirst()){
            do{
                Contact contact = new Contact();
                contact.setContactId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_CONTACT_ID))));
                contact.setFirstName(cursor.getString(cursor.getColumnIndex(Constants.KEY_CONTACT_FIRSTNAME)));
                contact.setLastName(cursor.getString(cursor.getColumnIndex(Constants.KEY_CONTACT_LASTNAME)));
                contact.setPhoneNumber(cursor.getString(cursor.getColumnIndex(Constants.KEY_CONTACT_PHONENUMBER)));
                contact.setEmail(cursor.getString(cursor.getColumnIndex(Constants.KEY_CONTACT_EMAIL)));

                contactList.add(contact);
            }while (cursor.moveToNext());
        }
        return contactList;
    }

    //get contacts 
    public int getContactCount(){
        String countQuery = "SELECT * FROM " + Constants.TABLE_CONTACT;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(countQuery, null);
        return cursor.getCount();
    }

    //delete item
    public void deleteContact(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Constants.TABLE_CONTACT, Constants.KEY_CONTACT_ID + " =? ", new String[]{String.valueOf(id)});
        db.close();
    }
    //
    public int updateContact(Contact contact){
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_CONTACT_FIRSTNAME, contact.getFirstName());
        values.put(Constants.KEY_CONTACT_LASTNAME, contact.getLastName());
        values.put(Constants.KEY_CONTACT_PHONENUMBER, contact.getPhoneNumber());
        values.put(Constants.KEY_CONTACT_EMAIL, contact.getEmail());

        return db.update(Constants.TABLE_CONTACT, values, Constants.KEY_CONTACT_ID + " =?" , new String[]{String.valueOf(contact.getContactId())});

    }


    //get all meeting
    public List<Meeting> getAllMeetings(){
        SQLiteDatabase db = this.getReadableDatabase();
        List<Meeting> meetingList = new ArrayList<>();
        Cursor cursor = db.query(Constants.TABLE_MEETING, new String[]{
                Constants.KEY_MEETING_ID, Constants.KEY_MEETING_START, Constants.KEY_MEETING_END, Constants.KEY_MEETING_PLACE, Constants.KEY_MEETING_TYPE
        }, null, null, null, null, Constants.KEY_MEETING_START + " DESC");
        if(cursor.moveToFirst()){
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
            }while (cursor.moveToNext());
        }
        return meetingList;
    }

    //get meetings count
    public int getMeetingCount(){
        String countQuery = "SELECT count(*) MeetingCount FROM " + Constants.TABLE_MEETING;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(countQuery, null);
        int ant = 0;
        if(cursor.moveToFirst())
        {
            ant = cursor.getInt(cursor.getColumnIndex("MeetingCount"));
        }
        return ant;
    }
    public void deleteMeeting(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Constants.TABLE_MEETING, Constants.KEY_MEETING_ID + " =? ", new String[]{String.valueOf(id)});
        db.close();

    }
    public void addMeeting(Meeting meeting){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Constants.KEY_MEETING_START, meeting.getMeeting_start());
        values.put(Constants.KEY_MEETING_END, meeting.getMeeting_end());
        values.put(Constants.KEY_MEETING_PLACE, meeting.getMeeting_place());
        values.put(Constants.KEY_MEETING_TYPE, meeting.getMeeting_type());

        db.insert(Constants.TABLE_MEETING,null, values);
    }

    public int updateMeeting(Meeting meeting){
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_MEETING_START, meeting.getMeeting_start());
        values.put(Constants.KEY_MEETING_END, meeting.getMeeting_end());
        values.put(Constants.KEY_MEETING_PLACE, meeting.getMeeting_place());
        values.put(Constants.KEY_MEETING_TYPE, meeting.getMeeting_type());

        return db.update(Constants.TABLE_CONTACT, values, Constants.KEY_MEETING_ID + " =?", new String[]{String.valueOf(meeting.getMetingId())});
    }

    public void addContactToMeeting(int meetingID, int contactID){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_CONTACTTBL_ID, contactID);
        values.put(Constants.KEY_MEETINGTBL_ID, meetingID);

        db.insert(Constants.TABLE_COMBO, null, values);
    }

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

            }while (cursor.moveToFirst());
        }
        return contactsList;
        //select customerTBL.customer_firsname from customerTBl cross join comboTBL on customerTBL.id = comboTBL.customerTBl_id  where comboTBL.meetingTBL_id =1;
    }
    public List<Contact> getContactNotInMeeting(int meetingID){
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
            }while (cursor.moveToFirst());
        }
        return contactsList;
        //select customerTBL.customer_firsname from customerTBl cross join comboTBL on customerTBL.id = comboTBL.customerTBl_id  where comboTBL.meetingTBL_id =1;
    }





}
