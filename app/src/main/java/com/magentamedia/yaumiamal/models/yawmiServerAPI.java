package com.magentamedia.yaumiamal.models;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface yawmiServerAPI {
    @FormUrlEncoded
    @POST("yawmiserver/register.php")
    Call<RegisterFeedback> registerAcc(@Field("username") String username,
                                       @Field("email") String email,
                                       @Field("password") String password,
                                       @Field("token_number") String token_number);

    @GET("yawmiserver/artikel.php")
    Call<ArtikelModel> getPostingan();
}
