package com.tobiasstrom.s331392mappe2comtobiasstrom.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tobiasstrom.s331392mappe2comtobiasstrom.util.Constants;

public class ContentProviderHandeler extends ContentProvider {
    private static final String TAG = "ContentProviderHandeler";

    ContentProviderHandeler.DatabaseHelper DBhelper;
    SQLiteDatabase db;

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
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
                    Constants.KEY_MEETING_TYPE + " NVARCHAR(200));";

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

            db.execSQL(CREATE_CONTACTS_TABLE);
            db.execSQL(CREATE_MEETING_TABLE);
            db.execSQL(CREATE_COMBO_TABLE);
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_CONTACT);
            onCreate(db);
            db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_MEETING);
            onCreate(db);
            db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_COMBO);
            onCreate(db);
        }
    }
    @Override
    public boolean onCreate() {
        DBhelper = new ContentProviderHandeler.DatabaseHelper(getContext());
        db = DBhelper.getWritableDatabase();
        return true;
    }
    @Override
    public String getType(Uri uri) {
        switch (Constants.uriMatcher.match(uri)) {
            case Constants.MCONTACT:
                return "vnd.android.cursor.dir/vnd.example.contact";
            case Constants.CONTAKT:
                return "vnd.android.cursor.item/vnd.example.contact";
            default:
                throw new
                        IllegalArgumentException("Ugyldig URI" + uri);
        }
    }
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = DBhelper.getWritableDatabase();
        db.insert(Constants.TABLE_CONTACT, null, values);
        Cursor c = db.query(Constants.TABLE_CONTACT, null, null, null, null, null, null);
        c.moveToLast();
        long minid = c.getLong(0);
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, minid);
    }



    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cur = null;
        if (Constants.uriMatcher.match(uri) == Constants.CONTAKT) {
            cur = db.query(Constants.TABLE_CONTACT, projection, Constants.KEY_CONTACT_ID + "=" + uri.getPathSegments().get(1), selectionArgs, null, null, sortOrder);
            return cur;
        } else {
            cur = db.query(Constants.TABLE_CONTACT, null, null, null, null, null, null);
            return cur;
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
