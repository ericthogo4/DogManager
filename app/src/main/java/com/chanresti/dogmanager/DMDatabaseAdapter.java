package com.chanresti.dogmanager;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class DMDatabaseAdapter {


    private DMDatabaseAdapter.DatabaseHelper dMDatabaseHelper;
    private static final String DATABASE_NAME = "dba_dm";
    private static final int DATABASE_VERSION = 1;
    private final Context dContext;
    private SQLiteDatabase dMDatabase;

    //Reminder fields
    private static final String TABLE_REMINDER = "tbl_rmd";

    private static final String COL_REMINDER_ID = "rmd_id";
    private static final String COL_REMINDER_TITLE = "rmd_nme";
    private static final String COL_REMINDER_DTS = "rmd_dts";

    private static final int INDEX_REMINDER_ID  = 0;
    private static final int INDEX_REMINDER_TITLE = INDEX_REMINDER_ID + 1;
    private static final int INDEX_REMINDER_DTS = INDEX_REMINDER_ID + 2;

    private static final String CREATE_TABLE_REMINDER =
            "CREATE TABLE if not exists " + TABLE_REMINDER + " ( " +
                    COL_REMINDER_ID + " INTEGER PRIMARY KEY autoincrement, " +
                    COL_REMINDER_TITLE + " TEXT, " +
                    COL_REMINDER_DTS + " TEXT );";



    //Profile fields
    private static final String TABLE_PROFILE = "tbl_prf";

    private static final String COL_PROFILE_ID = "prf_id";
    private static final String COL_PROFILE_NAME = "prf_nme";
    private static final String COL_PROFILE_PPU = "prf_ppu";
    private static final String COL_PROFILE_WEIGHT = "prf_wgt";
    private static final String COL_PROFILE_GENDER = "prf_gdr";
    private static final String COL_PROFILE_BREED = "prf_brd";
    private static final String COL_PROFILE_DOB = "prf_dob";

    private static final int INDEX_PROFILE_ID = 0;
    private static final int INDEX_PROFILE_NAME = INDEX_PROFILE_ID + 1;
    private static final int INDEX_PROFILE_PPU = INDEX_PROFILE_ID + 2;
    private static final int INDEX_PROFILE_WEIGHT = INDEX_PROFILE_ID + 3;
    private static final int INDEX_PROFILE_GENDER = INDEX_PROFILE_ID + 4;
    private static final int INDEX_PROFILE_BREED = INDEX_PROFILE_ID + 5;
    private static final int INDEX_PROFILE_DOB = INDEX_PROFILE_ID + 6;

    private static final String CREATE_TABLE_PROFILE =
            "CREATE TABLE if not exists " + TABLE_PROFILE + " ( " +
                    COL_PROFILE_ID + " INTEGER PRIMARY KEY autoincrement, " +
                    COL_PROFILE_NAME + " TEXT, " +
                    COL_PROFILE_PPU + " TEXT, " +
                    COL_PROFILE_WEIGHT + " INTEGER, " +
                    COL_PROFILE_GENDER + " INTEGER, " +
                    COL_PROFILE_BREED + " TEXT, " +
                    COL_PROFILE_DOB + " TEXT );";





    protected DMDatabaseAdapter(Context dContext) {
        this.dContext = dContext;
    }

    protected void open() throws SQLException {
        dMDatabaseHelper = new DMDatabaseAdapter.DatabaseHelper(dContext);
        dMDatabase = dMDatabaseHelper.getWritableDatabase();
    }

    //Reminder Methods
    protected   void updateReminder(int reminderId, String nReminderTitle, String nReminderDTS){

        ContentValues reminderValues = new ContentValues();

        reminderValues.put(COL_REMINDER_TITLE, nReminderTitle);
        reminderValues.put(COL_REMINDER_DTS, nReminderDTS);

        dMDatabase.update(TABLE_REMINDER, reminderValues,
                COL_REMINDER_ID + "=?", new String[]{String.valueOf(reminderId)});

    }

    protected void deleteReminder(int reminderId) {
        dMDatabase.delete(TABLE_REMINDER, COL_REMINDER_ID + "=?", new String[]{String.valueOf(reminderId)});
    }

    protected int createReminder(String reminderTitle, String reminderDTS) {
        ContentValues reminderValues = new ContentValues();

        reminderValues.put(COL_REMINDER_TITLE, reminderTitle);
        reminderValues.put(COL_REMINDER_DTS, reminderDTS);

        dMDatabase.insert(TABLE_REMINDER, null, reminderValues);

        int newReminderId= fetchReminders().get(0).getReminderId();
        return newReminderId;
    }

    protected List<Reminder> fetchReminders() {
        Reminder reminder;
        List<Reminder> reminderList =new ArrayList<>();

        Cursor reminderTableCursor = dMDatabase.query(TABLE_REMINDER, new String[]{COL_REMINDER_ID,
                        COL_REMINDER_TITLE,COL_REMINDER_DTS},
                null, null, null, null, COL_REMINDER_ID +" DESC",null
        );

        if (reminderTableCursor != null){
            while (reminderTableCursor.moveToNext()){

                int reminderId = reminderTableCursor.getInt(INDEX_REMINDER_ID);
                String reminderTitle = reminderTableCursor.getString(INDEX_REMINDER_TITLE);
                String reminderDTS = reminderTableCursor.getString(INDEX_REMINDER_DTS);

                String reminderDOF = reminderDTS.substring(7,17);
                String reminderTOF = reminderDTS.substring(0,5);
                long reminderTIM = getReminderTIMFromDTS(reminderDTS);

                reminder= new Reminder(reminderId, reminderTitle, reminderDOF, reminderTOF, reminderTIM);

                reminderList.add(reminder);}
        }

        if(reminderList.size()!=0){
            DMDatabaseAdapter.ReminderComparator reminderComparator = new DMDatabaseAdapter.ReminderComparator();
            Collections.sort(reminderList,reminderComparator);
        }

        return reminderList;
    }

    protected long getReminderTIMFromDTS(String reminderDTS){
        DateFormat dateFormat=new SimpleDateFormat("HH:mm  dd/MM/yyyy");
        Date date = null;
        try {
            date=dateFormat.parse(reminderDTS);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long reminderTIM = 0;

        if (date != null) {
            reminderTIM = date.getTime();
        }

        return reminderTIM;
    }



    //Profile Methods
    protected   void updateProfile(int profileId, String nProfileName, int nProfileWeight, int nProfileGender, String nProfileBreed, String nProfileDob){

        ContentValues profileValues = new ContentValues();

        profileValues.put(COL_PROFILE_NAME, nProfileName);
        profileValues.put(COL_PROFILE_WEIGHT, nProfileWeight);
        profileValues.put(COL_PROFILE_GENDER, nProfileGender);
        profileValues.put(COL_PROFILE_BREED, nProfileBreed);
        profileValues.put(COL_PROFILE_DOB, nProfileDob);

        dMDatabase.update(TABLE_PROFILE, profileValues,
                COL_PROFILE_ID + "=?", new String[]{String.valueOf(profileId)});
    }

    protected   void updateProfilePicture(int profileId, String nProfilePicture){

        ContentValues profileValues = new ContentValues();

        profileValues.put(COL_PROFILE_PPU, nProfilePicture);

        dMDatabase.update(TABLE_PROFILE, profileValues,
                COL_PROFILE_ID + "=?", new String[]{String.valueOf(profileId)});

    }

    protected void deleteProfile(int profileId) {
        dMDatabase.delete(TABLE_PROFILE, COL_PROFILE_ID + "=?", new String[]{String.valueOf(profileId)});
    }

    protected int createProfile(String name, String profilePictureUri, int weight, int gender, String breed, String dateOfBirth) {

        ContentValues profileValues = new ContentValues();

        profileValues.put(COL_PROFILE_NAME, name);
        profileValues.put(COL_PROFILE_PPU, profilePictureUri);
        profileValues.put(COL_PROFILE_WEIGHT, weight);
        profileValues.put(COL_PROFILE_GENDER, gender);
        profileValues.put(COL_PROFILE_BREED, breed);
        profileValues.put(COL_PROFILE_DOB, dateOfBirth);

        dMDatabase.insert(TABLE_PROFILE, null, profileValues);

        int newProfileId= fetchProfiles().get(0).getId();
        return newProfileId;
    }

    protected List<Profile> fetchProfiles() {

        Profile profile;
        List<Profile> profileList =new ArrayList<>();

        Cursor profileTableCursor = dMDatabase.query(TABLE_PROFILE, new String[]{COL_PROFILE_ID,
                        COL_PROFILE_NAME,
                        COL_PROFILE_PPU,
                        COL_PROFILE_WEIGHT,
                        COL_PROFILE_GENDER,
                        COL_PROFILE_BREED,
                        COL_PROFILE_DOB},
                null, null, null, null, COL_PROFILE_NAME +" ASC",null
        );


        if (profileTableCursor != null){
            while (profileTableCursor.moveToNext()){

                int id = profileTableCursor.getInt(INDEX_PROFILE_ID);
                String name = profileTableCursor.getString(INDEX_PROFILE_NAME);
                String profilePictureUri = profileTableCursor.getString(INDEX_PROFILE_PPU);
                int weight = profileTableCursor.getInt(INDEX_PROFILE_WEIGHT);
                int gender = profileTableCursor.getInt(INDEX_PROFILE_GENDER);
                String breed = profileTableCursor.getString(INDEX_PROFILE_BREED);
                String dateOfBirth = profileTableCursor.getString(INDEX_PROFILE_DOB);
                int age = getAgeFromDateOfBirth(dateOfBirth);

                profile= new Profile(id, name, profilePictureUri, weight, gender, breed, dateOfBirth, age);

                profileList.add(profile);}
        }
        return profileList;
    }

    protected int getAgeFromDateOfBirth(String dob){

        long dobMills = getTIMFromDS(dob);
        long nowTIM = getNowTIM();
        long ageInMills = nowTIM - dobMills;
        int ageInDays = (int) (ageInMills/ (1000*60*60*24));
        int ageInYears = (int) (ageInDays/ (365));

        return ageInYears;
    }

    public long getTIMFromDS(String reminderDTS){
        DateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date=dateFormat.parse(reminderDTS);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long reminderTIM = 0;

        if (date != null) {
            reminderTIM = date.getTime();
        }

        return reminderTIM;
    }

    protected long getNowTIM(){
        Date currentDateTime =new Date();
        long currentDateTimeInMills = currentDateTime.getTime();
        return currentDateTimeInMills;
    }

    protected void close() {
        if (dMDatabaseHelper != null) {
            dMDatabaseHelper.close();
        }
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE_REMINDER);
            db.execSQL(CREATE_TABLE_PROFILE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDER);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILE);
            onCreate(db);
        }
    }

    protected static class ReminderComparator implements Comparator<Reminder> {

        public int compare(Reminder reminderOne, Reminder reminderTwo){
            if(reminderOne.getReminderTIM()==reminderTwo.getReminderTIM()){
                return 0;
            }
            else if(reminderOne.getReminderTIM()>reminderTwo.getReminderTIM()){
                return -1;
            }
            else {
                return 1;
            }


        }

    }









}
