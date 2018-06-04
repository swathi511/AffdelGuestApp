package com.hjsoft.affdelguestapp.webservice;

import com.google.gson.JsonObject;
import com.hjsoft.affdelguestapp.model.Pojo;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by hjsoft on 23/11/16.
 */
public interface API {

    @POST("GuestLogin/Checklogin")
    Call<Pojo> sendLoginDetails(@Body JsonObject v);

    @POST("GuestChangePassword/Updatepassword")
    Call<Pojo> changePassword(@Body JsonObject v);

    @POST("GuestImei/UpdateIMEI")
    Call<Pojo> updateIMEI(@Body JsonObject v);

    @POST("Register/AddUserDetails")
    Call<Pojo> userRegister(@Body JsonObject v);


}
