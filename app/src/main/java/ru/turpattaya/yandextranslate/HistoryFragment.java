package ru.turpattaya.yandextranslate;

import android.content.*;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

public class HistoryFragment extends Fragment {

    private HistoryFragmentAddFavoriteHost host;
    private ListView listHistory;
    private HistoryAdapter adapter;


    public HistoryFragment() {
    }



    // Активность, на которой будет размещен фрагмент, должна имплментить
    // этот интерфейс, чтобы фрагмент мог передавать ей данные
    public interface HistoryFragmentAddFavoriteHost {
        public void addToFavorite(int historyId, String historyTextIn, String historyTextOut,
                                  String historyTranslateDirection, int imageFavorite);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rowView = inflater.inflate(R.layout.fragment_history, container, false);

        listHistory = (ListView) rowView.findViewById(R.id.list_history);



        populateHistoryFragment();

        return rowView;
    }


    private void populateHistoryFragment() {
        MySQLiteHelper helper = new MySQLiteHelper(getContext());
        Cursor cursor = helper.getReadableDatabase().query(
                HistoryTable.TABLE_HISTORY, null, null, null, null, null, null );
        HistoryAdapter adapter = new HistoryAdapter(getContext(), cursor);
        listHistory.setAdapter(adapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        adapter = new HistoryAdapter(context, null);
        /*listHistory.setAdapter(adapter);*/
        /*setListAdapter(adapter);*/
        /*updateCursorInAdapter();*/
        Log.d("valo", "HistoryFragment onAttach");

        if (context instanceof HistoryFragmentAddFavoriteHost)
            host = (HistoryFragmentAddFavoriteHost) context;
        else
            throw new RuntimeException(context.toString()
                    + " must implement HistoryFragmentAddFavoriteHost (ArticleListFragmentHost!)");
    }

    // Метод жизненного цикла фрагмента.
    // Присваиваем листу нулевой адаптер
    // Заменяем в адаптере курсор на null
    // зануляем адаптер
    // обнуляем ссылку на активность,
    @Override
    public void onDetach() {
        super.onDetach();
       /* adapter.swapCursor(null);
        adapter = null;*/
        host = null;
        Log.d("happy", "HistoryFragment onDetach");
    }
}

 /* // Метод для получения курсора
    private Cursor getHistoryCursor() {

        ContentResolver resolver = getContext().getContentResolver();
        return resolver.query(
                MyContentProvider.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    private void updateCursorInAdapter() {
        Cursor cursor = getHistoryCursor();
        adapter.swapCursor(cursor);
    }*/