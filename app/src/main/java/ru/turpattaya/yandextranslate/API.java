package ru.turpattaya.yandextranslate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static ru.turpattaya.yandextranslate.ApiKey.KEY_API_YANDEX;
import static ru.turpattaya.yandextranslate.ApiKey.KEY_DICTIONARY_YANDEX;


public class API {

    

    void translate(String textIn, String translateDirection, Callback<JsonTranslate> callback) {

        /* HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
// add your other interceptors …
// add logging as last interceptor
        httpClient.addInterceptor(logging);  // <-- this is the important line!*/

        Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://translate.yandex.net/")
                .addConverterFactory(GsonConverterFactory.create(gson))
/*
                  .client(httpClient.build())
*/
                .build();
        ServiceTranslate service = retrofit.create(ServiceTranslate.class);

        Call<JsonTranslate> call = service.getTranslatedText(KEY_API_YANDEX, textIn, translateDirection);
        call.enqueue(callback);
    }

  /*  void getLangs(Callback<Object> callback) {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
// add your other interceptors …
// add logging as last interceptor
        httpClient.addInterceptor(logging);  // <-- this is the important line!

        Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://dictionary.yandex.net/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();
        ServiceGetLangs service = retrofit.create(ServiceGetLangs.class);

        Call<Object> call = service.getLangs(KEY_DICTIONARY_YANDEX);
        call.enqueue(callback);
    }*/
}
