package com.hjsoft.affdelguestapp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hjsoft.affdelguestapp.R;

public class OTPValidationActivity extends AppCompatActivity {

    EditText etOtp1,etOtp2,etOtp3,etOtp4;
    Button btValidateOtp;
    String stOtp1,stOtp2,stOtp3,stOtp4;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_confirm);

        etOtp1=(EditText)findViewById(R.id.arc_et_otp1);
        etOtp2=(EditText)findViewById(R.id.arc_et_otp2);
        etOtp3=(EditText)findViewById(R.id.arc_et_otp3);
        etOtp4=(EditText)findViewById(R.id.arc_et_otp4);
        btValidateOtp=(Button)findViewById(R.id.arc_bt_validate_otp);

        btValidateOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stOtp1=etOtp1.getText().toString().trim();
                stOtp2=etOtp2.getText().toString().trim();
                stOtp3=etOtp3.getText().toString().trim();
                stOtp4=etOtp4.getText().toString().trim();




            }
        });



    }
}
