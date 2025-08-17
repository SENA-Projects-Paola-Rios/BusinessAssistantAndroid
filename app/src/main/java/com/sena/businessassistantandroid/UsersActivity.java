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
import com.sena.businessassistantandroid.users.FakeUserRepository;
import com.sena.businessassistantandroid.users.User;
import com.sena.businessassistantandroid.users.UsersAdapter;

import java.util.List;

public class UsersActivity extends AppCompatActivity implements UsersAdapter.Callbacks {

    private static final String PREFS = "ba_prefs";
    private static final String KEY_NIGHT_MODE = "night_mode";

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageButton btnMenu, btnThemeToggle;
    private View btnAddUser;
    private RecyclerView rv;
    private UsersAdapter adapter;
    private FakeUserRepository repo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // Aplicar tema guardado antes de cargar UI
        SharedPreferences sp = getSharedPreferences(PREFS, MODE_PRIVATE);
        int savedMode = sp.getInt(KEY_NIGHT_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        AppCompatDelegate.setDefaultNightMode(savedMode);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        // Referencias UI
        drawerLayout   = findViewById(R.id.drawer_layout_users);
        navigationView = findViewById(R.id.navigation_view_users);
        btnMenu        = findViewById(R.id.btnMenuUsers);
        btnThemeToggle = findViewById(R.id.btnThemeToggle);
        btnAddUser     = findViewById(R.id.btnAddUser);
        rv             = findViewById(R.id.rvUsers);

        // Botón abrir/cerrar menú lateral
        btnMenu.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(navigationView)) {
                drawerLayout.closeDrawer(navigationView);
            } else {
                drawerLayout.openDrawer(navigationView);
            }
        });

        // Manejo de clics en el menú lateral
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, WelcomeActivity.class));
            } else if (id == R.id.nav_users) {
                // Ya estamos en UsersActivity
            }
            drawerLayout.closeDrawer(navigationView);
            return true;
        });

        // Modo claro/oscuro
        // Actualizar icono según el tema guardado
        updateDarkModeIcon(savedMode);
        btnThemeToggle.setOnClickListener(v -> toggleTheme());

        // Lista de usuarios
        repo = new FakeUserRepository();
        List<User> data = repo.getAll();

        adapter = new UsersAdapter(data, this);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        // Botón agregar usuario
        btnAddUser.setOnClickListener(v -> showUserForm(null));
    }

    /**
     * Alternar entre modo claro y oscuro
     */
    private void toggleTheme() {
        int current = AppCompatDelegate.getDefaultNightMode();
        int next = (current == AppCompatDelegate.MODE_NIGHT_YES)
                ? AppCompatDelegate.MODE_NIGHT_NO
                : AppCompatDelegate.MODE_NIGHT_YES;

        // Guardar en preferencias
        getSharedPreferences(PREFS, MODE_PRIVATE)
                .edit()
                .putInt(KEY_NIGHT_MODE, next)
                .apply();

        // Aplicar tema
        AppCompatDelegate.setDefaultNightMode(next);

        // Recargar para aplicar cambios inmediatamente
        recreate();
    }

    /**
     * Actualizar icono según el modo actual
     */
    private void updateDarkModeIcon(int mode) {
        btnThemeToggle.setImageResource(
                (mode == AppCompatDelegate.MODE_NIGHT_YES)
                        ? R.drawable.ic_light_mode_24
                        : R.drawable.ic_dark_mode_24
        );
    }

    // ---- Callbacks del adapter ----

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
                    repo.delete(u);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(this, R.string.deleted_success, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    public void onActivate(User u) {
        // Mostrar modal con info del usuario
        String info = "Nombre: " + u.name + "\n"
                + "Email: " + u.email + "\n"
                + "Rol: " + u.role;

        new AlertDialog.Builder(this)
                .setTitle(R.string.user_info)
                .setMessage(info)
                .setPositiveButton(R.string.ok, null)
                .show();
    }

    // ---- Formulario Crear/Editar ----
    private void showUserForm(@Nullable User userToEdit) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_user_form, null, false);
        TextInputEditText etName      = view.findViewById(R.id.etName);
        TextInputEditText etEmail     = view.findViewById(R.id.etEmail);
        TextInputEditText etRole      = view.findViewById(R.id.etRole);
        TextInputEditText etPassword  = view.findViewById(R.id.etPassword);

        boolean editing = userToEdit != null;

        // Si estoy editando, oculto la contraseña
        if (editing) {
            etName.setText(userToEdit.name);
            etEmail.setText(userToEdit.email);
            etRole.setText(userToEdit.role);
            etPassword.setVisibility(View.GONE);
        }

        new AlertDialog.Builder(this)
                .setTitle(editing ? R.string.user_edit : R.string.users_add)
                .setView(view)
                .setPositiveButton(R.string.save, (dialog, which) -> {
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
                        userToEdit.name = name;
                        userToEdit.email = email;
                        userToEdit.role = role;
                        repo.update(userToEdit);
                    } else {
                        repo.add(new User(0, name, email, role, pass));
                    }
                    adapter.notifyDataSetChanged();
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }
}
