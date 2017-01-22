package rnrecord.commands;

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
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

/**
 * Created by reweber on 22/01/2017
 */
@RunWith(RobolectricTestRunner.class)
public class DeleteCommandTest {

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
    public void shouldDeleteDbEntry() throws Exception {
        String tableName  = "testTable";
        ReadableMap readableMap = JavaOnlyMap.of("id", 1);
        AwaitablePromise<Integer> awaitablePromise = new AwaitablePromise<>();
        when(sqLiteDatabase.delete(tableName, "id = ?", new String[] {"1"})).thenReturn(1);

        DeleteCommand deleteCommand = new DeleteCommand(reactContext, rnRecordSQLiteHelper);
        deleteCommand.delete(tableName, readableMap, awaitablePromise.promise());
        Integer deleteCount = awaitablePromise.awaitResolve();
        assertThat(deleteCount, is(1));
    }
}