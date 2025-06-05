package com.example.gunmedia.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.gunmedia.MainActivity;
import com.example.gunmedia.R;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        Button registerButton = findViewById(R.id.btn_register);
        Button loginButton = findViewById(R.id.btn_login);
        TextView versionText = findViewById(R.id.version_text);

        // Версия приложения
        versionText.setText("Версия 0.1.0");

        // Переход на регистрацию
        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(AuthActivity.this, RegistrationPart1Activity.class);
            startActivity(intent);
        });

        // Переход в приложение
        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(AuthActivity.this, MainActivity.class);
            intent.putExtra("SHOW_PROFILE", true);
            startActivity(intent);
            finish();
        });
    }
}