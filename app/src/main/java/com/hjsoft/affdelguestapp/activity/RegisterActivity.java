package com.hjsoft.affdelguestapp.activity;

import android.content.Intent;
import android.icu.text.UnicodeSetSpanner;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        REST_CLIENT= RestClient.get();

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

                if(stMobile.length()==0||stName.length()==0||stEmail.length()==0||stPwd.length()==0||
                        stCPwd.length()==0||stAddress.length()==0||stCity.length()==0)
                {
                    Toast.makeText(RegisterActivity.this,"Please fill all the details!",Toast.LENGTH_LONG).show();
                }
                else if(stMobile.length()!=10)
                {
                    Toast.makeText(RegisterActivity.this,"Please enter valid mobile number!",Toast.LENGTH_SHORT).show();
                    etMobileNumber.setText("");
                }
                else if(!(android.util.Patterns.EMAIL_ADDRESS.matcher(stEmail).matches()))
                {
                    Toast.makeText(RegisterActivity.this,"Please enter valid email-id!",Toast.LENGTH_SHORT).show();
                    etEmail.setText("");
                }
                else if(!(stPwd.equals(stCPwd)))
                {
                    Toast.makeText(RegisterActivity.this,"Password and Confirm Password should be same!",Toast.LENGTH_SHORT).show();
                    etPassword.setText("");
                    etConfirmPassword.setText("");
                }
                else {

                    JsonObject v = new JsonObject();
                    v.addProperty("name", stName);
                    v.addProperty("mobile", stMobile);
                    v.addProperty("city", stCity);
                    v.addProperty("address", stAddress);
                    v.addProperty("email", stEmail);
                    v.addProperty("password", stPwd);
                    v.addProperty("confirmpassword", stCPwd);
                    v.addProperty("otp", "");
                    Call<Pojo> call = REST_CLIENT.userRegister(v);
                    call.enqueue(new Callback<Pojo>() {
                        @Override
                        public void onResponse(Call<Pojo> call, Response<Pojo> response) {

                            if (response.isSuccessful()) {
                                Intent i = new Intent(RegisterActivity.this, OTPValidationActivity.class);
                                startActivity(i);
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<Pojo> call, Throwable t) {

                            Toast.makeText(RegisterActivity.this,"Please check Internet connection!", Toast.LENGTH_SHORT).show();

                        }
                    });
                }



            }
        });





    }
}
