package com.example.gunmedia.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.gunmedia.auth.AuthActivity;
import com.example.gunmedia.databinding.FragmentProfileBinding;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        firebaseAuth = FirebaseAuth.getInstance();

        // Обработка выхода из аккаунта
        binding.btnLogout.setOnClickListener(v -> logoutUser());

        // Обработка изменения пароля
        binding.btnChangePassword.setOnClickListener(v -> {
            // Здесь будет переход на экран изменения пароля
        });

        return binding.getRoot();
    }

    private void logoutUser() {
        if (firebaseAuth != null) {
            firebaseAuth.signOut();
            redirectToAuthScreen();
        }
    }

    private void redirectToAuthScreen() {
        Intent intent = new Intent(requireActivity(), AuthActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        requireActivity().finish();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}