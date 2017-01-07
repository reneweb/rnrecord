package rnrecord.commands;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import com.facebook.react.bridge.GuardedAsyncTask;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.ReadableType;
import rnrecord.RnRecordSQLiteHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by reweber on 07/01/2017
 */
public class DeleteCommand {
    private ReactApplicationContext reactContext;

    public DeleteCommand(ReactApplicationContext reactContext) {
        this.reactContext = reactContext;
    }

    public void delete(String tableName, ReadableMap props, Promise promise) {
        int id = props.getInt("id");
        executeDbCallsAsync(tableName, promise, id);
    }

    private void executeDbCallsAsync(final String tableName, final Promise promise, final int id) {
        new GuardedAsyncTask(reactContext) {
            @Override
            protected void doInBackgroundGuarded(Object[] params) {
                SQLiteDatabase db = RnRecordSQLiteHelper
                        .getInstance(reactContext)
                        .getWritableDatabase();

                promise.resolve(db.delete(tableName, "id = ?", new String[] {String.valueOf(id)} ));

            }
        }.execute();
    }
}
