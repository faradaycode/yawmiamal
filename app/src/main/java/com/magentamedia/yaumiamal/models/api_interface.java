package com.magentamedia.yaumiamal.models;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface api_interface {
//    @GET("{periode}/daily.json")
//    Call<Items> getJadwal(@Path("periode") String periode);

    @GET("timingsByCity")
    Call<Items> getJadwal(
            @Query(value = "city", encoded = true) String city,
            @Query(value = "country", encoded = true) String country,
            @Query(value = "method", encoded = true) String method
    );
}
