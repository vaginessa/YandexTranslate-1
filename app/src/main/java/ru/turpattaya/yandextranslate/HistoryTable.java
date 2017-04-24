package ru.turpattaya.yandextranslate;

import android.database.sqlite.SQLiteDatabase;

public class HistoryTable {

    public static final String TABLE_HISTORY = "history";
    public static final String COLUMN_HISTORY_ID = "_id";
    public static final String COLUMN_HISTORY_TEXTIN = "textIn";
    public static final String COLUMN_HISTORY_TEXTOUT = "textOut";
    public static final String COLUMN_HISTORY_TRANSLATE_DIRECTION = "translateDirection";
    public static final String COLUMN_DIRECTION_IS_FAVORITE = "isFavorite";

    private static final String HISTORY_CREATE = "create table "
            + TABLE_HISTORY + "("
            + COLUMN_HISTORY_ID
            + " integer primary key autoincrement, "
            + COLUMN_HISTORY_TEXTIN
            + " text not null, "
            + COLUMN_HISTORY_TEXTOUT
            + " text not null, "
            + COLUMN_HISTORY_TRANSLATE_DIRECTION
            + " text not null, "
            + COLUMN_DIRECTION_IS_FAVORITE
            + " integer not null "
            + ");";

    public static void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(HISTORY_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
        onCreate(sqLiteDatabase);
    }
}
