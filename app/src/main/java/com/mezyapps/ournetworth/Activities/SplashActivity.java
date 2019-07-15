package com.mezyapps.ournetworth.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.mezyapps.ournetworth.Common;
import com.mezyapps.ournetworth.Config;
import com.mezyapps.ournetworth.DetectConnection;
import com.mezyapps.ournetworth.MVP.Member;
import com.mezyapps.ournetworth.R;
import com.mezyapps.ournetworth.Retrofit.Api;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SplashActivity extends AppCompatActivity {
    public static List<Member> allMembaerData;

    
    @BindView(R.id.errorText)
    TextView errorText;
    @BindView(R.id.internetNotAvailable)
    LinearLayout internetNotAvailable;
    @BindView(R.id.splashImage)
    ImageView splashImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


         // Check the internet and get response from API's
        if (DetectConnection.checkInternetConnection(getApplicationContext())) {
            getAllMember();
        } else {
            errorText.setText("Internet Connection Not Available");
            internetNotAvailable.setVisibility(View.VISIBLE);
            splashImage.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.tryAgain)
    public void onClick() {
        if (DetectConnection.checkInternetConnection(getApplicationContext())) {
            internetNotAvailable.setVisibility(View.GONE);
            splashImage.setVisibility(View.VISIBLE);
            getAllMember();
        } else {
            errorText.setText("Internet Connection Not Available");
            internetNotAvailable.setVisibility(View.VISIBLE);
            splashImage.setVisibility(View.GONE);
        }
    }

    public void getAllMember() {

        // getting news list data
        Api.getClient().getAllMember(new Callback<List<Member>>() {
            @Override
            public void success(List<Member> allMember, Response response) {
                try {
                    allMembaerData = allMember;
                    if (allMember.get(0).getSuccess().equalsIgnoreCase("false")){
                        Toast.makeText(SplashActivity.this, allMember.get(0).getMessage(), Toast.LENGTH_SHORT).show();
                    }else {
                        moveNext();
                    }

                } catch (Exception e) {

                    errorText.setText("No Member Added In This App!");
                    internetNotAvailable.setVisibility(View.VISIBLE);
                    splashImage.setVisibility(View.GONE);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("error", error.toString());
                errorText.setText("Internet Connection Not Available");
                internetNotAvailable.setVisibility(View.VISIBLE);
                splashImage.setVisibility(View.GONE);
            }
        });

    }


    private void moveNext() {
// redirect to next page after getting data from server


        if (Common.getSavedUserData(SplashActivity.this, "mobile").equalsIgnoreCase("")) {
            Config.moveTo(SplashActivity.this, LoginActivity.class);
            finishAffinity();
        } else {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finishAffinity();
        }

    }


}
