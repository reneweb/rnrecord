package rnrecord.commands;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.facebook.react.bridge.GuardedAsyncTask;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.ReadableType;
import com.facebook.react.bridge.WritableNativeArray;
import rnrecord.RnRecordSQLiteHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by reweber on 07/01/2017
 */
public class FindCommand {
    private ReactApplicationContext reactContext;

    public FindCommand(ReactApplicationContext reactContext) {
        this.reactContext = reactContext;
    }

    public void find(String tableName, ReadableMap query, Promise promise) {
        List<String> queryKeys = getQueryKeys(query);
        String queryString = buildQuery(queryKeys).toString();
        ArrayList<String> queryArguments = buildQueryArgument(tableName, queryKeys, query);

        executeDbCallsAsync(queryString, queryArguments, promise);
    }

    private void executeDbCallsAsync(final String queryString, final ArrayList<String> queryArguments, final Promise promise) {
        new GuardedAsyncTask(reactContext) {
            @Override
            protected void doInBackgroundGuarded(Object[] params) {
                SQLiteDatabase db = RnRecordSQLiteHelper
                        .getInstance(reactContext)
                        .getWritableDatabase();

                promise.resolve(transformQueryResults(db.rawQuery(queryString, queryArguments.toArray(new String[queryArguments.size()]) )));

            }
        }.execute();
    }

    private ReadableArray transformQueryResults(Cursor cursor) {
        WritableNativeArray result = new WritableNativeArray();
        while(cursor.moveToNext()) {
            WritableNativeArray row = new WritableNativeArray();
            for (int i = 0; i < cursor.getCount(); i++) {
                row.pushString(cursor.getString(i));
            }
            result.pushArray(row);
        }

        cursor.close();

        return result;
    }

    private ArrayList<String> buildQueryArgument(String tableName, List<String> keys, ReadableMap query) {
        final ArrayList<String> arguments = new ArrayList<>();
        arguments.add(tableName);

        for (String key : keys) {
            ReadableType type = query.getType(key);
            if(type == ReadableType.Boolean) {
                arguments.add(String.valueOf(query.getBoolean(key)));
            } else if(type == ReadableType.Number) {
                arguments.add(String.valueOf(query.getDouble(key)));
            } else if(type == ReadableType.String) {
                arguments.add(String.valueOf(query.getString(key)));
            }
        }

        return arguments;
    }

    private StringBuilder buildQuery(List<String> keys) {
        final StringBuilder queryBuilder = new StringBuilder("SELECT * from ? WHERE ");
        for (int i = 0, keysSize = keys.size(); i < keysSize; i++) {
            String key = keys.get(i);
            queryBuilder.append(key).append(" = ? ");
            if(i < keys.size()) {
                queryBuilder.append("AND ");
            }
        }
        queryBuilder.append(";");
        return queryBuilder;
    }

    private List<String> getQueryKeys(ReadableMap query) {
        List<String> keys = new ArrayList<>();
        ReadableMapKeySetIterator iterator = query.keySetIterator();
        while(iterator.hasNextKey()) {
            keys.add(iterator.nextKey());
        }
        return keys;
    }
}
