package com.finki.messageshoot.View.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.finki.messageshoot.Model.User;
import com.finki.messageshoot.Repository.Callbacks.UsersLoadedCallback;
import com.finki.messageshoot.Repository.UserRepository;
import com.finki.messageshoot.View.Fragments.CustomFragmentManager;
import com.finki.messageshoot.View.Fragments.FragmentChat;
import com.finki.messageshoot.View.Fragments.FragmentHome;
import com.finki.messageshoot.View.Interfaces.IEssentials;
import com.finki.messageshoot.ViewModel.MyViewModel;
import com.finki.messageshoot.databinding.ActivityMainBinding;
import com.finki.messageshoot.databinding.FragmentChatBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class MainActivity extends AppCompatActivity implements IEssentials {

    private ActivityMainBinding binding;
    private MyViewModel myViewModel;
    private FirebaseAuth firebaseAuth;

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
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Go to Login activity ?
        // hello, najglupi se ovie dvata zms, najjglupi

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        FragmentHome fragmentHome = FragmentHome.newInstance("Aleksandar", "Mitrevski");
        CustomFragmentManager.changeFragment(this, binding, fragmentHome);

        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);
        myViewModel.loadAllUsers();
    }

    @Override
    public void addEventListeners() {

    }

    @Override
    public void additionalThemeChanges() {

    }
}