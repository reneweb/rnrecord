package rnrecord.commands;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.GuardedAsyncTask;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.ReadableType;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import rnrecord.RnRecordSQLiteHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by reweber on 07/01/2017
 */
public class FindCommand {
    private ReactApplicationContext reactContext;
    private RnRecordSQLiteHelper rnRecordSQLiteHelper;

    public FindCommand(ReactApplicationContext reactContext, RnRecordSQLiteHelper rnRecordSQLiteHelper) {
        this.reactContext = reactContext;
        this.rnRecordSQLiteHelper = rnRecordSQLiteHelper;
    }

    public void find(String tableName, ReadableMap query, Promise promise) {
        List<String> queryKeys = getQueryKeys(query);
        String queryString = buildQuery(tableName, queryKeys).toString();
        ArrayList<String> queryArguments = buildQueryArgument(queryKeys, query);

        executeDbCallsAsync(queryString, queryArguments, promise);
    }

    private void executeDbCallsAsync(final String queryString, final ArrayList<String> queryArguments, final Promise promise) {
        new GuardedAsyncTask(reactContext) {
            @Override
            protected void doInBackgroundGuarded(Object[] params) {
                SQLiteDatabase db = rnRecordSQLiteHelper.getWritableDatabase();

                promise.resolve(transformQueryResults(db.rawQuery(queryString, queryArguments.toArray(new String[queryArguments.size()]) )));

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

    private ArrayList<String> buildQueryArgument(List<String> keys, ReadableMap query) {
        final ArrayList<String> arguments = new ArrayList<>();

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

    private StringBuilder buildQuery(String tableName, List<String> keys) {
        final StringBuilder queryBuilder = new StringBuilder("SELECT * from " + tableName + " WHERE ");
        for (int i = 0, keysSize = keys.size(); i < keysSize; i++) {
            String key = keys.get(i);
            queryBuilder.append(key).append(" = ? ");
            if(i < keys.size() - 1) {
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
