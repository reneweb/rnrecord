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
public class SaveCommandTest {
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
    public void shouldSaveDbEntry() throws Exception {
        String tableName  = "testTable";
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", "rob");
        AwaitablePromise<Double> awaitablePromise = new AwaitablePromise<>();
        when(sqLiteDatabase.insert(tableName, null, contentValues)).thenReturn(1L);

        SaveCommand saveCommand = new SaveCommand(reactContext, rnRecordSQLiteHelper);
        saveCommand.save(tableName, JavaOnlyMap.of("name", "rob"), awaitablePromise.promise());
        Double insertCount = awaitablePromise.awaitResolve();
        assertThat(insertCount, is(1D));
    }
}