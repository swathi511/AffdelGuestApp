package com.hjsoft.affdelguestapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

public class ChangePasswordFragment extends Fragment {

    View view;
    EditText etOldPwd,etNewPwd,etConfirmPwd;
    String stOldPwd,stNewPwd,stConfirmPwd,stPwd,stProfileId;
    Button btSubmit;
    API REST_CLIENT;
    SessionManager session;
    HashMap<String, String> user;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_change_password,container,false);
        etOldPwd=(EditText)view.findViewById(R.id.fcp_et_old_pwd);
        etNewPwd=(EditText)view.findViewById(R.id.fcp_et_new_pwd);
        etConfirmPwd=(EditText)view.findViewById(R.id.fcp_et_confirm_pwd);
        btSubmit=(Button)view.findViewById(R.id.fcp_bt_submit);

        REST_CLIENT= RestClient.get();
        session=new SessionManager(getContext());
        user=session.getUserDetails();

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                stPwd=user.get(SessionManager.KEY_PWD);
                stOldPwd=etOldPwd.getText().toString().trim();
                stNewPwd=etNewPwd.getText().toString().trim();
                stConfirmPwd=etConfirmPwd.getText().toString().trim();

                if(stOldPwd.equals("")||stNewPwd.equals("")||stConfirmPwd.equals(""))
                {
                    Toast.makeText(getActivity(),"Please fill all the details!",Toast.LENGTH_SHORT).show();
                }
                else if(!stPwd.equals(stOldPwd))
                {
                    Toast.makeText(getActivity(),"Old Password doesn't matches with the stored password",Toast.LENGTH_SHORT).show();

                    etOldPwd.setText("");

                }
                else if(!stNewPwd.equals(stConfirmPwd))
                {
                    Toast.makeText(getActivity(),"New and Confirm Password should be same!",Toast.LENGTH_SHORT).show();
                    etNewPwd.setText("");
                    etConfirmPwd.setText("");
                }
                else {

                    stProfileId=user.get(SessionManager.KEY_PROFILE_ID);

                    JsonObject v=new JsonObject();
                    v.addProperty("guestprofileid","");
                    v.addProperty("oldpassword",stOldPwd);
                    v.addProperty("newpassword",stNewPwd);

                    Call<Pojo> call=REST_CLIENT.changePassword(v);
                    call.enqueue(new Callback<Pojo>() {
                        @Override
                        public void onResponse(Call<Pojo> call, Response<Pojo> response) {

                            if(response.isSuccessful())
                            {
                                Toast.makeText(getActivity(),"Password updated successfully!",Toast.LENGTH_SHORT).show();
                                etOldPwd.setClickable(false);
                                etOldPwd.setEnabled(false);
                                etNewPwd.setClickable(false);
                                etConfirmPwd.setEnabled(false);
                                etConfirmPwd.setClickable(false);
                                etConfirmPwd.setEnabled(false);
                            }
                        }

                        @Override
                        public void onFailure(Call<Pojo> call, Throwable t) {

                            Toast.makeText(getActivity(),"Please check Internet connection!",Toast.LENGTH_SHORT).show();

                        }
                    });


                }
            }
        });





        return view;
    }
}
