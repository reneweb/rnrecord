package rnrecord.commands;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import com.facebook.react.bridge.GuardedAsyncTask;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.ReadableType;
import rnrecord.RnRecordSQLiteHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by reweber on 05/01/2017
 */
public class SaveCommand {

    private ReactContext reactContext;

    public SaveCommand(ReactContext reactContext) {
        this.reactContext = reactContext;
    }

    public void save(final String tableName, ReadableMap props, final Promise promise) {
        List<String> keys = getPropKeys(props);

        final String createQuery = buildCreateQuery(props, keys).toString();
        final ArrayList<String> createQueryArguments = buildCreateQueryArgument(tableName, keys);

        final ContentValues contentValues = prepareInsertContentValues(props, keys);

        executeDbCallsAsync(tableName, promise, createQuery, createQueryArguments, contentValues);
    }

    private void executeDbCallsAsync(final String tableName, final Promise promise, final String createQuery, final ArrayList<String> createQueryArguments, final ContentValues contentValues) {
        new GuardedAsyncTask(reactContext) {
            @Override
            protected void doInBackgroundGuarded(Object[] params) {
                SQLiteDatabase db = RnRecordSQLiteHelper
                        .getInstance(reactContext)
                        .getWritableDatabase();

                db.execSQL(createQuery, createQueryArguments.toArray());
                promise.resolve(db.insert(tableName, null, contentValues));

            }
        }.execute();
    }

    private ContentValues prepareInsertContentValues(ReadableMap props, List<String> keys) {
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

    private ArrayList<String> buildCreateQueryArgument(String tableName, List<String> keys) {
        final ArrayList<String> arguments = new ArrayList<>();
        arguments.add(tableName);
        arguments.addAll(keys);
        return arguments;
    }

    private StringBuilder buildCreateQuery(ReadableMap props, List<String> keys) {
        final StringBuilder queryBuilder = new StringBuilder("CREATE TABLE IF NOT EXISTS ? ");
        queryBuilder.append("(");
        queryBuilder.append("id INTEGER ");
        for (String key : keys) {
            String propType = null;
            if(props.getType(key) == ReadableType.Boolean) {
                propType = "BOOLEAN";
            } else if(props.getType(key) == ReadableType.String) {
                propType = "TEXT";
            } else if(props.getType(key) == ReadableType.Number) {
                propType = "DOUBLE";
            }

            if(propType != null) {
                queryBuilder.append(", ? ").append(propType).append(", ");
            }
        }
        queryBuilder.append(")");
        return queryBuilder;
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
