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

import se.bachstatter.contactdblabb4.models.Contact;

import static se.bachstatter.contactdblabb4.data.contracts.ContactContract.*;

/**
 * Created by Jocek on 2014-09-17.
 */
public class ContactDbHelper extends SQLiteOpenHelper {
    /**
     * Class Consonants
     */
    private static final String TEXT_TYPE= " text";
    private static final String CREATE_MSG= "Table created version ";
    private static final String INT= " integer";
    private static final String COMMA= ", ";
    private static final String COLUMN_WHERE_END = "=?";
    private static final String SORT_ASC = " ASC";
    private static final String DATABASE_NAME = "ContactsDb.db";
    private static final int DATABASE_VERSION = 5;
    private static boolean SORT_ON_NAME = false ;


    /**
     *  Create db sql statement
     */
    private static final String DATABASE_CREATE = "create table "
            + ContactEntry.TABLE_NAME + "(" +
            ContactEntry._ID + " integer primary key, " +
            ContactEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA +
            ContactEntry.COLUMN_NAME_IMG_URL + TEXT_TYPE + COMMA +
            ContactEntry.COLUMN_NAME_AGE + INT + COMMA +
            ContactEntry.COLUMN_NAME_DESCRIPTION + TEXT_TYPE +
            ");";
    /**
     * Projection for all of the columns
     */
    private static String[] projection = {
            ContactEntry._ID,
            ContactEntry.COLUMN_NAME_NAME,
            ContactEntry.COLUMN_NAME_IMG_URL,
            ContactEntry.COLUMN_NAME_AGE,
            ContactEntry.COLUMN_NAME_DESCRIPTION
    };

    /**
     * Database delete SQL statement
     */
    private static final String SQL_DELETE_CONTACT =
            "DROP TABLE IF EXISTS " + ContactEntry.TABLE_NAME;

    /**
     * DBhelper constructor
     * We send context, db name, null for cursorfactory and the current db version
     * to SQLiteOpenHelper constructor
     *
     * @param context
     */
    public ContactDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * On create try to execute DATABASE_CREATE statement
     * If it works run seedData() and Log out the current db version.
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            db.execSQL(DATABASE_CREATE);
            seedData(db);

