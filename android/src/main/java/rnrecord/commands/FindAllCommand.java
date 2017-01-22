package rnrecord.commands;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.GuardedAsyncTask;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import rnrecord.RnRecordSQLiteHelper;

/**
 * Created by reweber on 07/01/2017
 */
public class FindAllCommand {
    private ReactApplicationContext reactContext;
    private RnRecordSQLiteHelper rnRecordSQLiteHelper;

    public FindAllCommand(ReactApplicationContext reactContext, RnRecordSQLiteHelper rnRecordSQLiteHelper) {
        this.reactContext = reactContext;
        this.rnRecordSQLiteHelper = rnRecordSQLiteHelper;
    }

    public void findAll(String tableName, Promise promise) {
        executeDbCallsAsync(tableName, promise);
    }

    private void executeDbCallsAsync(final String tableName, final Promise promise) {
        new GuardedAsyncTask(reactContext) {
            @Override
            protected void doInBackgroundGuarded(Object[] params) {
                SQLiteDatabase db = rnRecordSQLiteHelper.getWritableDatabase();

                promise.resolve(transformQueryResults(db.rawQuery("select * from " + tableName, null)));

            }
        }.execute();
    }

    private ReadableArray transformQueryResults(Cursor cursor) {
        WritableArray result = Arguments.createArray();
        while(cursor.moveToNext()) {
            WritableMap row = Arguments.createMap();
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                row.putString(cursor.getColumnName(i), cursor.getString(i));
            }
            result.pushMap(row);
        }

        cursor.close();

        return result;
    }
}
