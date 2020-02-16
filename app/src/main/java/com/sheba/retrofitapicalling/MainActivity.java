package com.sheba.retrofitapicalling;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.sheba.retrofitapicalling.model.Hero;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MainActivityContract.View {

    List<Hero> heroes;
    TextView tvName;
    MainActivityPresenter mainActivityPresenter;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvName = findViewById(R.id.textName);
        mContext = MainActivity.this;

        mainActivityPresenter = new MainActivityPresenter(mContext, this);
        mainActivityPresenter.getMovieList();

    }

    @Override
    public void showMovieList(List<Hero> heroes) {

        this.heroes = heroes;
        Toast.makeText(this, "Heroes Size:" + heroes.size(), Toast.LENGTH_SHORT).show();

        tvName.setText(heroes.get(0).getName());

    }

    @Override
    public void showServiceError(String message) {

    }
}
