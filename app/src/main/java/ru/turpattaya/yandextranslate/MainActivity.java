package ru.turpattaya.yandextranslate;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setHomeButtonEnabled(false);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);


        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_main_translate:
                Intent intent = new Intent(this, HistoryAndFavoriteActivity.class);
                startActivity(intent);
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
                return true;
        }*/
        return super.onOptionsItemSelected(item);
    }
}
