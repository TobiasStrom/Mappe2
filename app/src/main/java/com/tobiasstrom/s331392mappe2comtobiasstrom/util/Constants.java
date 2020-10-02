package com.tobiasstrom.s331392mappe2comtobiasstrom.util;

public class Constants {
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "mapppe2DB";

    //Table colums
    public static final String TABLE_CONTACT = "customerTBL";
    public static final String KEY_CONTACT_ID = "id";
    public static final String KEY_CONTACT_FIRSTNAME = "customer_firsname";
    public static final String KEY_CONTACT_LASTNAME = "customer_lastname";
    public static final String KEY_CONTACT_EMAIL = "customer_email";
    public static final String KEY_CONTACT_PHONENUMBER = "customer_phonenumber";

    //Table colums
    public static final String TABLE_MEETING = "meetingTBL";
    public static final String KEY_MEETING_ID = "id";
    public static final String KEY_MEETING_START = "meeting_start";
    public static final String KEY_MEETING_END = "meeting_date";
    public static final String KEY_MEETING_PLACE = "meeting_place";
    public static final String KEY_MEETING_TYPE = "meeting_type";

    //table colums
    public static final String TABLE_COMBO = "comboTBL";
    public static final String KEY_CONTACTTBL_ID = "customerTBL_id";
    public static final String KEY_MEETINGTBL_ID = "meetingTBL_ID";
}
