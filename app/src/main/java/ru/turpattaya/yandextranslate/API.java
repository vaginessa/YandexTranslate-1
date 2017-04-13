package ru.turpattaya.yandextranslate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class API {
    static String KEY = "trnsl.1.1.20170411T091433Z.6c7492b689a517b9.a71ebefe6b9a2658af4b6bfa30a1a5ca8a524ee0";
    /*https://translate.yandex.net/api/v1.5/tr.json/translate?key=trnsl.1.1.20170411T091433Z.6c7492b689a517b9.a71ebefe6b9a2658af4b6bfa30a1a5ca8a524ee0&text=крокодил&lang=ru-en*/

    void requestTranslation(String somethingForTranslate, Callback<List<JsonTranslate>> callback) {


         HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
// add your other interceptors …
// add logging as last interceptor
        httpClient.addInterceptor(logging);  // <-- this is the important line!

        Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://translate.yandex.net/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                  .client(httpClient.build())
                .build();
        ServiceTranslate service = retrofit.create(ServiceTranslate.class);

        Call<List<JsonTranslate>> call = service.getTranslatedText(KEY, Arrays.asList(new String[]{"крокодил пошел гулять"}), "ru-en");
        call.enqueue(callback);
    }
}
