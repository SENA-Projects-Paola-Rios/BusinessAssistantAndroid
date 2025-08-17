package com.sena.businessassistantandroid.network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApi {


    @POST("api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);
}
