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
public class UpdateCommand {
    private ReactApplicationContext reactContext;
    private RnRecordSQLiteHelper rnRecordSQLiteHelper;

    public UpdateCommand(ReactApplicationContext reactContext, RnRecordSQLiteHelper rnRecordSQLiteHelper) {
        this.reactContext = reactContext;
        this.rnRecordSQLiteHelper = rnRecordSQLiteHelper;
    }

    public void update(String tableName, ReadableMap props, Promise promise) {
        List<String> keys = getPropKeys(props);
        ContentValues contentValues = prepareUpdateContentValues(props, keys);
        executeDbCallsAsync(tableName, promise, contentValues);
    }

    private void executeDbCallsAsync(final String tableName, final Promise promise, final ContentValues contentValues) {
        new GuardedAsyncTask(reactContext) {
            @Override
            protected void doInBackgroundGuarded(Object[] params) {
                SQLiteDatabase db = rnRecordSQLiteHelper.getWritableDatabase();

                promise.resolve(db.update(tableName, contentValues, "id = ?", new String[] {contentValues.getAsString("id")} ));

            }
        }.execute();
    }

    private ContentValues prepareUpdateContentValues(ReadableMap props, List<String> keys) {
        final ContentValues contentValues = new ContentValues();
        for (String key : keys) {
            if(props.getType(key) == ReadableType.Boolean) {
                contentValues.put(key, props.getBoolean(key));
            } else if(props.getType(key) == ReadableType.Number) {
                contentValues.put(key, props.getDouble(key));
            } else if(props.getType(key) == ReadableType.String) {
                contentValues.put(key, props.getString(key));
            }
        }
        return contentValues;
    }

    private List<String> getPropKeys(ReadableMap props) {
        List<String> keys = new ArrayList<>();
        ReadableMapKeySetIterator iterator = props.keySetIterator();
        while(iterator.hasNextKey()) {
            keys.add(iterator.nextKey());
        }
        return keys;
    }
}
