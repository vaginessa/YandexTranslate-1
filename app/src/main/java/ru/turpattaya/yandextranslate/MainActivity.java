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
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import ru.yandex.speechkit.Error;
import ru.yandex.speechkit.Recognizer;
import ru.yandex.speechkit.SpeechKit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.turpattaya.yandextranslate.JsonDictionary.JsonDictionaryResult;
import ru.yandex.speechkit.Synthesis;
import ru.yandex.speechkit.Vocalizer;
import ru.yandex.speechkit.VocalizerListener;
import ru.yandex.speechkit.gui.RecognizerActivity;

import static ru.turpattaya.yandextranslate.ApiKey.KEY_SPEECHKIT_YANDEX;

public class MainActivity extends AppCompatActivity implements VocalizerListener {

    private TextView inLanguageToolbar, outLanguageToolbar, tvOut;
    private TextView dictTranslateResult, dictPosResult, dictTranscriptionResult, dictTrResult;
    private EditText etIn;

    private ImageView imageClearEtMain, imageMicrophoneMain, imageReproductionTextInMain, imageReproductionTextOutMain,
            footerTranslateImage, footerFavoriteImage, footerSettingsImage;

    public static final int REQUEST_CODE = 999;
    private Vocalizer vocalizer;

    String textForTranslate, inLanguageToolbarPref, outLanguageToolbarPref, langCodePref, inLangKey, outLangKey;

    SharedPreferences preferences;
    public static final String IN_LANG_TOOLBAR_PREF = "IN_LANG_TOOLBAR_PREF";
    public static final String OUT_LANG_TOOLBAR_PREF = "OUT_LANG_TOOLBAR_PREF";
    public static final String LANG_CODE_PREF = "LANG_CODE_PREF";
    public static final String IN_LANG_KEY = "IN_LANG_KEY";
    public static final String OUT_LANG_KEY = "OUT_LANG_KEY";

    SQLiteDatabase db;
    HistoryAdapter adapter;

