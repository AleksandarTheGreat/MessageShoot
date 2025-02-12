package com.finki.messageshoot.View.Activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.finki.messageshoot.Repository.Implementations.AuthenticationRepository;
import com.finki.messageshoot.View.Interfaces.IEssentials;
import com.finki.messageshoot.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity implements IEssentials {

    private ActivityLoginBinding binding;
    private AuthenticationRepository authenticationRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        instantiateObjects();
        addEventListeners();
        additionalThemeChanges();

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void instantiateObjects() {
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authenticationRepository = new AuthenticationRepository(LoginActivity.this, binding);
    }

    @Override
    public void addEventListeners() {
        binding.buttonLogin.setOnClickListener(view -> {
            String email = binding.textInputEditTextEmail.getText().toString().trim();
            String password = binding.textInputEditTextPassword.getText().toString().trim();

            authenticationRepository.loginUser(email, password);
        });
    }

    @Override
    public void additionalThemeChanges() {

    }
}


