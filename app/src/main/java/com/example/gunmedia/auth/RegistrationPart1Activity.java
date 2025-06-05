package com.example.gunmedia.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.example.gunmedia.R;

public class RegistrationPart1Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_part1);

        Button backButton = findViewById(R.id.btn_back);
        Button continueButton = findViewById(R.id.btn_continue);
        EditText emailInput = findViewById(R.id.email_input);
        EditText phoneInput = findViewById(R.id.phone_input);
        EditText passwordInput = findViewById(R.id.password_input);
        EditText confirmPasswordInput = findViewById(R.id.confirm_password_input);
        CheckBox newsletterCheckbox = findViewById(R.id.newsletter_checkbox);

        // Возврат на предыдущий экран
        backButton.setOnClickListener(v -> finish());

        // Продолжение регистрации
        continueButton.setOnClickListener(v -> {
            // Проверка паролей
            if (!passwordInput.getText().toString().equals(confirmPasswordInput.getText().toString())) {
                confirmPasswordInput.setError("Пароли не совпадают");
                return;
            }

            // Переход на второй этап регистрации
            Intent intent = new Intent(this, RegistrationPart2Activity.class);
            intent.putExtra("EMAIL", emailInput.getText().toString());
            intent.putExtra("PHONE", phoneInput.getText().toString());
            intent.putExtra("PASSWORD", passwordInput.getText().toString());
            intent.putExtra("NEWSLETTER", newsletterCheckbox.isChecked());
            startActivity(intent);
        });
    }
}