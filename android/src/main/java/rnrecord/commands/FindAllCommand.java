package rnrecord.commands;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.facebook.react.bridge.GuardedAsyncTask;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableNativeArray;
import rnrecord.RnRecordSQLiteHelper;

/**
 * Created by reweber on 07/01/2017
 */
public class FindAllCommand {
    private ReactApplicationContext reactContext;

    public FindAllCommand(ReactApplicationContext reactContext) {
        this.reactContext = reactContext;
    }

    public void findAll(String tableName, Promise promise) {
        executeDbCallsAsync(tableName, promise);
    }

    private void executeDbCallsAsync(final String tableName, final Promise promise) {
        new GuardedAsyncTask(reactContext) {
            @Override
            protected void doInBackgroundGuarded(Object[] params) {
                SQLiteDatabase db = RnRecordSQLiteHelper
                        .getInstance(reactContext)
                        .getWritableDatabase();

                promise.resolve(transformQueryResults(db.rawQuery("select * from " + tableName, null)));

            }
        }.execute();
    }

    private ReadableArray transformQueryResults(Cursor cursor) {
        WritableNativeArray result = new WritableNativeArray();
        while(cursor.moveToNext()) {
            WritableNativeArray row = new WritableNativeArray();
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                row.pushString(cursor.getString(i));
            }
            result.pushArray(row);
        }

        cursor.close();

        return result;
    }
}
