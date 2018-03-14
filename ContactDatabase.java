package com.andy.pru.phone_contact_diary;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

// ContactDatabase class that encapsulates the database functions of this application
class ContactDatabase
{

    // define constants
    private static final String KEY_ROWID = "ContactID";
    private static final String KEY_FirstName= "FirstName";
    private static final String KEY_LastName = "LastName";
    private static final String KEY_emailID = "emailID";
    private static final String KEY_Mobile01 = "Mobile01";
    private static final String KEY_Company = "Company";
    private static final String KEY_Address="Address";
    private static final String KEY_Gender="Gender";
    private static final String TAG = "DBAdapter";

    private static final String DATABASE_NAME = "PhonebookDB";
    private static final String DATABASE_TABLE = "Contacts";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE = "create table Contacts (ContactID integer primary key autoincrement, FirstName text not null, LastName text not null, emailID text not null, Mobile01 text not null, Company text not null, Address text not null, Gender text not null );";

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    ContactDatabase(Context ctx)
    {
        DBHelper = new DatabaseHelper(ctx);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        // creates the db if it does not exist
        @Override
        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL(DATABASE_CREATE);
        }

        // called when the db needs upgrading
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion,
                              int newVersion)
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS titles");
            onCreate(db);
        }
    }

    //open db
    void open() throws SQLException
    {
        db = DBHelper.getWritableDatabase();
    }

    //close db
    void close()
    {
        DBHelper.close();
    }

    //add a record
    long insertContacts(String FirstName, String LastName, String emailID, String Mobile01, String Company, String Address, String Gender)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_FirstName, FirstName);
        initialValues.put(KEY_LastName, LastName);
        initialValues.put(KEY_emailID, emailID);
        initialValues.put(KEY_Mobile01, Mobile01);
        initialValues.put(KEY_Company,Company);
        initialValues.put(KEY_Address,Address);
        initialValues.put(KEY_Gender,Gender);
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    //delete a record
    boolean deleteContacts(long rowId)
    {
        return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }
    //delete all the records
    void deleteContactsAll(){
        db.delete(DATABASE_TABLE, "1", null);
    }

    //get all record
    Cursor getAllContacts()
    {
        return db.query(DATABASE_TABLE, new String[] {KEY_ROWID,KEY_FirstName,KEY_LastName,KEY_emailID,KEY_Mobile01,KEY_Company,KEY_Address,KEY_Gender}, null, null, null, null, null);
    }

    //get a record
    Cursor getContacts(long rowId) throws SQLException
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,KEY_FirstName,KEY_LastName, KEY_emailID,KEY_Mobile01,KEY_Company,KEY_Address,KEY_Gender},
                        KEY_ROWID + "=" + rowId,
                        null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //record update
    boolean updateContacts(long rowId, String FirstName, String LastName, String emailID, String Mobile01, String Company, String Address, String Gender)
    {
        ContentValues args = new ContentValues();
        args.put(KEY_FirstName, FirstName);
        args.put(KEY_LastName, LastName);
        args.put(KEY_emailID, emailID);
        args.put(KEY_Mobile01, Mobile01);
        args.put(KEY_Company,Company);
        args.put(KEY_Address,Address);
        args.put(KEY_Gender,Gender);
        return db.update(DATABASE_TABLE, args,
                KEY_ROWID + "=" + rowId, null) > 0;
    }
}