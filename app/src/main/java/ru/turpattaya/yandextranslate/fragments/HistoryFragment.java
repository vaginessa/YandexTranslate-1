package ru.turpattaya.yandextranslate.fragments;

import android.content.*;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import ru.turpattaya.yandextranslate.HistoryAdapter;
import ru.turpattaya.yandextranslate.DataBase.HistoryTable;
import ru.turpattaya.yandextranslate.DataBase.MySQLiteHelper;
import ru.turpattaya.yandextranslate.R;

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
        setHasOptionsMenu(true);
        listHistory = (ListView) rowView.findViewById(R.id.list_history);

        populateHistoryFragment();

        return rowView;
    }



    public void refreshView() {
        MySQLiteHelper helper = new MySQLiteHelper(getContext());
        Cursor cursor = helper.getReadableDatabase().query(
                HistoryTable.TABLE_HISTORY, null, null, null, null, null, null );

       adapter.changeCursor(cursor);
    }

    private void populateHistoryFragment() {
        MySQLiteHelper helper = new MySQLiteHelper(getContext());
        Cursor cursor = helper.getReadableDatabase().query(
                HistoryTable.TABLE_HISTORY, null, null, null, null, null, null );
        adapter = new HistoryAdapter(getContext(), cursor);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_history_and_favorite, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_delete:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
// TODO  ДОДЕЛАТЬ УДАЛЕНИЕ
                // set title
                alertDialogBuilder.setTitle("Your Title");

                // set dialog message
                alertDialogBuilder
                        .setMessage("Удалить все данные из истории?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                MySQLiteHelper helper = new MySQLiteHelper(getContext());
                                Cursor cursor = helper.getWritableDatabase().query(
                                        HistoryTable.TABLE_HISTORY,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null
                                );

                                helper.getWritableDatabase().delete(HistoryTable.TABLE_HISTORY, null, null);
                                helper.close();

                                HistoryAdapter adapter = new HistoryAdapter(getContext(), cursor);
                                listHistory.setAdapter(adapter);
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                dialog.dismiss();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

                return true;

        }
        return super.onOptionsItemSelected(item);
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