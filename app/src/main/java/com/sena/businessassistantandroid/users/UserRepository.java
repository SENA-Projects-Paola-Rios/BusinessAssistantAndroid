package com.sena.businessassistantandroid.users;

import android.util.Log;

import com.sena.businessassistantandroid.network.RetrofitClient;
import com.sena.businessassistantandroid.network.UserApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {

    private final UserApi api;

    public UserRepository() {
        api = RetrofitClient.getInstance().create(UserApi.class);
    }

    public void fetchUsers(String token, final UserCallback callback) {
        api.getUsers("Bearer " + token).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e("UserRepository", "Error fetching users", t);
                callback.onError(t.getMessage());
            }
        });
    }

    public interface UserCallback {
        void onSuccess(List<User> users);
        void onError(String error);
    }
}
