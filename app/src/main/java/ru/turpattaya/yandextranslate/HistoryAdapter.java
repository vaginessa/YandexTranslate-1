package ru.turpattaya.yandextranslate;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HistoryAdapter extends CursorAdapter {


    public HistoryAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View row = LayoutInflater.from(context).inflate(R.layout.item_fragment_history, viewGroup, false);

        ViewHolderItem holder = new ViewHolderItem();
        holder.imageFavoriteIcon = (ImageView) row.findViewById(R.id.image_favor_icon);
        holder.textInLanguage = (TextView) row.findViewById(R.id.text_in_lang);
        holder.textOutLanguage = (TextView) row.findViewById(R.id.text_out_lang);
        holder.textTranslationDirection = (TextView) row.findViewById(R.id.text_translation_direction);

        row.setTag(holder);

        return row;

    }
    private void populateViewHistory(ViewHolderItem holder, Cursor cursor, final Context context, View view)
    {
        holder.textInLanguage.setText(cursor.getString(cursor.getColumnIndex(HistoryTable.COLUMN_HISTORY_TEXTIN)));
        holder.textOutLanguage.setText(cursor.getString(cursor.getColumnIndex(HistoryTable.COLUMN_HISTORY_TEXTOUT)));
        holder.textTranslationDirection.setText(cursor.getString(cursor.getColumnIndex(HistoryTable.COLUMN_HISTORY_TRANSLATE_DIRECTION)));

        //Непонятно как реализовать отбор избранных будет делаться модификацией where is_favorite='true'

        /*final long id = cursor.getLong(cursor.getColumnIndex(ExcursionTable.COLUMN_EXCURSION_ID));*/

       /* view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Valo", "got it! -> " + id);
                Intent intent = new Intent(context, ExcursionDetailActivity.class);
                intent.putExtra("id", id);
                context.startActivity(intent);
            }
        });*/

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolderItem holder = (ViewHolderItem) view.getTag();
        populateViewHistory(holder, cursor, context, view);
    }
}