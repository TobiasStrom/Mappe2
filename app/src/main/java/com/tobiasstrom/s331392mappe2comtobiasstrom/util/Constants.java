package com.tobiasstrom.s331392mappe2comtobiasstrom.util;

import android.content.UriMatcher;
import android.net.Uri;

import java.util.ArrayList;

public class Constants {
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "mapppe2DB";

    //Table colums
    public static final String TABLE_CONTACT = "customerTBL";
    public static final String KEY_CONTACT_ID = "_id";
    public static final String KEY_CONTACT_FIRSTNAME = "customer_firsname";
    public static final String KEY_CONTACT_LASTNAME = "customer_lastname";
    public static final String KEY_CONTACT_EMAIL = "customer_email";
    public static final String KEY_CONTACT_PHONENUMBER = "customer_phonenumber";

    //Table colums
    public static final String TABLE_MEETING = "meetingTBL";
    public static final String KEY_MEETING_ID = "_id";
    public static final String KEY_MEETING_START = "meeting_start";
    public static final String KEY_MEETING_END = "meeting_end";
    public static final String KEY_MEETING_PLACE = "meeting_place";
    public static final String KEY_MEETING_TYPE = "meeting_type";

    //table colums
    public static final String TABLE_COMBO = "comboTBL";
    public static final String KEY_CONTACTTBL_ID = "customerTBL_id";
    public static final String KEY_MEETINGTBL_ID = "meetingTBL_ID";

    //For contents provider
    public static final String PROVIDER = "com.tobiasstrom.contentprovidermeetings";
    public static final int CONTAKT = 1;
    public static final int MCONTACT = 2;
    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER + "/contact");
    public static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER, "contact", MCONTACT);
        uriMatcher.addURI(PROVIDER, "contact/#", CONTAKT);
    }


    public static ArrayList<Integer> selectedItems;
}
