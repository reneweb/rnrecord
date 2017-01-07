package rnrecord.commands;

import android.database.sqlite.SQLiteDatabase;
import com.facebook.react.bridge.GuardedAsyncTask;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableMap;
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

                promise.resolve(db.rawQuery("select * from ?", new String[] {tableName} ));

            }
        }.execute();
    }
}
