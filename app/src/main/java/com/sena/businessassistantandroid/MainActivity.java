package com.sena.businessassistantandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS = "ba_prefs";
    private static final String KEY_NIGHT_MODE = "night_mode";

    private ImageButton btnThemeToggle;
    private TextInputEditText etEmail, etPassword;
    private MaterialButton btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Aplica el tema guardado antes de dibujar vistas
        SharedPreferences sp = getSharedPreferences(PREFS, MODE_PRIVATE);
        int savedMode = sp.getInt(KEY_NIGHT_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        AppCompatDelegate.setDefaultNightMode(savedMode);

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Ajuste de insets (de la plantilla)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Referencias a vistas
        btnThemeToggle = findViewById(R.id.btnThemeToggle);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        // Icono del toggle según tema actual
        setToggleIconForMode(savedMode);

        // Toggle de tema claro/oscuro
        btnThemeToggle.setOnClickListener(v -> {
            int current = AppCompatDelegate.getDefaultNightMode();
            int nextMode = (current == AppCompatDelegate.MODE_NIGHT_YES)
                    ? AppCompatDelegate.MODE_NIGHT_NO
                    : AppCompatDelegate.MODE_NIGHT_YES;

            // guardar preferencia
            getSharedPreferences(PREFS, MODE_PRIVATE)
                    .edit()
                    .putInt(KEY_NIGHT_MODE, nextMode)
                    .apply();

            AppCompatDelegate.setDefaultNightMode(nextMode);
            // actualiza ícono y recrea para aplicar colores
            setToggleIconForMode(nextMode);
            recreate();
        });

        // Acción del botón "Ingresar" (validación básica)
        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
            String pass  = etPassword.getText() != null ? etPassword.getText().toString() : "";

            if (TextUtils.isEmpty(email)) {
                etEmail.setError("Ingrese el usuario (email)");
                etEmail.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(pass)) {
                etPassword.setError("Ingrese la contraseña");
                etPassword.requestFocus();
                return;
            }

            // Aquí luego se hará la conexión con el backend.
            Toast.makeText(MainActivity.this, "Login exitoso", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
            startActivity(intent);
            finish(); 
        });
    }

    private void setToggleIconForMode(int mode) {
        if (btnThemeToggle == null) return;
        if (mode == AppCompatDelegate.MODE_NIGHT_YES) {
            btnThemeToggle.setImageResource(R.drawable.ic_light_mode_24); // sol
        } else {
            btnThemeToggle.setImageResource(R.drawable.ic_dark_mode_24);  // luna
        }
    }
}
