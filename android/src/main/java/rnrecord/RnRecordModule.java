
package rnrecord;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import com.facebook.react.bridge.GuardedAsyncTask;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.ReadableType;
import rnrecord.commands.SaveCommand;

import java.util.ArrayList;
import java.util.List;

public class RnRecordModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;
  private final SaveCommand saveCommand;

  public RnRecordModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
    saveCommand = new SaveCommand(reactContext);
  }

  @Override
  public String getName() {
    return "RnRecord";
  }

  @ReactMethod
  public void save(final String tableName, ReadableMap props, final Promise promise) {
    saveCommand.save(tableName, props, promise);
  }

  @ReactMethod
  public void update(String tableName, ReadableMap props, Promise promise) {

  }

  @ReactMethod
  public void remove(String tableName, ReadableMap props, Promise promise) {

  }

  @ReactMethod
  public void findAll(String tableName, Promise promise) {

  }

  @ReactMethod
  public void find(String tableName, ReadableMap query, Promise promise) {

  }
}