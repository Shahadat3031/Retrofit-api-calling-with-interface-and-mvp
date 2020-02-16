package com.sheba.retrofitapicalling;

import android.content.Context;

import com.sheba.retrofitapicalling.api.HeroListApi;
import com.sheba.retrofitapicalling.api.HeroListCallBack;
import com.sheba.retrofitapicalling.model.Hero;

import java.util.List;

public class MainActivityPresenter implements MainActivityContract.Presenter {

    private Context mContext;
    private MainActivityContract.View mainActivityView;
    private HeroListApi heroListApi;

    public MainActivityPresenter(Context mContext, MainActivityContract.View mainActivityView) {
        this.mContext = mContext;
        this.mainActivityView = mainActivityView;
        this.heroListApi = new HeroListApi(mContext);
    }

    @Override
    public void getMovieList() {
        heroListApi.getMovieList(new HeroListCallBack.GetHeroList() {

            @Override
            public void onSuccess(List<Hero> hero) {
                mainActivityView.showMovieList(hero);
            }

            @Override
            public void onError(String msg) {
                mainActivityView.showServiceError("Error!!!");
            }

            @Override
            public void onFailed(String msg) {
                mainActivityView.showServiceError("Failed to load");
            }
        });

    }
}
