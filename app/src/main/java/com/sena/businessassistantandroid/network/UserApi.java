package com.sena.businessassistantandroid.network;

import com.sena.businessassistantandroid.users.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserApi {
    @GET("api/users")
    Call<List<User>> getUsers(@Header("Authorization") String token);

    @POST("api/users")
    Call<User> createUser(
            @Header("Authorization") String token,
            @Body User user
    );

    @PUT("api/users/{id}")
    Call<User> updateUser(@Header("Authorization") String token, @Path("id") int id, @Body User user);

}
