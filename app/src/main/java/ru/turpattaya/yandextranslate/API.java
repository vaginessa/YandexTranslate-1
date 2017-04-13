package ru.turpattaya.yandextranslate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class API {
    public static String KEY_YANDEX_API = "trnsl.1.1.20170411T091433Z.6c7492b689a517b9.a71ebefe6b9a2658af4b6bfa30a1a5ca8a524ee0";

    public API(String KEY) {
        this.KEY_YANDEX_API = KEY;

    }



    void requestTranslation(String textIn, String translateDirection,  Callback<JsonTranslate> callback) {

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

        Call<JsonTranslate> call = service.getTranslatedText(KEY_YANDEX_API, textIn, translateDirection);
        call.enqueue(callback);
    }
}