    public MainActivity() {
    }

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
        footerTranslateImage = (ImageView) findViewById(R.id.footer_image_translate);
        footerFavoriteImage = (ImageView) findViewById(R.id.footer_image_favorite);
        footerSettingsImage = (ImageView) findViewById(R.id.footer_image_settings);
        dictTranslateResult = (TextView) findViewById(R.id.main_text_dict_translate_result);
        dictPosResult = (TextView) findViewById(R.id.main_text_dict_pos_result);
        dictTranscriptionResult = (TextView) findViewById(R.id.main_text_dict_transcription);
        /*dictTrResult = (TextView) findViewById(R.id.main_text_dict_tr_result);*/

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
                if (tvOut != null) {
                    String changeWord = tvOut.getText().toString();
                    etIn.setText(changeWord);
                }
                loadTranslate();

            }
        });

        etIn.addTextChangedListener(new TextWatcher() {
            final Handler handler = new android.os.Handler();
            Runnable runnable;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler.removeCallbacks(runnable);
                resetVocalizer();
            }

            @Override
            public void afterTextChanged(Editable s) {
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        loadTranslate();
                        String text = etIn.getText().toString();
                        String[] words = text.split("\\s+");
                        if (words.length < 2 && words.length >0) {
                            loadDictionary();
                        } else {
                            dictTranslateResult.setText("");
                            dictPosResult.setText("");
                            dictTranscriptionResult.setText("");
                        }
                    }
                };
                handler.postDelayed(runnable, 2000);
            }
        });

        

        imageClearEtMain = (ImageView) findViewById(R.id.main_image_clear);
        imageClearEtMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etIn.setText("");
            }
        });

        footerTranslateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        footerFavoriteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HistoryAndFavoriteActivity.class);
                startActivity(intent);
            }
        });
        footerSettingsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        SpeechKit.getInstance().configure(getApplicationContext(), KEY_SPEECHKIT_YANDEX);

        imageMicrophoneMain = (ImageView) findViewById(R.id.main_microphone);
        imageReproductionTextInMain = (ImageView) findViewById(R.id.main_reproduction_text_in);
        imageReproductionTextOutMain = (ImageView) findViewById(R.id.main_reproduction_text_out);

        imageMicrophoneMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etIn.setText("");
                Intent intent = new Intent(MainActivity.this, RecognizerActivity.class);
                intent.putExtra(RecognizerActivity.EXTRA_MODEL, Recognizer.Model.QUERIES);
                intent.putExtra(RecognizerActivity.EXTRA_LANGUAGE, Recognizer.Language.RUSSIAN);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        imageReproductionTextInMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textVocal = etIn.getText().toString();
                if (TextUtils.isEmpty(textVocal)) {
                    Toast.makeText(MainActivity.this, "Нечего воспроизводить!", Toast.LENGTH_SHORT).show();
                } else {
                    resetVocalizer();
                    vocalizer = Vocalizer.createVocalizer(Vocalizer.Language.RUSSIAN, textVocal, true, Vocalizer.Voice.ERMIL);
                    vocalizer.setListener(MainActivity.this);
                    vocalizer.start();
                }
            }
        });

        imageReproductionTextOutMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textOutVocal = tvOut.getText().toString();
                if (TextUtils.isEmpty(textOutVocal)) {
                    Toast.makeText(MainActivity.this, "Нечего воспроизводить!", Toast.LENGTH_SHORT).show();
                } else {
                    resetVocalizer();
                    if (OUT_LANG_KEY.equals("ru")) {
                        vocalizer = Vocalizer.createVocalizer(Vocalizer.Language.RUSSIAN, textOutVocal, true, Vocalizer.Voice.ERMIL);
                        vocalizer.setListener(MainActivity.this);
                        vocalizer.start();
                    } else if (OUT_LANG_KEY.equals("en")) {
                        vocalizer = Vocalizer.createVocalizer(Vocalizer.Language.ENGLISH, textOutVocal, true, Vocalizer.Voice.ERMIL);
                        vocalizer.setListener(MainActivity.this);
                        vocalizer.start();
                    }
                }
            }
        });
    }

    private void loadDictionary() {
        API api = new API();
        textForTranslate = etIn.getText().toString();
        if (!textForTranslate.equals("")) {

            api.dictionary(langCodePref, textForTranslate, new Callback<JsonDictionaryResult>() {
                @Override
                public void onResponse(Call<JsonDictionaryResult> call, Response<JsonDictionaryResult> response) {
                    Log.d("Valo", "onResponse");
                    if (response != null) {
                        //text = Text of the entry, translation, or synonym (mandatory)
                        for (int i = 0; i <response.body().getDef().size() ; i++) { //нужно для текста для вывода > одного слова


                        if (response.body().getDef().get(0).getText() != null) {
                            dictTranslateResult.setText(response.body().getDef().get(0).getText());
                        }
                        //Ts = transcription
                        if (response.body().getDef().get(0).getTs() != null) {
                            dictTranscriptionResult.setText("[" + response.body().getDef().get(0).getTs() + "]");
                        }


                            Log.d("Valo", "i=" + i);

                            //pos = Part of speech (may be omitted).
                            if (response.body().getDef().get(i).getPos() != null) {
                                dictPosResult.setText(response.body().getDef().get(i).getPos());
                            }

                            //tr = Array of translations.
                            /*if (response.body().getDef().get(i).getTr() != null) {
                                for (int j = 0; j < response.body().getDef().get(i).getTr().size(); j++) {
                                    dictTrResult.setText(response.body().getDef().get(i).getTr().get(j).getText());

                                    //затем синонимы
                                    for (int k = 0; k < response.body().getDef().get(i).getTr().get(j).getSyn().size(); k++) {
                                      *//*  @SerializedName("text")
                                        @Expose
                                        private String text;
                                        @SerializedName("pos")
                                        @Expose
                                        private String pos;
                                        @SerializedName("gen")
                                        @Expose
                                        private String gen;*//*
                                    }
                                    //затем значения
                                    for (int l = 0; l < response.body().getDef().get(i).getTr().get(j).getSyn().size(); l++) {
                                       *//* public class Mean {
                                            @SerializedName("text")
                                            @Expose
                                            private String text;
                                            *//*
                                    }
                                    //примеры
                                    for (int m = 0; m < response.body().getDef().get(i).getTr().get(j).getSyn().size(); m++) {
                                       *//* public class Ex {
                                            @SerializedName("text")
                                            @Expose
                                            private String text;
                                            @SerializedName("tr")
                                            @Expose
                                            private List<Tr_> tr = null;*//*
                                    }
                                }
                            }*/
                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonDictionaryResult> call, Throwable t) {
                    Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.d("Valo", "onFailure " + t.getMessage());
                }
            });
        } else {
            tvOut.setText("");
        }
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
                        String textOut = response.body().getText().get(0);
                        tvOut.setText(textOut);
                            addRec(textForTranslate, textOut, langCodePref, 0);

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



    private void addRec(String textIn, String textOut, String translateDirection, int directionIsFavorite) {
        MySQLiteHelper mDBHelper = new MySQLiteHelper(this);
        db = mDBHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(HistoryTable.COLUMN_HISTORY_TEXTIN, textIn);
        cv.put(HistoryTable.COLUMN_HISTORY_TEXTOUT, textOut);
        cv.put(HistoryTable.COLUMN_HISTORY_TRANSLATE_DIRECTION, translateDirection);
        cv.put(HistoryTable.COLUMN_DIRECTION_IS_FAVORITE, directionIsFavorite);
        db.insert(HistoryTable.TABLE_HISTORY, null, cv);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RecognizerActivity.RESULT_OK && data != null) {
                final String result = data.getStringExtra(RecognizerActivity.EXTRA_RESULT);
                etIn.setText(result);
            } else if (resultCode == RecognizerActivity.RESULT_ERROR) {
                String error = ((ru.yandex.speechkit.Error) data.getSerializableExtra(RecognizerActivity.EXTRA_ERROR)).getString();
                etIn.setText(error);
            }
        }
    }

    private void resetVocalizer() {
        if (vocalizer != null) {
            vocalizer.cancel();
            vocalizer = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        resetVocalizer();
    }

    @Override
    public void onSynthesisBegin(Vocalizer vocalizer) {
        Toast.makeText(MainActivity.this, "Обрабатываем",  Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSynthesisDone(Vocalizer vocalizer, Synthesis synthesis) {

    }

    @Override
    public void onPlayingBegin(Vocalizer vocalizer) {
        Toast.makeText(MainActivity.this, "Воспроизведение",  Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPlayingDone(Vocalizer vocalizer) {
        Toast.makeText(MainActivity.this, "Воспроизведение заверщено", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onVocalizerError(Vocalizer vocalizer, Error error) {
        Toast.makeText(MainActivity.this, "Error occurred " + error.getString(), Toast.LENGTH_SHORT).show();
        resetVocalizer();
    }
}


