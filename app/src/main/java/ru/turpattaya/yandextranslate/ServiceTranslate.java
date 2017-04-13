package ru.turpattaya.yandextranslate;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by MSI on 4/12/2017.
 */

public interface ServiceTranslate {
    /*https://translate.yandex.net/api/v1.5/tr.json/translate?key=trnsl.1.1.20170411T091433Z.6c7492b689a517b9.a71ebefe6b9a2658af4b6bfa30a1a5ca8a524ee0&text=крокодил&lang=ru-en*/
    @GET("api/v1.5/tr.json/translate")
    Call<List<JsonTranslate>> getTranslatedText(
            @Query("key") String key,
            @Query("text") List<String> text,
            @Query("lang") String language);
}

