package rnrecord.commands;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import com.facebook.react.bridge.JavaOnlyMap;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import rnrecord.AwaitablePromise;
import rnrecord.RnRecordSQLiteHelper;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Created by reweber on 22/01/2017
 */
@RunWith(RobolectricTestRunner.class)
public class UpdateCommandTest {
    @Mock
    ReactApplicationContext reactContext;

    @Mock
    RnRecordSQLiteHelper rnRecordSQLiteHelper;

    @Mock
    SQLiteDatabase sqLiteDatabase;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(rnRecordSQLiteHelper.getWritableDatabase()).thenReturn(sqLiteDatabase);
    }

    @Test
    public void shouldUpdateDbEntry() throws Exception {
        String tableName  = "testTable";
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", "rob");
        contentValues.put("id", 1D);
        ReadableMap readableMap = JavaOnlyMap.of("id", 1D, "name", "rob");
        AwaitablePromise<Integer> awaitablePromise = new AwaitablePromise<>();
        when(sqLiteDatabase.update(tableName, contentValues, "id = ?", new String[] {"1.0"})).thenReturn(1);

        UpdateCommand updateCommand = new UpdateCommand(reactContext, rnRecordSQLiteHelper);
        updateCommand.update(tableName, readableMap, awaitablePromise.promise());
        Integer updateCount = awaitablePromise.awaitResolve();
        assertThat(updateCount, is(1));
    }
}