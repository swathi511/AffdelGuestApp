package com.hjsoft.affdelguestapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.JsonObject;
import com.hjsoft.affdelguestapp.R;
import com.hjsoft.affdelguestapp.model.Pojo;
import com.hjsoft.affdelguestapp.webservice.API;
import com.hjsoft.affdelguestapp.webservice.RestClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    EditText etMobileNumber,etName,etEmail,etPassword,etConfirmPassword,etAddress,etCity;
    String stMobile,stName,stEmail,stPwd,stCPwd,stAddress,stCity;
    Button btGetOtp;
    API REST_CLIENT;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    int PRIVATE_MODE = 0;
    ProgressDialog progressDialog;
    private static final String PREF_NAME = "SharedPref";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        REST_CLIENT= RestClient.get();
        pref = getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();

        etMobileNumber=(EditText)findViewById(R.id.ar_et_mobile);
        etName=(EditText)findViewById(R.id.ar_et_name);
        etEmail=(EditText)findViewById(R.id.ar_et_email);
        etPassword=(EditText)findViewById(R.id.ar_et_pwd);
        etConfirmPassword=(EditText)findViewById(R.id.ar_et_c_pwd);
        etAddress=(EditText)findViewById(R.id.ar_et_address);
        etCity=(EditText)findViewById(R.id.ar_et_city);
        btGetOtp=(Button)findViewById(R.id.ar_bt_get_otp);

        btGetOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                stMobile=etMobileNumber.getText().toString().trim();
                stName=etName.getText().toString().trim();
                stEmail=etEmail.getText().toString().trim();
                stPwd=etPassword.getText().toString().trim();
                stCPwd=etConfirmPassword.getText().toString().trim();
                stAddress=etAddress.getText().toString().trim();
                stCity=etCity.getText().toString().trim();

                JsonObject v=new JsonObject();
                v.addProperty("name",stName);
                v.addProperty("mobile",stMobile);
                v.addProperty("city",stCity);
                v.addProperty("address",stAddress);
                v.addProperty("email",stEmail);
                v.addProperty("password",stPwd);
                v.addProperty("confirmpassword",stCPwd);
                v.addProperty("otp","");

                editor.putString("name",stName);
                editor.putString("mobile",stMobile);
                editor.putString("city",stCity);
                editor.putString("address",stAddress);
                editor.putString("email",stEmail);
                editor.putString("password",stPwd);
                editor.putString("confirmpassword",stCPwd);
                editor.commit();

                Call<Pojo> call=REST_CLIENT.userRegister(v);
                call.enqueue(new Callback<Pojo>() {
                    @Override
                    public void onResponse(Call<Pojo> call, Response<Pojo> response) {

                        if(response.isSuccessful())
                        {
                            Intent i=new Intent(RegisterActivity.this,OTPValidationActivity.class);
                            startActivity(i);
                        }
                    }

                    @Override
                    public void onFailure(Call<Pojo> call, Throwable t) {

                    }
                });



            }
        });





    }
}
