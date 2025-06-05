package com.example.gunmedia.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.appcompat.app.AppCompatActivity;
import com.example.gunmedia.MainActivity;
import com.example.gunmedia.R;

public class RegistrationPart2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_part2);

        Button backButton = findViewById(R.id.btn_back);
        Button finishButton = findViewById(R.id.btn_finish);
        EditText firstNameInput = findViewById(R.id.first_name_input);
        EditText lastNameInput = findViewById(R.id.last_name_input);
        EditText nicknameInput = findViewById(R.id.nickname_input);
        RadioGroup genderGroup = findViewById(R.id.gender_group);
        EditText birthDateInput = findViewById(R.id.birth_date_input);
        EditText regionInput = findViewById(R.id.region_input);
        EditText cityInput = findViewById(R.id.city_input);

        // Возврат на предыдущий экран
        backButton.setOnClickListener(v -> finish());

        // Завершение регистрации
        finishButton.setOnClickListener(v -> {
            int selectedId = genderGroup.getCheckedRadioButtonId();
            RadioButton selectedGender = selectedId != -1 ? findViewById(selectedId) : null;
            String gender = selectedGender != null ? selectedGender.getText().toString() : "";

            // Сохранение данных пользователя (заглушка)
            // В реальном приложении здесь будет отправка на сервер

            // Переход в приложение
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("SHOW_PROFILE", true);
            startActivity(intent);
            finishAffinity(); // Закрыть все предыдущие активности
        });
    }
}