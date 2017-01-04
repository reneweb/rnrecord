
package rnrecord;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;

public class RnRecordModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;

  public RnRecordModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "RnRecord";
  }

  @ReactMethod
  public void save(String tableName, ReadableMap props, Promise promise) {

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