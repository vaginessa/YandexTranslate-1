package ru.turpattaya.yandextranslate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextView inLanguage;
    private TextView outLanguage;

    private EditText etIn;
    private TextView tvOut;

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

        etIn = (EditText) findViewById(R.id.main_edit_text_in);
        tvOut = (TextView) findViewById(R.id.main_text_out);
        inLanguage = (TextView) findViewById(R.id.text_history_in_lang);
        outLanguage = (TextView) findViewById(R.id.text_history_out_lang);

        loadTranslate();

    }

    private void loadTranslate() {
        API api = new API();
        String croco = "Что это такое";
        api.requestTranslation(croco, new Callback<List<JsonTranslate>>() {
            @Override
            public void onResponse(Call<List<JsonTranslate>> call, Response<List<JsonTranslate>> response) {
                Log.d("Valo", "onResponse");
                if (response != null) {
                    tvOut.setText(String.valueOf(response.body()));
              /* CampaignAdapter adapter = new CampaignAdapter(CampaignsActivity.this, response.body());

                 listViewCampaigns.setAdapter(adapter);*/
                }
            }

            @Override
            public void onFailure(Call<List<JsonTranslate>> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("Valo", "onFailure " +  t.getMessage());
            }
        });
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
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;

            case R.id.menu_main_favorite:
                Intent intentFavorite = new Intent(this, HistoryAndFavoriteActivity.class);
                startActivity(intentFavorite);
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
