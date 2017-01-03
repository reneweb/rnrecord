package rnrecord;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by reweber on 03/01/2017
 */
public class RnRecordSQLiteHelper extends SQLiteOpenHelper {

    private static RnRecordSQLiteHelper mInstance = null;

    private static final String DATABASE_NAME = "rnrecord";
    private static final int DATABASE_VERSION = 1;

    public static RnRecordSQLiteHelper getInstance(Context ctx) {

        if (mInstance == null) {
            mInstance = new RnRecordSQLiteHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }

    private RnRecordSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            Log.w(RnRecordSQLiteHelper.class.getName(),
                    "Upgrading database from version " + oldVersion + " to "
                            + newVersion + ", which will destroy all old data"
            );
            db.execSQL("DROP DATABASE");
            onCreate(db);
        }
    }
}
