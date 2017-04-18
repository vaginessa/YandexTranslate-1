package ru.turpattaya.yandextranslate;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;



public interface ServiceDictionary {
    /*https://dictionary.yandex.net/api/v1/dicservice.json/lookup?key=...&lang=en-ru&text=time*/
    @GET("api/v1/dicservice.json/lookup")
    Call<Object> getDictioanry(
            @Query("key") String key,
            @Query("lang") String language,
            @Query("text") String text);
}
