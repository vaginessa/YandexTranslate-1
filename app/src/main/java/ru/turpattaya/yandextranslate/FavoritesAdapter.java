package ru.turpattaya.yandextranslate;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import ru.turpattaya.yandextranslate.DataBase.HistoryTable;
import ru.turpattaya.yandextranslate.DataBase.MySQLiteHelper;

import static ru.turpattaya.yandextranslate.DataBase.HistoryTable.COLUMN_DIRECTION_IS_FAVORITE;

/**
 * Created by MSI on 4/24/2017.
 */

public class FavoritesAdapter extends HistoryAdapter {

    public FavoritesAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    Cursor updateDBCursor(MySQLiteHelper helper)
    {
        return helper.getReadableDatabase().query(
                HistoryTable.TABLE_HISTORY, null, COLUMN_DIRECTION_IS_FAVORITE + "=1", null, null, null, null );
    }
}
