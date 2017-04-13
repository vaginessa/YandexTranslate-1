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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static ru.turpattaya.yandextranslate.API.KEY_YANDEX_API;


public class MainActivity extends AppCompatActivity /*implements Callback*/ {

    private TextView inLanguageToolbar;
    private TextView outLanguageToolbar;
    private ImageView translateDirectionToolbar;

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
        inLanguageToolbar = (TextView) findViewById(R.id.main_toolbar_translate_from);
        outLanguageToolbar = (TextView) findViewById(R.id.main_toolbar_translate_to);
        translateDirectionToolbar = (ImageView) findViewById(R.id.main_toolbar_translate_direction);

        loadTranslate();

    }

    private void loadTranslate() {

        API api = new API(KEY_YANDEX_API);

        api.requestTranslation("Что это такое", "ru-en", new Callback<JsonTranslate>() {
            @Override
            public void onResponse(Call<JsonTranslate> call, Response<JsonTranslate> response) {
                Log.d("Valo", "onResponse");
                if (response != null) {
                    Log.d("happy", response.body().getText().get(0));
                    tvOut.setText(response.body().getText().get(0));

                }
            }

            @Override
            public void onFailure(Call<JsonTranslate> call, Throwable t) {
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

        return super.onOptionsItemSelected(item);
    }

  /*  @Override
    public void onResponse(Call call, Response response) {
        Object body = response.body();
        if(body instanceof JsonTranslate)
        Log.d("happy", "Dictionary");
    else if(body instanceof JsonTranslate)
        Log.d("happy", "Translate");
    else
        Log.d("happy", "wtf?");
    }

    @Override
    public void onFailure(Call call, Throwable t) {
        Log.d("valo", "onFailure wtf?");

    }*/
}
