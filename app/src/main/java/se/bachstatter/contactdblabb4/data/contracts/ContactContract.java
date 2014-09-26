package se.bachstatter.contactdblabb4.data.contracts;

import android.provider.BaseColumns;

/**
 * Created by Jocek on 2014-09-24.
 */
public class ContactContract {
    /**
     * Empty constructor to prevent someone from accidentally instantiating the contract class,
     */
    public ContactContract() {}

    /**
     * Inner class that defines the tables contents.
     */
    public static abstract class ContactEntry implements BaseColumns {
        public static final String TABLE_NAME = "contacts";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_AGE = "age";
        public static final String COLUMN_NAME_IMG_URL = "imgUrl";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
    }
}
