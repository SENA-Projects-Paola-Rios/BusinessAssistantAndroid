package com.sena.businessassistantandroid.network;

import com.sena.businessassistantandroid.users.User;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface UserApi {
    @GET("api/users")
    Call<List<User>> getUsers(@Header("Authorization") String token);
}
