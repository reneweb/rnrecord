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
public class DeleteCommand {
    private ReactApplicationContext reactContext;
    private RnRecordSQLiteHelper rnRecordSQLiteHelper;

    public DeleteCommand(ReactApplicationContext reactContext, RnRecordSQLiteHelper rnRecordSQLiteHelper) {
        this.reactContext = reactContext;
        this.rnRecordSQLiteHelper = rnRecordSQLiteHelper;
    }

    public void delete(String tableName, ReadableMap props, Promise promise) {
        int id = props.getInt("id");
        executeDbCallsAsync(tableName, promise, id);
    }

    private void executeDbCallsAsync(final String tableName, final Promise promise, final int id) {
        new GuardedAsyncTask(reactContext) {
            @Override
            protected void doInBackgroundGuarded(Object[] params) {
                SQLiteDatabase db = rnRecordSQLiteHelper.getWritableDatabase();

                promise.resolve(db.delete(tableName, "id = ?", new String[] {String.valueOf(id)} ));

            }
        }.execute();
    }
}
