package ru.turpattaya.yandextranslate;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Timer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextView inLanguageToolbar;
    private TextView outLanguageToolbar;

    private EditText etIn;
    private TextView tvOut;

    String textForTranslate;

    SharedPreferences preferences;
    public static final String IN_LANG_TOOLBAR_PREF = "IN_LANG_TOOLBAR_PREF";
    public static final String OUT_LANG_TOOLBAR_PREF = "OUT_LANG_TOOLBAR_PREF";
    public static final String LANG_CODE_PREF = "LANG_CODE_PREF";
    public static final String IN_LANG_KEY = "IN_LANG_KEY";
    public static final String OUT_LANG_KEY = "OUT_LANG_KEY";

    String inLanguageToolbarPref;
    String outLanguageToolbarPref;
    String langCodePref;
    String inLangKey;
    String outLangKey;


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
        ImageView translateDirectionToolbar = (ImageView) findViewById(R.id.main_toolbar_translate_direction);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        inLanguageToolbarPref = preferences.getString("IN_LANG_TOOLBAR_PREF", null); //если что-то есть в preference , то мы это забираем
        outLanguageToolbarPref = preferences.getString("OUT_LANG_TOOLBAR_PREF", null);
        langCodePref = preferences.getString("LANG_CODE_PREF", null);
        inLangKey = preferences.getString("IN_LANG_KEY", null);
        outLangKey = preferences.getString("OUT_LANG_KEY", null);

        if (inLanguageToolbarPref != null && outLanguageToolbarPref != null && langCodePref != null) {
            inLanguageToolbar.setText(inLanguageToolbarPref);
            outLanguageToolbar.setText(outLanguageToolbarPref);
        } else {
            inLanguageToolbar.setText("английский");
            outLanguageToolbar.setText("русский");
            langCodePref = "en-ru";
            inLangKey = "en";
            outLangKey = "ru";
            inLanguageToolbarPref = inLanguageToolbar.getText().toString();
            outLanguageToolbarPref = outLanguageToolbar.getText().toString();
            saveDateToSharedPreference();
        }

        inLanguageToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InLanguageDialog dialog = new InLanguageDialog();
                dialog.show(getSupportFragmentManager(), "DialogLanguageIn");

            }
        });
        outLanguageToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OutLanguageDialog dialog = new OutLanguageDialog();
                dialog.show(getSupportFragmentManager(), "DialogLanguageOut");

            }
        });

        translateDirectionToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String changeLang = "";
                String getInLang = preferences.getString(IN_LANG_TOOLBAR_PREF, null);
                String getOutLang = preferences.getString(OUT_LANG_TOOLBAR_PREF, null);

                changeLang = getInLang;
                getInLang = getOutLang;
                getOutLang = changeLang;
                String changeKeys = "";
                changeKeys = inLangKey;
                inLangKey = outLangKey;
                outLangKey = changeKeys;
                inLanguageToolbar.setText(getInLang);
                outLanguageToolbar.setText(getOutLang);
                getPairLangsToRequest();
                saveDateToSharedPreference();
                loadTranslate();

            }
        });

        etIn.addTextChangedListener(new TextWatcher() {
            final Handler handler = new android.os.Handler();
            Runnable runnable;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler.removeCallbacks(runnable);
            }
            private Timer timer=new Timer();
            private final long DELAY = 1000; // milliseconds

            @Override
            public void afterTextChanged(Editable s) {
                runnable = new Runnable() {
                    @Override
                    public void run() {
                            loadTranslate();
                    }
                };
                handler.postDelayed(runnable, 500);
            }
        });
    }

    private void saveDateToSharedPreference() {
        String putSharedInLangToolbar = inLanguageToolbar.getText().toString();
        SharedPreferences.Editor editorinLanguageToolbarPref = preferences.edit().putString(IN_LANG_TOOLBAR_PREF, putSharedInLangToolbar);
        editorinLanguageToolbarPref.apply();
        String putSharedOutLangToolbar = outLanguageToolbar.getText().toString();
        SharedPreferences.Editor outLanguageToolbar = preferences.edit().putString(OUT_LANG_TOOLBAR_PREF, putSharedOutLangToolbar);
        outLanguageToolbar.apply();
        SharedPreferences.Editor editorlangCodePref = preferences.edit().putString(LANG_CODE_PREF, langCodePref);
        editorlangCodePref.apply();
        SharedPreferences.Editor inLangKeyPref = preferences.edit().putString(IN_LANG_KEY, inLangKey);
        inLangKeyPref.apply();
        SharedPreferences.Editor outLangKeyPref = preferences.edit().putString(OUT_LANG_KEY, outLangKey);
        outLangKeyPref.apply();
        /*Log.d("Valo", "shared null after put, IN_LANG_TOOLBAR_PREF= "+ IN_LANG_TOOLBAR_PREF + inLanguageToolbarPref+ " " +
                outLanguageToolbarPref+ " " +langCodePref+ " " + inLangKey+ " " + outLangKey);*/
    }

    private void loadTranslate() {
        API api = new API();
        textForTranslate = etIn.getText().toString();
            if (!textForTranslate.equals("")) {

                api.translate(textForTranslate, langCodePref, new Callback<JsonTranslate>() {
                    @Override
                    public void onResponse(Call<JsonTranslate> call, Response<JsonTranslate> response) {
                        Log.d("Valo", "onResponse");
                        if (response != null) {
                   /* Log.d("happy", response.body().getText().get(0));*/
                            tvOut.setText(response.body().getText().get(0));
                            /*saveRequestInDataBase();*/
                        }
                    }


                    @Override
                    public void onFailure(Call<JsonTranslate> call, Throwable t) {
                        Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d("Valo", "onFailure " + t.getMessage());
                    }
                });
        } else {
            tvOut.setText("");
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
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;

            case R.id.menu_main_favorite:
                Intent intentFavorite = new Intent(this, HistoryAndFavoriteActivity.class);
                startActivity(intentFavorite);
        }
        return super.onOptionsItemSelected(item);
    }

    public void getInLanguageFromDialog(String languageIn, String key) {
        inLanguageToolbar.setText(languageIn);
        inLangKey = key;
        SharedPreferences.Editor inLangKeyPref = preferences.edit().putString(IN_LANG_KEY, inLangKey);
        inLangKeyPref.apply();
        SharedPreferences.Editor editorInLanPref = preferences.edit().putString(IN_LANG_TOOLBAR_PREF, inLanguageToolbar.getText().toString());
        editorInLanPref.apply();
        getPairLangsToRequest();
        /*Log.d("Valo", "getInLanguageFromDialog" + languageIn + " " + key+ " editorInLanPref= " +editorInLanPref);*/
        loadTranslate();
    }

    public void getOutLanguageFromDialog(String languageOut, String key) {
        outLanguageToolbar.setText(languageOut);
        outLangKey = key;
        SharedPreferences.Editor outLangKeyPref = preferences.edit().putString(OUT_LANG_KEY, outLangKey);
        outLangKeyPref.apply();
        SharedPreferences.Editor editorOutLanPref = preferences.edit().putString(OUT_LANG_TOOLBAR_PREF, outLanguageToolbar.getText().toString());
        editorOutLanPref.apply();
        getPairLangsToRequest();
        loadTranslate();
    }

    public String getPairLangsToRequest() {
        if (inLangKey == null) {
            String getInLangKey = preferences.getString(IN_LANG_KEY, null);
            inLangKey = getInLangKey;
        } else if (outLangKey == null) {
            String getOutLangKey = preferences.getString(OUT_LANG_KEY, null);
            outLangKey = getOutLangKey;
        } else {
            langCodePref = inLangKey + "-" + outLangKey;
            SharedPreferences.Editor editorLangCodePref = preferences.edit().putString(LANG_CODE_PREF, langCodePref);
            editorLangCodePref.apply();
        }
        return langCodePref;
    }
}

/*  private void saveRequestInDataBase() {

        MySQLiteHelper helper = new MySQLiteHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        *//*values.put(HistoryTable.COLUMN_HISTORY_ID, id);*//*
        values.put(HistoryTable.COLUMN_HISTORY_TEXTIN, etIn.getText().toString());
        values.put(HistoryTable.COLUMN_HISTORY_TEXTOUT, tvOut.getText().toString());
        values.put(HistoryTable.COLUMN_HISTORY_TRANSLATE_DIRECTION, langCode);

// Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                HistoryTable.TABLE_HISTORY,
                null,
                values);
    }*/
