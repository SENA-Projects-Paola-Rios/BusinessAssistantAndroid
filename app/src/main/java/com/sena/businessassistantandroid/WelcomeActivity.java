package com.sena.businessassistantandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class WelcomeActivity extends AppCompatActivity {

    private static final String PREFS = "ba_prefs";
    private static final String KEY_NIGHT_MODE = "night_mode";

    private DrawerLayout drawerLayout;
    private ImageButton menuButton, darkModeButton;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 🔹 Aplicar tema guardado
        SharedPreferences sp = getSharedPreferences(PREFS, MODE_PRIVATE);
        int savedMode = sp.getInt(KEY_NIGHT_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        AppCompatDelegate.setDefaultNightMode(savedMode);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Referencias
        drawerLayout = findViewById(R.id.drawer_layout);
        menuButton = findViewById(R.id.menu_button);
        darkModeButton = findViewById(R.id.dark_mode_button);
        navigationView = findViewById(R.id.navigation_view);

        // Actualiza ícono según el modo actual
        updateDarkModeIcon(AppCompatDelegate.getDefaultNightMode());

        // Toggle para abrir/cerrar menú lateral
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Abre menú lateral desde botón
        menuButton.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(navigationView)) {
                drawerLayout.closeDrawer(navigationView);
            } else {
                drawerLayout.openDrawer(navigationView);
            }
        });

        // Botón de modo oscuro
        darkModeButton.setOnClickListener(v -> {
            int current = AppCompatDelegate.getDefaultNightMode();
            int nextMode = (current == AppCompatDelegate.MODE_NIGHT_YES)
                    ? AppCompatDelegate.MODE_NIGHT_NO
                    : AppCompatDelegate.MODE_NIGHT_YES;

            // Guardar preferencia
            getSharedPreferences(PREFS, MODE_PRIVATE)
                    .edit()
                    .putInt(KEY_NIGHT_MODE, nextMode)
                    .apply();

            AppCompatDelegate.setDefaultNightMode(nextMode);

            // Actualizar ícono sin necesidad de recrear
            updateDarkModeIcon(nextMode);
        });

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_users) {
                startActivity(new Intent(this, UsersActivity.class));
                drawerLayout.closeDrawer(navigationView);
                return true;
            }
            return false;
        });

    }

    private void updateDarkModeIcon(int mode) {
        if (mode == AppCompatDelegate.MODE_NIGHT_YES) {
            darkModeButton.setImageResource(R.drawable.ic_light_mode_24);
        } else {
            darkModeButton.setImageResource(R.drawable.ic_dark_mode_24);
        }
    }
}