            Log.d(getClass().getName(), CREATE_MSG + DATABASE_VERSION);
        }
        catch (Exception ex){
            Log.d(getClass().getName(), ex.getMessage());
        }
    }

    /**
     * Called when the database needs to be upgraded.
     * We check that the new version isnt the same as the old.
     * Then delete the old one and run onCreate again.
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion != oldVersion){
            try{
                db.execSQL(SQL_DELETE_CONTACT);
                onCreate(db);
            }
            catch (Exception ex){
                Log.d(getClass().getName(), ex.getMessage());
            }
        }
    }

    /**
     * Called when the database needs to be downgraded. This is strictly similar to
     * onUpgrade method, but is called whenever current version is newer than requested one.
     * We run onUpgrade to downgrade to the old version.
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    /**
     * Inserts a new contact to db.
     * get a WritableDatabase, create a new ContentValues with the values we want to insert.
     * Run insert on the WritableDatabase, with table name, null for columnHack and the values
     *
     * @param contact
     * @return the row ID of the newly inserted row, or -1 if an error occurred
     */
    public long insert(Contact contact){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ContactEntry.COLUMN_NAME_NAME, contact.getName());
        values.put(ContactEntry.COLUMN_NAME_IMG_URL, contact.getImgUrl());
        values.put(ContactEntry.COLUMN_NAME_AGE, contact.getAge());
        values.put(ContactEntry.COLUMN_NAME_DESCRIPTION, contact.getDescription());

        return db.insert(ContactEntry.TABLE_NAME,null,values);
    }

    /**
     * Get a readable db.
     * Set sorting to null or ,if SORT_ON_NAME is true, sort on name in ascending order.
     * Run query on db with the table name, our static projection and null for all other args.
     * except if sorting is on.
     *
     * @return A Cursor object, which is positioned before the first entry.
     */
    public Cursor get(){
        SQLiteDatabase db = getReadableDatabase();
        String sorting = null;
        if(SORT_ON_NAME){
            sorting = ContactEntry.COLUMN_NAME_NAME + SORT_ASC;
        }
        return db.query(
                ContactEntry.TABLE_NAME,             // The table to query
                projection,                          // The columns to return
                null,                                // The columns for the WHERE clause
                null,                                // The values for the WHERE clause
                null,                                // don't group the rows
                null,                                // don't filter by row groups
                sorting                              // The sort order
        );
    }

    /**
     * Get a readable db.
     * save id as a String array.
     * Run query on db with the table name, our static projection and set columns for where to _ID
     * and values for where clause to id
     *
     * If movetofirst is true. Get all values from cursor, create a contact and return it.
     * else retrun null.
     *
     * @param id
     * @return Contact
     */
    public Contact getItem(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                ContactEntry.TABLE_NAME,                  // The table to query
                projection,                               // The columns to return
                ContactEntry._ID + COLUMN_WHERE_END,      // The columns for the WHERE clause
                idToArray(id),                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                      // The sort order
        );
        if (cursor.moveToFirst()) {
            return new Contact(
                    cursor.getString(cursor.getColumnIndex(ContactEntry.COLUMN_NAME_IMG_URL)),
                    cursor.getString(cursor.getColumnIndex(ContactEntry.COLUMN_NAME_NAME)),
                    cursor.getString(cursor.getColumnIndex(ContactEntry.COLUMN_NAME_DESCRIPTION)),
                    cursor.getInt(cursor.getColumnIndex(ContactEntry.COLUMN_NAME_AGE))
            );
        }
        return null;
    }

    /**
     * Get writable db and run delete on it, with table name, the _ID as column for the where clause
     * and the array id as where argument
     *
     * @param id
     * @return the number of rows affected if a whereClause is passed in, 0 otherwise.
     */
    public int delete(int id){
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(ContactEntry.TABLE_NAME, ContactEntry._ID + COLUMN_WHERE_END, idToArray(id));
    }

    /**
     * Updates a contact.
     * get a WritableDatabase, create a new ContentValues with the values we want to update.
     * Run update on the WritableDatabase, with table name, null for columnHack and the values.
     *
     * @param contact
     * @param id
     * @return the number of rows affected
     */
    public int update(Contact contact, int id){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ContactEntry.COLUMN_NAME_NAME, contact.getName());
        values.put(ContactEntry.COLUMN_NAME_IMG_URL, contact.getImgUrl());
        values.put(ContactEntry.COLUMN_NAME_AGE, contact.getAge());
        values.put(ContactEntry.COLUMN_NAME_DESCRIPTION, contact.getDescription());

        return db.update(ContactEntry.TABLE_NAME, values, ContactEntry._ID + COLUMN_WHERE_END, idToArray(id));
    }

    /**
     * Convert int id to String array
     *
     * @param id
     * @return String[]
     */
    private String[] idToArray(int id){
        return new String[] {String.valueOf(id)};
    }

    /**
     * ToggleSorting toggles the true or false on SORT_ON_NAME
     */
    public void toggleSorting() {
        if(SORT_ON_NAME){
            SORT_ON_NAME = false;
        }else{
            SORT_ON_NAME = true;
        }
    }

    /**
     * seedData gets called when database is created and inserts two contacts.
     * See insert method for comments.
     *
     * @param db
     */
    private void seedData(SQLiteDatabase db) {
        Contact contact = new Contact("https://fbcdn-sphotos-b-a.akamaihd.net/hphotos-ak-xpa1/v/t1.0-9/10325400_10152110180717896_2268770918259889556_n.jpg?oh=f56b8b4f0425573484f23f30f441f7fc&oe=5496A104&__gda__=1418816786_2ff62edef154eb1e3df7b0f23bacfe13", "Linn Sandström", "En snäll pys", 25);

        ContentValues values = new ContentValues();
        values.put(ContactEntry.COLUMN_NAME_NAME, contact.getName());
        values.put(ContactEntry.COLUMN_NAME_IMG_URL, contact.getImgUrl());
        values.put(ContactEntry.COLUMN_NAME_AGE, contact.getAge());
        values.put(ContactEntry.COLUMN_NAME_DESCRIPTION, contact.getDescription());
        db.insert(ContactEntry.TABLE_NAME,null,values);

        Contact contact1 = new Contact("http://developer.fortnox.se/wp-content/uploads/2014/03/nakleh_jocke.jpg", "Jocke", "trevligt typ", 25);

        ContentValues values1 = new ContentValues();
        values1.put(ContactEntry.COLUMN_NAME_NAME, contact1.getName());
        values1.put(ContactEntry.COLUMN_NAME_IMG_URL, contact1.getImgUrl());
        values1.put(ContactEntry.COLUMN_NAME_AGE, contact1.getAge());
        values1.put(ContactEntry.COLUMN_NAME_DESCRIPTION, contact1.getDescription());
        db.insert(ContactEntry.TABLE_NAME,null,values1);
    }
}