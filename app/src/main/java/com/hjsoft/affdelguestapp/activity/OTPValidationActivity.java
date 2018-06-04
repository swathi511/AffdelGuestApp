package com.hjsoft.affdelguestapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.hjsoft.affdelguestapp.R;
import com.hjsoft.affdelguestapp.model.Pojo;
import com.hjsoft.affdelguestapp.webservice.API;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OTPValidationActivity extends AppCompatActivity {

    EditText etOtp1,etOtp2,etOtp3,etOtp4;
    Button btValidateOtp;
    String stOtp1,stOtp2,stOtp3,stOtp4;
    API REST_CLIENT;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    int PRIVATE_MODE = 0;
    ProgressDialog progressDialog;
    private static final String PREF_NAME = "SharedPref";

    String stMobile,stName,stEmail,stPwd,stCPwd,stAddress,stCity;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_confirm);

        etOtp1=(EditText)findViewById(R.id.arc_et_otp1);
        etOtp2=(EditText)findViewById(R.id.arc_et_otp2);
        etOtp3=(EditText)findViewById(R.id.arc_et_otp3);
        etOtp4=(EditText)findViewById(R.id.arc_et_otp4);
        btValidateOtp=(Button)findViewById(R.id.arc_bt_validate_otp);
        pref = getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();

        stName=pref.getString("name",null);
        stMobile=pref.getString("mobile",null);
        stCity=pref.getString("city",null);
        stAddress=pref.getString("address",null);
        stEmail=pref.getString("email",null);
        stPwd=pref.getString("password",null);
        stCPwd=pref.getString("confirmpassword",null);

        etOtp1.addTextChangedListener(new GenericTextWatcher(etOtp1));
        etOtp2.addTextChangedListener(new GenericTextWatcher(etOtp2));
        etOtp3.addTextChangedListener(new GenericTextWatcher(etOtp3));
        etOtp4.addTextChangedListener(new GenericTextWatcher(etOtp4));

        btValidateOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                stOtp1=etOtp1.getText().toString().trim();
                stOtp2=etOtp2.getText().toString().trim();
                stOtp3=etOtp3.getText().toString().trim();
                stOtp4=etOtp4.getText().toString().trim();

                if(stOtp1.length()==0||stOtp2.length()==0||stOtp3.length()==0||stOtp4.length()==0){
                    Toast.makeText(OTPValidationActivity.this, "Enter valid OTP", Toast.LENGTH_SHORT).show();
                }
                else{

                    progressDialog = new ProgressDialog(OTPValidationActivity.this);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Please wait ...");
                    progressDialog.show();

                    JsonObject v=new JsonObject();
                    v.addProperty("name",stName);
                    v.addProperty("mobile",stMobile);
                    v.addProperty("city",stCity);
                    v.addProperty("address",stAddress);
                    v.addProperty("email",stEmail);
                    v.addProperty("password",stPwd);
                    v.addProperty("confirmpassword",stCPwd);
                    v.addProperty("otp",stOtp1+stOtp2+stOtp3+stOtp4);

                    Call<Pojo>call=REST_CLIENT.otpValidation(v);
                    call.enqueue(new Callback<Pojo>() {
                        Pojo otpPojo;
                        @Override
                        public void onResponse(Call<Pojo> call, Response<Pojo> response) {

                            if(response.isSuccessful()){

                                Toast.makeText(OTPValidationActivity.this,"OTP successfully validated!",Toast.LENGTH_SHORT).show();

                                Intent k=new Intent(OTPValidationActivity.this,ParcelBookingActivity.class);
                                startActivity(k);
                                finish();
                            }

                        }

                        @Override
                        public void onFailure(Call<Pojo> call, Throwable t) {

                        }
                    });


                }


            }
        });



    }
    class GenericTextWatcher implements TextWatcher
    {
        private View view;
        private GenericTextWatcher(View view)
        {
            this.view = view;
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // TODO Auto-generated method stub
            String text = editable.toString();
            switch(view.getId())
            {

                case R.id.arc_et_otp1:
                    if(text.length()==1)
                        etOtp2.requestFocus();
                    break;
                case R.id.arc_et_otp2:
                    if(text.length()==1)
                        etOtp3.requestFocus();
                    break;
                case R.id.arc_et_otp3:
                    if(text.length()==1)
                        etOtp4.requestFocus();
                    break;
                case R.id.arc_et_otp4:
                    break;
            }
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            String text = arg0.toString();

            switch (view.getId()) {
                case R.id.arc_et_otp1:
                    if (text.length() == 1) {

                        etOtp1.requestFocus();
                        etOtp1.setSelection(etOtp1.getText().length());

                    }
                    else
                        etOtp1.requestFocus();

                    break;
                case R.id.arc_et_otp2:
                    if (text.length()==1) {

                        etOtp1.requestFocus();
                        etOtp1.setSelection(etOtp1.getText().length());
                    }

                    break;
                case R.id.arc_et_otp3:
                    if (text.length() == 1) {

                        etOtp2.requestFocus();
                        etOtp2.setSelection(etOtp2.getText().length());

                    }

                    break;
                case R.id.arc_et_otp4:
                    if (text.length() == 1) {
                        etOtp3.requestFocus();

                        etOtp3.setSelection(etOtp3.getText().length());
                    }
                    break;
                default:
                    break;
            }

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

    }
}
