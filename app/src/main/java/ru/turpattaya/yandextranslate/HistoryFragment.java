package ru.turpattaya.yandextranslate;

import android.content.*;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class HistoryFragment extends Fragment {

    private HistoryFragmentHost host;
    private ListView listHistory;
    private MyCursorAdapter adapter;

    public HistoryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rowView = inflater.inflate(R.layout.fragment_history, container, false);

        listHistory = (ListView) rowView.findViewById(R.id.list_history);

        MySQLiteHelper helper = new MySQLiteHelper(getContext());

        Cursor cursor = helper.getReadableDatabase().query(
                HistoryTable.TABLE_HISTORY,
                null,
                null,
                null,
                null,
                null,
                null
        );

        MyCursorAdapter adapter = new MyCursorAdapter(getContext(), cursor);

        listHistory.setAdapter(adapter);

        return rowView;
    }


    // Активность, на которой будет размещен фрагмент, должна имплментить
    // этот интерфейс, чтобы фрагмент мог передавать ей данные
    public interface HistoryFragmentHost {
        public void historySelected(int historyId, String historyTextIn, String historyTextOut, String historyTranslateDirection);
    }

    // Метод для получения курсора
    private Cursor getHistoryCursor() {

        ContentResolver resolver = getContext().getContentResolver();
        return resolver.query(
                MyContentProvider.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    // Метод жизненного цикла фрагмента
    // Нужен чтобы получить ссылку на активность,
    // привести ее к интерфейсному типу
    // создать хелпер, адаптер, присвоить адаптер
    // и выполнить запрос к базе данных
   /* @Override
    public void onAttach(Context context) {
        super.onAttach(context);
       *//* helper = new MySQLiteHelper(context);*//*
        adapter = new MyCursorAdapter(context, null);

        updateCursorInAdapter();
        Log.d("happy", "ArticlesListFragment onAttach");

        if (context instanceof HistoryFragmentHost)
            host = (HistoryFragmentHost) context;
        else
            throw new RuntimeException(context.toString()
                    + " must implement HistoryFragmentHost!");
    }*/

    // Получем новый курсор.
    // Заменяем курсор в адаптере.

    private void updateCursorInAdapter() {
        Cursor cursor = getHistoryCursor();
        adapter.swapCursor(cursor);
    }

    // Метод жизненного цикла фрагмента.
    // Присваиваем листу нулевой адаптер
    // Заменяем в адаптере курсор на null
    // зануляем адаптер
    // закрываем хелпер
    // зануляем хелпер
    // обнуляем ссылку на активность,
  /*  @Override
    public void onDetach() {
        super.onDetach();
        adapter.swapCursor(null);
        adapter = null;
       *//* helper.close();
        helper = null;*//*
        host = null;
        Log.d("happy", "ArticlesListFragment onDetach");
    }*/
}


//досмотреть Андроид 2 часть 69 последние 8 минут

// Получение ссылки на ListView для регистрации щелчка
        /*listHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                               // Реакция на щелчок на статью в списке
                                               // Вызываем интерфейсный метод в активности
                                               @Override
                                               public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                   if (host != null) {
                                                       Cursor cursor = adapter.getCursor();
                                                       // Cursor cursor = getArticlesCursor();
                                                       cursor.moveToPosition(i);
                                                       float articleRating = cursor.getFloat(
                                                               cursor.getColumnIndex(ArticlesTable.COLUMN_ARTICLE_RATING)
                                                       );
                                                       int articleId = cursor.getInt(
                                                               cursor.getColumnIndex(ArticlesTable.COLUMN_ARTICLE_ID)
                                                       );
                                                       String articleURL = cursor.getString(
                                                               cursor.getColumnIndex(ArticlesTable.COLUMN_ARTICLE_URL)
                                                       );
                                                       if (host != null)
                                                           host.historySelected(articleId, articleURL, articleRating);
                                                   }

                                                   return rowView;
                                               }

                                           });*/
