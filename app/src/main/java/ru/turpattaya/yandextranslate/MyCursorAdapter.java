package ru.turpattaya.yandextranslate;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
//Непонятно что делать с иконкой избранное , то ли здесь ее как-то заенять, то-ли новый адаптер для избранного создавать, а потом в коде как то

public class MyCursorAdapter extends CursorAdapter {

    public MyCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View row = LayoutInflater.from(context).inflate(R.layout.item_fragment_history, viewGroup, false);
        ViewHolderItem holder = new ViewHolderItem();
        holder.imageFavoriteIcon = (ImageView) row.findViewById(R.id.image_history_icon);
        holder.textInHistory = (TextView) row.findViewById(R.id.text_history_in_lang);
        holder.textOutHistory = (TextView) row.findViewById(R.id.text_history_out_lang);
        holder.textTranslationDirectionHistory = (TextView) row.findViewById(R.id.text_history_translation_direction);

        row.setTag(holder);

        return row;
    }

    private void populateView(ViewHolderItem holder, Cursor cursor, Context context) {
        holder.textInHistory.setText(cursor.getString(cursor.getColumnIndex(HistoryTable.COLUMN_HISTORY_TEXTIN)));
        holder.textOutHistory.setText(cursor.getString(cursor.getColumnIndex(HistoryTable.COLUMN_HISTORY_TEXTOUT)));
        holder.textTranslationDirectionHistory.setText(cursor.getString(cursor.getColumnIndex(HistoryTable.COLUMN_HISTORY_TRANSLATE_DIRECTION)));
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolderItem holder = (ViewHolderItem) view.getTag();
        populateView(holder, cursor, context);
    }
}

