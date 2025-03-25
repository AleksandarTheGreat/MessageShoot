package com.finki.messageshoot.View.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.finki.messageshoot.Model.User;
import com.finki.messageshoot.R;
import com.finki.messageshoot.Repository.Utils.NetworkUtils;
import com.finki.messageshoot.View.Fragments.CustomFragmentManager;
import com.finki.messageshoot.View.Fragments.FragmentHome;
import com.finki.messageshoot.View.Fragments.FragmentInput;
import com.finki.messageshoot.View.Fragments.FragmentTextPosts;
import com.finki.messageshoot.View.Interfaces.IEssentials;
import com.finki.messageshoot.ViewModel.ViewModelTextPost;
import com.finki.messageshoot.ViewModel.ViewModelUsers;
import com.finki.messageshoot.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MainActivity extends AppCompatActivity implements IEssentials {

    private ActivityMainBinding binding;
    private ViewModelUsers viewModelUsers;
    private ViewModelTextPost viewModelTextPost;
    private FirebaseAuth firebaseAuth;
    private AppCompatActivity appCompatActivity;
    private Context context;
    private FragmentHome fragmentHome;
    private FragmentTextPosts fragmentTextPosts;
    private FragmentInput fragmentInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        instantiateObjects();
        addEventListeners();
        additionalThemeChanges();

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });
    }

    @Override
    public void instantiateObjects() {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Go to Login activity ?

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        appCompatActivity = this;
        context = this;


        fragmentHome = FragmentHome.newInstance("Aleksandar", "Mitrevski");
        fragmentHome.setActivityMainBinding(binding);

        fragmentTextPosts = FragmentTextPosts.newInstance();
        fragmentTextPosts.setActivityMainBinding(binding);

        fragmentInput = FragmentInput.newInstance("Aleks", "The great");
        fragmentInput.setActivityMainBinding(binding);
        fragmentInput.setFragmentTextPosts(fragmentTextPosts);


        CustomFragmentManager.changeFragment(this, binding, fragmentHome, false);

        viewModelUsers = new ViewModelProvider(this).get(ViewModelUsers.class);
        viewModelUsers.loadAllUsers();
        viewModelUsers.loadCurrentUser();

        viewModelTextPost = new ViewModelProvider(this).get(ViewModelTextPost.class);
        viewModelTextPost.init(this, this);
        // I can list all textPosts here instead of FragmentTextPosts
        // But this also contradicts always loading new text posts

        int paddingLeft = binding.navigationViewMainActivity.getPaddingLeft();
        int paddingRight = binding.navigationViewMainActivity.getPaddingRight();
        Log.d("Tag", "Padding left: " + paddingLeft);
        Log.d("Tag", "Padding right: " + paddingRight);
    }

    @Override
    public void addEventListeners() {
        binding.navigationViewMainActivity.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.menuItemChats){
                    CustomFragmentManager.changeFragment(appCompatActivity, binding, fragmentHome, false);
                } else if (item.getItemId() == R.id.menuItemPosts){
                    CustomFragmentManager.changeFragment(appCompatActivity, binding, fragmentTextPosts, false);
                }
                return true;
            }
        });

        binding.floatingActionButtonMainActivity.setOnClickListener(view -> {
            CustomFragmentManager.changeFragment(appCompatActivity, binding, fragmentInput, false);
        });

        // This seems to be called even when instantiating
        NetworkUtils.getInstance().registerCallback(context, binding);
    }

    @Override
    public void additionalThemeChanges() {

    }
}