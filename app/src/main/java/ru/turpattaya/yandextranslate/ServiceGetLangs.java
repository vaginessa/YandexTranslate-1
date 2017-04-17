package ru.turpattaya.yandextranslate;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by MSI on 4/12/2017.
 */

public interface ServiceGetLangs {
    /*https://dictionary.yandex.net/api/v1/dicservice/getLangs?key=APIkey*/
    @GET("api/v1/dicservice/getLangs")
    Call<Object> getLangs(@Query("key") String key);
}

