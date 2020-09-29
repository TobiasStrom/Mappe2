package com.tobiasstrom.s331392mappe2comtobiasstrom.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.tobiasstrom.s331392mappe2comtobiasstrom.model.Contact;
import com.tobiasstrom.s331392mappe2comtobiasstrom.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHandler";


    public DatabaseHandler(Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + Constants.TABLE_CONTACT + " (" +
                Constants.KEY_CONTACT_ID + " INTEGER PRIMARY KEY, " +
                Constants.KEY_CONTACT_FIRSTNAME + " TEXT, " +
                Constants.KEY_CONTACT_LASTNAME + " TEXT, " +
                Constants.KEY_CONTACT_PHONENUMBER + " TXT, " +
                Constants.KEY_CONTACT_EMAIL + " TXT);";

        //Contact contact = new Contact();
        //contact.setFirstName("Tobias");
        //contact.setLastName("Strøm");
        //contact.setPhoneNumber("95163394");
        //contact.setEmail("tobias@tobiasstrom@gmail.com");
        //db.execSQL("DROP TABLE " + Constants.TABLE_CONTACT);
        db.execSQL(CREATE_CONTACTS_TABLE);
        //addContacts(contact);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVerison, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_CONTACT);

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
        Log.d(TAG, "AddGrocery: Saved to db");
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
    public int getGroceryCount(){
        String countQuery = "SELECT * FROM " + Constants.TABLE_CONTACT;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(countQuery, null);

        return cursor.getCount();
    }
}
