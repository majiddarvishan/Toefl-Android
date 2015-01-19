package company.majid.behnam.toeflibt;

/**
 * Created by Behnam on 12/18/2014.
 */

        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.SQLException;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;

/**
 * This class manages a connection to the database, providing
 * convenience methods to create/update/delete, and centralizing the
 * constants used in the database.
 *
 * It should be possible to adapt this class for common android/db applications
 * by changing the constants and a few methods.
 *
 * This class is released into the public domain, free for any purpose.
 * Nick Parlante 2011
 *
 */
public class TodoDB {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "accountDb1";
    public static final String DATABASE_TABLE = "todo";
    public static final String DATABASE_ACCOUNT_TABLE = "accounts1";
    // Field names -- use the KEY_XXX constants here and in
    // client code, so it's all consistent and checked at compile-time.

    public static final String KEY_ROWID = "_id";  // Android requires exactly this key name
    public static final int INDEX_ROWID = 0;

    public static final String KEY_USERNAME = "title";
    public static final int INDEX_USERNAME = 1;
    public static final String KEY_PASSWORD = "body";
    public static final int INDEX_PASSWORD = 2;

    public static final String[] KEYS_ALL =
            {TodoDB.KEY_ROWID, TodoDB.KEY_USERNAME, TodoDB.KEY_PASSWORD};


    private Context mContext;
    private SQLiteDatabase mDatabase;
    private TodoDBHelper mHelper;

    /**
     * Construct DB for this activity context.
     */
    public TodoDB(Context context) {
        mContext = context;
    }

    /**
     * Opens up a connection to the database. Do this before any operations.
     */
    public void open() throws SQLException {
        mHelper = new TodoDBHelper(mContext);
        mDatabase = mHelper.getWritableDatabase();
    }

    /**
     * Closes the database connection. Operations are not valid after this.
     */
    public void close() {
        mHelper.close();
        mHelper = null;
        mDatabase = null;
    }


    /**
     * Creates and inserts a new row using the given values.
     * Returns the rowid of the new row, or -1 on error.
     * todo: values should not include a rowid I assume.
     */
    public long createRow(ContentValues values) {
        return mDatabase.insert(DATABASE_ACCOUNT_TABLE, null, values);
    }

    /**
     * Updates the given rowid with the given values.
     * Returns true if there was a change (i.e. the rowid was valid).
     */
    public boolean updateRow(long rowId, ContentValues values) {
        return mDatabase.update(DATABASE_ACCOUNT_TABLE, values,
                TodoDB.KEY_ROWID + "=" + rowId, null) > 0;
    }

    /**
     * Deletes the given rowid.
     * Returns true if any rows were deleted (i.e. the id was valid).
     */
    public boolean deleteRow(long rowId) {
        return mDatabase.delete(DATABASE_ACCOUNT_TABLE,
                TodoDB.KEY_ROWID + "=" + rowId, null) > 0;
    }


    /**
     * Returns a cursor for all the rows. Caller should close or manage the cursor.
     */
    public Cursor queryAll() {
        String selectQuery = "SELECT * FROM " + DATABASE_ACCOUNT_TABLE;
        return mDatabase.rawQuery(selectQuery, null);
        /*
        return mDatabase.query(DATABASE_ACCOUNT_TABLE,
                KEYS_ALL,  // i.e. return all 4 columns
                null, null, null, null,
                TodoDB.KEY_USERNAME + " ASC"  // order-by, "DESC" for descending
        );*/

        // Could pass for third arg to filter in effect:
        // TodoDatabaseHelper.KEY_STATE + "=0"

        // query() is general purpose, here we show the most common usage.
    }

    /**
     * Returns a cursor for the given row id. Caller should close or manage the cursor.
     */
    public Cursor query(long rowId) throws SQLException {
        Cursor cursor = mDatabase.query(true, DATABASE_ACCOUNT_TABLE,
                KEYS_ALL,
                KEY_ROWID + "=" + rowId,  // select the one row we care about
                null, null, null, null, null);

        // cursor starts before first -- move it to the row itself.
        cursor.moveToFirst();
        return cursor;
    }

    /**
     * Creates a ContentValues hash for our data. Pass in to create/update.
     */
    public ContentValues createAccount(String userName, String password){
        ContentValues values = new ContentValues();
        values.put(TodoDB.KEY_USERNAME, userName);
        values.put(TodoDB.KEY_PASSWORD, password);
        return values;
    }

    // Helper for database open, create, upgrade.
    // Here written as a private inner class to TodoDB.
    private static class TodoDBHelper extends SQLiteOpenHelper {
        // SQL text to create table (basically just string or integer)
        private static final String DATABASE_CREATE =
                "create table " + DATABASE_ACCOUNT_TABLE + " (" +
                        TodoDB.KEY_ROWID + " integer primary key autoincrement, " +
                        TodoDB.KEY_USERNAME + " text not null, " +
                        TodoDB.KEY_PASSWORD + " text not null" +
                                //TodoDB.KEY_STATE + " integer " +
                        ");";

        // SQLITE does not have a complex type system, so although "done" is a boolean
        // to the app, here we store it as an integer with (0 = false)


        public TodoDBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        /** Creates the initial (empty) database. */
        @Override
        public void onCreate(SQLiteDatabase database) {
            database.execSQL(DATABASE_CREATE);
        }

        /** Called at version upgrade time, in case we want to change/migrate
         the database structure. Here we just do nothing. */
        @Override
        public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
            // we do nothing for this case
        }
    }
}
