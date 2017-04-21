package ru.turpattaya.yandextranslate;


        import android.content.Context;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteHelper extends SQLiteOpenHelper {

    private static final String HISTORY_DATABASE = "history.db";
    private static final  int HISTORY_DB_VERSION = 1;

    public MySQLiteHelper(Context context) {
        super(context, HISTORY_DATABASE, null, HISTORY_DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        HistoryTable.onCreate(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        HistoryTable.onUpgrade(sqLiteDatabase, i, i1);
    }
}