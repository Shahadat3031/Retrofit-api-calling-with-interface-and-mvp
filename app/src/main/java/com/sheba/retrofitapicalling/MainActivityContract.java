package com.sheba.retrofitapicalling;

import com.sheba.retrofitapicalling.model.Hero;

import java.util.List;

public class MainActivityContract {

    public interface View{
        void showMovieList(List<Hero> heroes);
        void showServiceError(String message);
    }

    public interface Presenter{
        void getMovieList();
    }
}
