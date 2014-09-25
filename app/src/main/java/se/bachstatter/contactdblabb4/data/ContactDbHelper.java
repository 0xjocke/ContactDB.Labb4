package se.bachstatter.contactdblabb4.data;

/**
 * Created by Jocek on 2014-09-24.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import se.bachstatter.contactdblabb4.R;
import se.bachstatter.contactdblabb4.data.contracts.ContactContract;
import se.bachstatter.contactdblabb4.fragments.ContactDetailFragment;
import se.bachstatter.contactdblabb4.models.Contact;

import static se.bachstatter.contactdblabb4.data.contracts.ContactContract.*;

/**
 * Created by Jocek on 2014-09-17.
 */
public class ContactDbHelper extends SQLiteOpenHelper {

    public static final String TEXT_TYPE= " text";
    public static final String INT= " integer";
    public static final String COMMA= ", ";
    private static boolean SORT_ON_NAME = false ;


    private static final String DATABASE_NAME = "ContactsDb.db";
    private static final int DATABASE_VERSION = 2;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + ContactEntry.TABLE_NAME + "(" +
            ContactEntry._ID + " integer primary key, " +
            ContactEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA +
            ContactEntry.COLUMN_NAME_IMG_URL + TEXT_TYPE + COMMA +
            ContactEntry.COLUMN_NAME_AGE + INT + COMMA +
            ContactEntry.COLUMN_NAME_DESCRIPTION + TEXT_TYPE +
            ");";
    String[] projection = {
            ContactEntry._ID,
            ContactEntry.COLUMN_NAME_NAME,
            ContactEntry.COLUMN_NAME_IMG_URL,
            ContactEntry.COLUMN_NAME_AGE,
            ContactEntry.COLUMN_NAME_DESCRIPTION
    };

    public ContactDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        if(DATABASE_VERSION != 2)
            database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(ContactDbHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + ContactEntry.TABLE_NAME);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public long insert(Contact contact){
        // Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ContactEntry.COLUMN_NAME_NAME, contact.getName());
        values.put(ContactEntry.COLUMN_NAME_IMG_URL, contact.getImgUrl());
        values.put(ContactEntry.COLUMN_NAME_AGE, contact.getAge());
        values.put(ContactEntry.COLUMN_NAME_DESCRIPTION, contact.getDescription());

        // Insert the new row, returning the primary key value of the new row
        return  db.insert(ContactEntry.TABLE_NAME,null,values);

    }

    public Cursor get(){
        SQLiteDatabase db = getReadableDatabase();
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String sorting = null;
        if(SORT_ON_NAME){
            sorting = ContactEntry.COLUMN_NAME_NAME + " ASC";
        }
        Cursor cursor = db.query(
                ContactEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sorting                                 // The sort order
        );
        return cursor;
    }

    public Cursor getAndSort(){
        SQLiteDatabase db = getReadableDatabase();
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.

        Cursor cursor = db.query(
                ContactEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                ContactEntry.COLUMN_NAME_NAME + " ASC"                                 // The sort order
        );
        return cursor;
    }

    public Contact getItem(int id) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                ContactEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                ContactEntry._ID + "=" + id,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );
        cursor.moveToFirst();

         String name = cursor.getString(cursor.getColumnIndex(ContactEntry.COLUMN_NAME_NAME));
         String imgUrl = cursor.getString(cursor.getColumnIndex(ContactEntry.COLUMN_NAME_IMG_URL));
         String description = cursor.getString(cursor.getColumnIndex(ContactEntry.COLUMN_NAME_DESCRIPTION));
         int age = cursor.getInt(cursor.getColumnIndex(ContactEntry.COLUMN_NAME_AGE));
        Contact contact = new Contact(imgUrl, name, description, age);
        return contact;
    }
    public boolean delete(int id){
        SQLiteDatabase db = getWritableDatabase();

        return db.delete(ContactEntry.TABLE_NAME, ContactEntry._ID + "=" + id, null) > 0;
    }

    public boolean update(Contact contact, int contactId){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ContactEntry.COLUMN_NAME_NAME, contact.getName());
        values.put(ContactEntry.COLUMN_NAME_IMG_URL, contact.getImgUrl());
        values.put(ContactEntry.COLUMN_NAME_AGE, contact.getAge());
        values.put(ContactEntry.COLUMN_NAME_DESCRIPTION, contact.getDescription());

        return db.update(ContactEntry.TABLE_NAME, values, ContactEntry._ID + "=" + contactId , null) > 0;
    }

    public void toggleSorting() {
        if(SORT_ON_NAME){
            SORT_ON_NAME = false;
        }else{
            SORT_ON_NAME = true;
        }
    }
}