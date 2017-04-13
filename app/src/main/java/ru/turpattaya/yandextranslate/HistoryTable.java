package ru.turpattaya.yandextranslate;

import android.database.sqlite.SQLiteDatabase;

public class HistoryTable {



    public static final String TABLE_HISTORY = "history";
    public static final String COLUMN_HISTORY_ID = "_id";
    public static final String COLUMN_HISTORY_TEXTIN = "textIn";
    public static final String COLUMN_HISTORY_TEXTOUT = "textOut";
    public static final String COLUMN_HISTORY_TRANSLATE_DIRECTION = "translateDirection";
    /*public static final int DIRECTION_IS_FAVORITE = 0;*/


    private static final String HISTORY_CREATE = "create table "
            + TABLE_HISTORY + "("
            + COLUMN_HISTORY_ID
            + " integer primary key autoincrement, "
            + COLUMN_HISTORY_TEXTIN
            + " text not null, "
            + COLUMN_HISTORY_TEXTOUT
            + " text not null, "
            + COLUMN_HISTORY_TRANSLATE_DIRECTION
            + " text not null "
           /* + DIRECTION_IS_FAVORITE
            + " integer, "*/
            + ");";

    public static void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(HISTORY_CREATE);
        populate(sqLiteDatabase);
    }

    private static void populate(SQLiteDatabase sqLiteDatabase)
    {

         sqLiteDatabase.execSQL("insert into history (textIn, textOut, translateDirection) values ("
                +"'привет',"
                +"'Hello',"
                +"'rus-eng'"
                +")");

        sqLiteDatabase.execSQL("insert into history (textIn, textOut, translateDirection) values ("
                +"'До свидания',"
                +"'Good buy',"
                +"'rus-eng'"
                +")");

        sqLiteDatabase.execSQL("insert into history (textIn, textOut, translateDirection) values ("
                +"'Computer',"
                +"'Компьютер',"
                +"'eng-rus'"
                +")");

        sqLiteDatabase.execSQL("insert into history (textIn, textOut, translateDirection) values ("
                +"'номер',"
                +"'number',"
                +"'rus-eng'"
                +")");


    }

    public static void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
        onCreate(sqLiteDatabase);
    }
}
