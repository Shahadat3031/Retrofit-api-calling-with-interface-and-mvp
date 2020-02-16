package com.sheba.retrofitapicalling.api;

import com.sheba.retrofitapicalling.model.Hero;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiClient {

    @GET("marvel")
    Call<List<Hero>> getHeroes();
}
