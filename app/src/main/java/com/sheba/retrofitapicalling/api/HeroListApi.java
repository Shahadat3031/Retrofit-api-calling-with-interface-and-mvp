package com.sheba.retrofitapicalling.api;

import android.content.Context;

import com.sheba.retrofitapicalling.model.Hero;
import com.sheba.retrofitapicalling.utility.ApiServiceGeneratorForCaching;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HeroListApi {

    private Context mContext;
    private ApiClient apiClient;

    public HeroListApi(Context mContext) {
        this.mContext = mContext;
        this.apiClient = new ApiServiceGeneratorForCaching().createService(ApiClient.class,mContext);

    }

    public void getMovieList(final HeroListCallBack.GetHeroList movieListCallBack){

       Call<List<Hero>> call = apiClient.getHeroes();


       call.enqueue(new Callback<List<Hero>>() {
           @Override
           public void onResponse(Call<List<Hero>> call, Response<List<Hero>> response) {

              if (response.body() !=null){
                  if (response.code()==200){
                      movieListCallBack.onSuccess(response.body());
                  } else {
                      movieListCallBack.onError("Error!!!");
                  }
              } else {
                  movieListCallBack.onError("Error Data");
              }
           }

           @Override
           public void onFailure(Call<List<Hero>> call, Throwable t) {
               movieListCallBack.onError("Something Error!!!");
           }
       });

    }


}
