package ru.turpattaya.yandextranslate.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.turpattaya.yandextranslate.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment {


    public FavoriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

}
 /*switch (item.getItemId()) {
         case R.id.category_islands:
         helper = new MySQLiteHelper(this);

         Cursor cursorSort = helper.getReadableDatabase().query(
         ExcursionTable.TABLE_EXCURSION,
         null,
         ExcursionTable.COLUMN_EXCURSION_CATEGORYID + " =?",
         new String[]{String.valueOf(1)},
         null,
         null,
         null,
         orderBy
         );
         ExcursionAdapter adapter = new ExcursionAdapter(this, cursorSort);
         listExcursion.setAdapter(adapter);

         return true;*/