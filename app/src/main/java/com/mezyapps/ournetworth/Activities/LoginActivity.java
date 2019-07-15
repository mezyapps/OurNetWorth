package com.mezyapps.ournetworth.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mezyapps.ournetworth.Common;
import com.mezyapps.ournetworth.Config;
import com.mezyapps.ournetworth.MVP.SignUpResponse;
import com.mezyapps.ournetworth.R;
import com.mezyapps.ournetworth.Retrofit.Api;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginActivity extends AppCompatActivity {
private EditText editText_mobile,editText_password;
private String mobile,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
      //  getSupportActionBar().hide();
        editText_mobile=findViewById(R.id.mobileno_login);
        editText_password=findViewById(R.id.password_login);
    }

    public void loginprocess(View view) {
        mobile=editText_mobile.getText().toString().trim();
        password=editText_password.getText().toString().trim();
        loginmethod();
    }

    private void loginmethod() {
        final SweetAlertDialog pDialog = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        // sending gcm token to server
        Api.getClient().login(mobile, password,
                new Callback<SignUpResponse>() {
                    @Override
                    public void success(SignUpResponse signUpResponse, Response response) {
                        pDialog.dismiss();
                        Log.d("signUpResponse", signUpResponse.getUserid() + "");
                        Toast.makeText(LoginActivity.this, signUpResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        if (signUpResponse.getSuccess().equalsIgnoreCase("true")) {
                            Common.saveUserData(LoginActivity.this, "mobile", editText_mobile.getText().toString());
                            Common.saveUserData(LoginActivity.this, "userId", signUpResponse.getUserid()+"");
                            Config.moveTo(LoginActivity.this, MainActivity.class);
                            finishAffinity();
//                        } else if (signUpResponse.getSuccess().equalsIgnoreCase("notactive")) {
//                            Config.moveTo(Login.this, AccountVerification.class);
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        pDialog.dismiss();

                        Log.e("error", error.toString());
                    }
                });
    }
}
