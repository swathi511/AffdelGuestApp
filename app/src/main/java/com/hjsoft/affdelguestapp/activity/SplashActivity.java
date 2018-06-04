package com.hjsoft.affdelguestapp.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.hjsoft.affdelguestapp.R;
import com.hjsoft.affdelguestapp.SessionManager;
import com.hjsoft.affdelguestapp.model.Pojo;
import com.hjsoft.affdelguestapp.webservice.API;
import com.hjsoft.affdelguestapp.webservice.RestClient;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    SessionManager session;
    HashMap<String, String> user;
    String stLogin,stPwd,stProfileId,stDeviceId;
    String version="1.0";
    API REST_CLIENT;
    final static int REQUEST_LOCATION = 199;


    @SuppressLint({"HardwareIds","MissingPermission"})
    @Override
    @TargetApi(26)
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        session = new SessionManager(getApplicationContext());
        user=session.getUserDetails();
        REST_CLIENT= RestClient.get();

        if (Build.VERSION.SDK_INT < 23) {
            TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            //System.out.println("IMIE number is "+telephonyManager.getDeviceId());
            stDeviceId = telephonyManager.getDeviceId();

            checkIfLoggedIn();
        } else {

            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
                //System.out.println("IMIE number is "+telephonyManager.getDeviceId()+telephonyManager.getImei());

                if(Build.VERSION.SDK_INT<=26) {
                    stDeviceId = telephonyManager.getDeviceId();
                }
                else {
                    stDeviceId=telephonyManager.getImei();
                }

                checkIfLoggedIn();
            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_PHONE_STATE)) {
                    Toast.makeText(SplashActivity.this, "Permission is required for this app to run !", Toast.LENGTH_LONG).show();
                }
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_LOCATION);
            }
        }

    }

    public void checkIfLoggedIn()
    {
        if (session.checkLogin()) {

            stLogin = user.get(SessionManager.KEY_NAME);
            stPwd = user.get(SessionManager.KEY_PWD);

            JsonObject v = new JsonObject();
            v.addProperty("login", stLogin);
            v.addProperty("pwd", stPwd);
            v.addProperty("version", version);

            Call<Pojo> call = REST_CLIENT.sendLoginDetails(v);
            call.enqueue(new Callback<Pojo>() {
                @Override
                public void onResponse(Call<Pojo> call, Response<Pojo> response) {

                    Pojo data;
                    String s;

                    if (response.isSuccessful()) {
                        data = response.body();
                        s = data.getMessage();
                        stProfileId = s.split("-")[0];

                        if (s.split("-").length == 1) {
                            JsonObject v = new JsonObject();
                            v.addProperty("guestprofileid", stProfileId);
                            v.addProperty("imei", stDeviceId);

                            Call<Pojo> call1 = REST_CLIENT.updateIMEI(v);
                            call1.enqueue(new Callback<Pojo>() {
                                @Override
                                public void onResponse(Call<Pojo> call, Response<Pojo> response) {

                                    if (response.isSuccessful()) {
                                        session.createLoginSession(stLogin, stPwd, stProfileId);
                                        Intent i = new Intent(SplashActivity.this, ParcelBookingActivity.class);
                                        startActivity(i);
                                        finish();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Pojo> call, Throwable t) {

                                    Toast.makeText(SplashActivity.this,"Please check Internet connection!",Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                        else {

                            if (stDeviceId.equals(s.split("-")[2])) {

                                Intent i = new Intent(SplashActivity.this, ParcelBookingActivity.class);
                                startActivity(i);
                                finish();

                            } else {

                                Toast.makeText(SplashActivity.this, "Profile is active in other device\nHence deactivating here!", Toast.LENGTH_LONG).show();
                                session.logoutUser();

                            }
                        }

                    } else {


                    }
                }

                @Override
                public void onFailure(Call<Pojo> call, Throwable t) {

                    Toast.makeText(SplashActivity.this,"Please check Internet connection!",Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    @SuppressLint({"HardwareIds","MissingPermission"})
    @Override
    @TargetApi(26)
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode==REQUEST_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
                //System.out.println("IMIE number is "+telephonyManager.getDeviceId()+"::"+session.isLoggedIn());
                if(Build.VERSION.SDK_INT<=26) {
                    stDeviceId = telephonyManager.getDeviceId();
                }
                else {
                    stDeviceId=telephonyManager.getImei();
                }

                checkIfLoggedIn();

            } else {
                Toast.makeText(SplashActivity.this, "Permission is required for this app to run !", Toast.LENGTH_LONG).show();
                finish();
            }
        }
        else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
