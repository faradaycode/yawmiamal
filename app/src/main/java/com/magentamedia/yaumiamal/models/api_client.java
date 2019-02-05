package com.magentamedia.yaumiamal.models;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class api_client {
//    public static final String URL = "http://muslimsalat.com/";
    public static final String URL = "http://api.aladhan.com/v1/";
    public static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if(retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(URL).addConverterFactory(GsonConverterFactory
                    .create()).build();
        }
        return retrofit;
    }
}