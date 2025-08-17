package com.sena.businessassistantandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.sena.businessassistantandroid.network.RetrofitClient;
import com.sena.businessassistantandroid.network.UserApi;
import com.sena.businessassistantandroid.users.User;
import com.sena.businessassistantandroid.users.UsersAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsersActivity extends AppCompatActivity implements UsersAdapter.Callbacks {

    private static final String PREFS = "ba_prefs";
    private static final String KEY_NIGHT_MODE = "night_mode";

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageButton btnMenu, btnThemeToggle;
    private View btnAddUser;
    private RecyclerView rv;
    private UsersAdapter adapter;
    private List<User> users = new ArrayList<>();

    private UserApi userApi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        SharedPreferences sp = getSharedPreferences(PREFS, MODE_PRIVATE);
        int savedMode = sp.getInt(KEY_NIGHT_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        AppCompatDelegate.setDefaultNightMode(savedMode);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        drawerLayout   = findViewById(R.id.drawer_layout_users);
        navigationView = findViewById(R.id.navigation_view_users);
        btnMenu        = findViewById(R.id.btnMenuUsers);
        btnThemeToggle = findViewById(R.id.btnThemeToggle);
        btnAddUser     = findViewById(R.id.btnAddUser);
        rv             = findViewById(R.id.rvUsers);

        btnMenu.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(navigationView)) {
                drawerLayout.closeDrawer(navigationView);
            } else {
                drawerLayout.openDrawer(navigationView);
            }
        });

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, WelcomeActivity.class));
            } else if (id == R.id.nav_users) {
                // ya estamos aquí
            }
            drawerLayout.closeDrawer(navigationView);
            return true;
        });

        updateDarkModeIcon(savedMode);
        btnThemeToggle.setOnClickListener(v -> toggleTheme());

        userApi = RetrofitClient.getInstance().create(UserApi.class);

        adapter = new UsersAdapter(users, this);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        loadUsers();

        btnAddUser.setOnClickListener(v -> showUserForm(null));
    }

    private void loadUsers() {
        SharedPreferences sp = getSharedPreferences("app_prefs", MODE_PRIVATE);
        String token = sp.getString("auth_token", null);

        if (token == null) {
            Toast.makeText(this, "No hay token, inicia sesión primero", Toast.LENGTH_SHORT).show();
            return;
        }

        userApi.getUsers("Bearer " + token).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    users.clear();
                    users.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(UsersActivity.this, "Error al obtener usuarios", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(UsersActivity.this, "Fallo de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void toggleTheme() {
        int current = AppCompatDelegate.getDefaultNightMode();
        int next = (current == AppCompatDelegate.MODE_NIGHT_YES)
                ? AppCompatDelegate.MODE_NIGHT_NO
                : AppCompatDelegate.MODE_NIGHT_YES;

        getSharedPreferences(PREFS, MODE_PRIVATE)
                .edit()
                .putInt(KEY_NIGHT_MODE, next)
                .apply();

        AppCompatDelegate.setDefaultNightMode(next);
        recreate();
    }

    private void updateDarkModeIcon(int mode) {
        btnThemeToggle.setImageResource(
                (mode == AppCompatDelegate.MODE_NIGHT_YES)
                        ? R.drawable.ic_light_mode_24
                        : R.drawable.ic_dark_mode_24
        );
    }

    @Override
    public void onEdit(User u) {
        showUserForm(u);
    }

    @Override
    public void onDelete(User u) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete)
                .setMessage(R.string.confirm_delete)
                .setPositiveButton(R.string.delete, (d, w) -> {
                    SharedPreferences sp = getSharedPreferences("app_prefs", MODE_PRIVATE);
                    String token = sp.getString("auth_token", null);

                    if (token == null) {
                        Toast.makeText(this, "No hay token, inicia sesión primero", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    userApi.deleteUser("Bearer " + token, u.id).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                users.remove(u);
                                adapter.notifyDataSetChanged();
                                Toast.makeText(UsersActivity.this, "Usuario eliminado con éxito", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(UsersActivity.this, "Error al eliminar usuario", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(UsersActivity.this, "Fallo de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    public void onActivate(User u) {
        String info = "Nombre: " + u.name + "\n"
                + "Email: " + u.email + "\n"
                + "Rol: " + u.role;

        new AlertDialog.Builder(this)
                .setTitle(R.string.user_info)
                .setMessage(info)
                .setPositiveButton(R.string.ok, null)
                .show();
    }

    private void showUserForm(@Nullable User userToEdit) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_user_form, null, false);
        TextInputEditText etName      = view.findViewById(R.id.etName);
        TextInputEditText etEmail     = view.findViewById(R.id.etEmail);
        TextInputEditText etRole      = view.findViewById(R.id.etRole);
        TextInputEditText etPassword  = view.findViewById(R.id.etPassword);

        boolean editing = userToEdit != null;

        if (editing) {
            etName.setText(userToEdit.name);
            etEmail.setText(userToEdit.email);
            etRole.setText(userToEdit.role);
            etPassword.setVisibility(View.GONE);
        }

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(editing ? R.string.user_edit : R.string.users_add)
                .setView(view)
                .setPositiveButton(R.string.save, null)
                .setNegativeButton(R.string.cancel, (d, w) -> d.dismiss())
                .create();

        dialog.setOnShowListener(dlg -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                String name  = etName.getText()  != null ? etName.getText().toString().trim()  : "";
                String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
                String role  = etRole.getText()  != null ? etRole.getText().toString().trim()  : "";
                String pass  = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(role)) {
                    Toast.makeText(this, R.string.fill_all_fields, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!editing && TextUtils.isEmpty(pass)) {
                    Toast.makeText(this, R.string.must_enter_password, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (editing) {
                    updateUserOnApi(userToEdit.id, name, email, role, dialog);
                } else {
                    createUserOnApi(name, email, role, pass, dialog);
                }
            });
        });

        dialog.show();
    }

    private void createUserOnApi(String name, String email, String role, String password, AlertDialog dialog) {
        SharedPreferences sp = getSharedPreferences("app_prefs", MODE_PRIVATE);
        String token = sp.getString("auth_token", null);

        if (token == null) {
            Toast.makeText(this, "No hay token, inicia sesión primero", Toast.LENGTH_SHORT).show();
            return;
        }

        User newUser = new User(null, name, email, role, password);

        userApi.createUser("Bearer " + token, newUser).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    loadUsers();
                    Toast.makeText(UsersActivity.this, "Usuario creado con éxito", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    try {
                        String err = response.errorBody() != null ? response.errorBody().string() : "Error al crear usuario";
                        Toast.makeText(UsersActivity.this, err, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(UsersActivity.this, "Error al crear usuario", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(UsersActivity.this, "Fallo de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUserOnApi(int id, String name, String email, String role, AlertDialog dialog) {
        SharedPreferences sp = getSharedPreferences("app_prefs", MODE_PRIVATE);
        String token = sp.getString("auth_token", null);

        if (token == null) {
            Toast.makeText(this, "No hay token, inicia sesión primero", Toast.LENGTH_SHORT).show();
            return;
        }

        User updatedUser = new User(id, name, email, role, null);

        userApi.updateUser("Bearer " + token, id, updatedUser).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    loadUsers();
                    Toast.makeText(UsersActivity.this, "Usuario actualizado con éxito", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    try {
                        String err = response.errorBody() != null ? response.errorBody().string() : "Error al actualizar usuario";
                        Toast.makeText(UsersActivity.this, err, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(UsersActivity.this, "Error al actualizar usuario", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(UsersActivity.this, "Fallo de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
