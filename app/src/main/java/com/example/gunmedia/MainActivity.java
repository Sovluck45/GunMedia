package com.example.gunmedia;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import com.example.gunmedia.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Инициализация NavController
        initNavigation();

        // Скрываем ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    private void initNavigation() {
        // Получаем NavHostFragment
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();

            // Настраиваем нижнюю навигацию
            BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
            NavigationUI.setupWithNavController(bottomNav, navController);

            bottomNav.setOnItemSelectedListener(item -> {
                if (item.getItemId() != navController.getCurrentDestination().getId()) {
                    navController.popBackStack(item.getItemId(), false);
                    navController.navigate(item.getItemId());
                }
                return true;
            });

            // Проверяем флаг после инициализации навигации
            if (getIntent().getBooleanExtra("SHOW_PROFILE", false)) {
                // Используем post для гарантии готовности View
                binding.getRoot().post(() ->
                        navController.navigate(R.id.nav_profile));
            }
        }
    }
}