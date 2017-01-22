package rnrecord.commands;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.JavaOnlyArray;
import com.facebook.react.bridge.ReactApplicationContext;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.RobolectricTestRunner;
import rnrecord.ArgumentsMockHelper;
import rnrecord.AwaitablePromise;
import rnrecord.RnRecordSQLiteHelper;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by reweber on 22/01/2017
 */
@PrepareForTest({Arguments.class})
@RunWith(RobolectricTestRunner.class)
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*"})
public class FindAllCommandTest {

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Mock
    ReactApplicationContext reactContext;

    @Mock
    RnRecordSQLiteHelper rnRecordSQLiteHelper;

    @Mock
    SQLiteDatabase sqLiteDatabase;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ArgumentsMockHelper.mockArgumentsClass();
        when(rnRecordSQLiteHelper.getWritableDatabase()).thenReturn(sqLiteDatabase);
    }

    @Test
    public void shouldFindAllDbEntries() throws Exception {
        String tableName  = "testTable";
        AwaitablePromise<JavaOnlyArray> awaitablePromise = new AwaitablePromise<>();
        Cursor cursor = mockCursor();
        when(sqLiteDatabase.rawQuery("select * from " + tableName, null)).thenReturn(cursor);

        FindAllCommand findAllCommand = new FindAllCommand(reactContext, rnRecordSQLiteHelper);
        findAllCommand.findAll(tableName, awaitablePromise.promise());
        JavaOnlyArray result = awaitablePromise.awaitResolve();
        assertThat(result.size(), is(1));
        assertThat(result.getMap(0).getString("name"), is("rob"));
    }

    private Cursor mockCursor() {
        Cursor cursor = mock(Cursor.class);
        when(cursor.moveToNext()).thenReturn(true).thenReturn(false);
        when(cursor.getColumnName(0)).thenReturn("name");
        when(cursor.getColumnCount()).thenReturn(1);
        when(cursor.getString(0)).thenReturn("rob");

        return cursor;
    }
}