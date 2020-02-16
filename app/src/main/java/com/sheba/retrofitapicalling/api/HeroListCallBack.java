package com.sheba.retrofitapicalling.api;

import com.sheba.retrofitapicalling.model.Hero;

import java.util.List;

public class HeroListCallBack {

    public interface GetHeroList {
       void onSuccess(List<Hero> hero);
       void onError(String msg);
       void onFailed(String msg);
    }
}
