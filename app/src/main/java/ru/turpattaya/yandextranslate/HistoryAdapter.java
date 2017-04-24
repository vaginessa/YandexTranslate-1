package ru.turpattaya.yandextranslate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ru.turpattaya.yandextranslate.DataBase.HistoryTable;
import ru.turpattaya.yandextranslate.DataBase.MySQLiteHelper;
//Непонятно что делать с иконкой избранное , то ли здесь ее как-то заенять, то-ли новый адаптер для избранного создавать, а потом в коде как то

public class HistoryAdapter extends CursorAdapter {



    public HistoryAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View row = LayoutInflater.from(context).inflate(R.layout.item_fragment_history, viewGroup, false);
        ViewHolderItem holder = new ViewHolderItem();
        holder.imageFavoriteIcon = (ImageView) row.findViewById(R.id.image_history_icon);
        holder.imageFavoriteIcon.setBackgroundResource(R.drawable.ic_not_favorite_24dp);
        holder.textInHistory = (TextView) row.findViewById(R.id.text_history_in_lang);
        holder.textOutHistory = (TextView) row.findViewById(R.id.text_history_out_lang);
        holder.textTranslationDirectionHistory = (TextView) row.findViewById(R.id.text_history_translation_direction);

        row.setTag(holder);

        return row;
    }

    Cursor updateDBCursor(MySQLiteHelper helper)
    {
        return helper.getReadableDatabase().query(
                HistoryTable.TABLE_HISTORY, null, null, null, null, null, null );
    }

    private void populateView(final ViewHolderItem holder, Cursor cursor, Context context) {
        holder.textInHistory.setText(cursor.getString(cursor.getColumnIndex(HistoryTable.COLUMN_HISTORY_TEXTIN)));
        holder.textOutHistory.setText(cursor.getString(cursor.getColumnIndex(HistoryTable.COLUMN_HISTORY_TEXTOUT)));
        holder.textTranslationDirectionHistory.setText(cursor.getString(cursor.getColumnIndex(HistoryTable.COLUMN_HISTORY_TRANSLATE_DIRECTION)));
        holder.isFavorite = false;
        holder.parentAdapter = this; /////
        int isFavoriteInt = cursor.getInt(cursor.getColumnIndex(HistoryTable.COLUMN_DIRECTION_IS_FAVORITE));
        if (isFavoriteInt==1) {
            holder.isFavorite = true;
        }

        // holderItem.parentAdapter.notifyDataSetChanged();   //// не перерисовывает экран
        if (holder.isFavorite) {
            holder.imageFavoriteIcon.setImageResource(R.drawable.ic_in_favorite_24dp);
        } else {
            holder.imageFavoriteIcon.setImageResource(R.drawable.ic_not_favorite_24dp);
        }

        final int id = cursor.getInt(cursor.getColumnIndex(HistoryTable.COLUMN_HISTORY_ID));

        holder.id = id;
        holder.imageFavoriteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewHolderItem holderItem =  holder;
                Log.d("Valo", "got it! -> " + holderItem.id);

                holderItem.isFavorite = !holderItem.isFavorite;

                MySQLiteHelper helper = new MySQLiteHelper(v.getContext());
                ContentValues values = new ContentValues();

                values.put(HistoryTable.COLUMN_DIRECTION_IS_FAVORITE, holderItem.isFavorite ? 1:0);
                helper.getWritableDatabase().update(HistoryTable.TABLE_HISTORY, values, HistoryTable.COLUMN_HISTORY_ID
                        +" = '"+ holderItem.id + "'", null);


                Cursor cursor = updateDBCursor(helper);

                changeCursor(cursor);

            }
        });
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolderItem holder = (ViewHolderItem) view.getTag();
        populateView(holder, cursor, context);
    }
}

