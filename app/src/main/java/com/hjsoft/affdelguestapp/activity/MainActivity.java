package com.hjsoft.affdelguestapp.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.icu.text.UnicodeSetSpanner;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class MainActivity extends AppCompatActivity {

    Button btLogin;
    String stLogin,stPwd,version="1.0";
    EditText etLogin,etPwd;
    API REST_CLIENT;
    String stProfileId,stDeviceId;
    SessionManager session;
    final static int REQUEST_LOCATION = 199;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "SharedPref";
    HashMap<String, String> user;
    TextView tvRegister;

    @SuppressLint({"HardwareIds","MissingPermission"})
    @Override
    @TargetApi(26)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new SessionManager(getApplicationContext());
        user=session.getUserDetails();
        REST_CLIENT=RestClient.get();

        btLogin=(Button)findViewById(R.id.am_bt_login);
        etLogin=(EditText)findViewById(R.id.am_et_mobile_no);
        etPwd=(EditText)findViewById(R.id.am_et_pwd);
        tvRegister=(TextView)findViewById(R.id.am_tv_register);

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });

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
                    Toast.makeText(MainActivity.this, "Permission is required for this app to run !", Toast.LENGTH_LONG).show();
                }
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_LOCATION);
            }
        }

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                stLogin=etLogin.getText().toString().trim();
                stPwd=etPwd.getText().toString().trim();

                if(stLogin.length()==0||stPwd.length()==0)
                {
                    Toast.makeText(MainActivity.this,"Please enter all the details!",Toast.LENGTH_SHORT).show();
                }
                else if(stLogin.length()!=10)
                {
                    Toast.makeText(MainActivity.this,"Please enter valid mobile number!",Toast.LENGTH_SHORT).show();
                }
                else {

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
                                                Intent i = new Intent(MainActivity.this, ParcelBookingActivity.class);
                                                startActivity(i);
                                                finish();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Pojo> call, Throwable t) {

                                            Toast.makeText(MainActivity.this,"Please check Internet connection!", Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                } else {

                                    Log.i("MA", "IMEI " + stDeviceId);

                                    if (stDeviceId.equals(s.split("-")[2])) {
                                        Intent i = new Intent(MainActivity.this, ParcelBookingActivity.class);
                                        startActivity(i);
                                        finish();
                                    } else {

                                        activateLogin();
                                    }
                                }
                            } else {

                                data = response.body();
                                s = data.getMessage();

                                if(s.equals("Version mismatched"))
                                {
                                    Toast.makeText(MainActivity.this,"Please update the app from playstore",Toast.LENGTH_SHORT).show();
                                }
                                else {

                                    Toast.makeText(MainActivity.this, "Invalid credentials!", Toast.LENGTH_SHORT).show();
                                    etLogin.setText("");
                                    etPwd.setText("");

                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<Pojo> call, Throwable t) {

                            Toast.makeText(MainActivity.this, "Please check the internet connection!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }





                /*Intent i=new Intent(MainActivity.this,ParcelBookingActivity.class);
                startActivity(i);
                finish();*/
            }
        });
    }

    public void checkIfLoggedIn() {

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
                                        Intent i = new Intent(MainActivity.this, ParcelBookingActivity.class);
                                        startActivity(i);
                                        finish();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Pojo> call, Throwable t) {

                                    Toast.makeText(MainActivity.this,"Please check Internet connection!",Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                        else {

                            if (stDeviceId.equals(s.split("-")[2])) {

                                Intent i = new Intent(MainActivity.this, ParcelBookingActivity.class);
                                startActivity(i);
                                finish();

                            } else {

                                Toast.makeText(MainActivity.this, "Profile is active in other device\nHence deactivating here!", Toast.LENGTH_LONG).show();
                                session.logoutUser();

                            }
                        }

                    } else {


                    }
                }

                @Override
                public void onFailure(Call<Pojo> call, Throwable t) {

                    Toast.makeText(MainActivity.this,"Please check Internet connection!",Toast.LENGTH_SHORT).show();
                }
            });

        }

    }
    public void activateLogin()
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);

        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.alert_profile_active, null);

        dialogBuilder.setView(dialogView);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);

        Button btOk=(Button)dialogView.findViewById(R.id.apa_bt_ok);
        Button btCancel=(Button)dialogView.findViewById(R.id.apa_bt_cancel);

        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                JsonObject v=new JsonObject();
                v.addProperty("guestprofileid",stProfileId);
                v.addProperty("imei",stDeviceId);

                Call<Pojo> call1=REST_CLIENT.updateIMEI(v);
                call1.enqueue(new Callback<Pojo>() {
                    @Override
                    public void onResponse(Call<Pojo> call, Response<Pojo> response) {

                        if(response.isSuccessful())
                        {
                            alertDialog.dismiss();
                            session.createLoginSession(stLogin,stPwd,stProfileId);
                            Intent i=new Intent(MainActivity.this,ParcelBookingActivity.class);
                            startActivity(i);
                            finish();
                        }


                    }

                    @Override
                    public void onFailure(Call<Pojo> call, Throwable t) {

                    }
                });
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();
                etLogin.setText("");
                etPwd.setText("");
            }
        });
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
                Toast.makeText(MainActivity.this, "Permission is required for this app to run !", Toast.LENGTH_LONG).show();
                finish();
            }
        }
        else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}

