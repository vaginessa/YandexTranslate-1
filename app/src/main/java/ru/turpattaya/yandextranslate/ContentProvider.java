package ru.turpattaya.yandextranslate;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;


public class ContentProvider extends android.content.ContentProvider {

/*
    public static final String CONTENT_AUTHORITY = "com.nocompany.articlesflarmentpuresqlite.articles";
*/
    public static final String CONTENT_AUTHORITY = "ru.turpattaya.yandextranslate.history";


    public static final Uri CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY + "/history");

    public static final int ALL_ROWS = 1;
    public static final int SINGLE_ROW = 2;

    public static final UriMatcher matcher;
    static  {
        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(CONTENT_AUTHORITY, "history", ALL_ROWS);
        matcher.addURI(CONTENT_AUTHORITY, "history/#", SINGLE_ROW);
    }

    private MySQLiteHelper helper;

    @Override
    public boolean onCreate() {
        helper = new MySQLiteHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (matcher.match(uri)) {
            case ALL_ROWS:
                return "vnd.android.cursor.dir/articles";
            case SINGLE_ROW:
                return "vnd.android.cursor.item/articles";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = helper.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(HistoryTable.TABLE_HISTORY);
        switch (matcher.match(uri)) {
            case SINGLE_ROW:
                String rowID = uri.getPathSegments().get(1);
                builder.appendWhere(HistoryTable.TABLE_HISTORY+"="+rowID);
                break;
        }

    /*    db.rawQuery(sql запрос) можно сделатьи так код выше*/

        return builder.query(
                db,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase db = helper.getWritableDatabase();
        long id = db.insert(
                HistoryTable.TABLE_HISTORY,
                null,
                values
        );
        if (id >= 0) {
            Uri inserted = ContentUris.withAppendedId(CONTENT_URI, id);
            //content://.../articles/66
            getContext().getContentResolver().notifyChange(inserted, null);
            return inserted;
        } else
            return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = helper.getWritableDatabase();
        switch (matcher.match(uri)) {
            case SINGLE_ROW:
                String rowId = uri.getPathSegments().get(1);
                selection = HistoryTable.COLUMN_HISTORY_ID + "=" + rowId
                        + ( !TextUtils.isEmpty(selection) ? " AND (" +selection + " )" :"");
                //delete from articles where age > 12;
                //delete from articles where id = 444 and (age > 12)
                break;
        }
        int deleteCount = db.delete(
                HistoryTable.TABLE_HISTORY,
                selection,
                selectionArgs
        );
        if (deleteCount > 0) {
            getContext().getContentResolver().notifyChange(uri, null) ;
        }
        return deleteCount;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = helper.getWritableDatabase();
        switch (matcher.match(uri)) {
            case SINGLE_ROW:
                String rowId = uri.getPathSegments().get(1);
                selection = HistoryTable.COLUMN_HISTORY_ID + "=" + rowId
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection + " )" : "");
                //delete from articles where age > 12;
                //delete from articles where id = 444 and (age > 12)
                break;
        }
        int updateCount = db.update(
                HistoryTable.TABLE_HISTORY,
                values,
                selection,
                selectionArgs
        );
        if (updateCount > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return updateCount;
    }

/*    @NonNull
    @Override
    public ContentProviderResult[] applyBatch(@NonNull ArrayList<ContentProviderOperation> operations) throws OperationApplicationException {
        return super.applyBatch(operations);
    }*/

    /*    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        return super.bulkInsert(uri, values);
    }*/
}
