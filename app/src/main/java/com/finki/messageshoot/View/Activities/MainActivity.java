package com.finki.messageshoot.View.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.finki.messageshoot.Model.User;
import com.finki.messageshoot.R;
import com.finki.messageshoot.Repository.Callbacks.UsersLoadedCallback;
import com.finki.messageshoot.Repository.UserRepository;
import com.finki.messageshoot.View.Fragments.CustomFragmentManager;
import com.finki.messageshoot.View.Fragments.FragmentChat;
import com.finki.messageshoot.View.Fragments.FragmentHome;
import com.finki.messageshoot.View.Fragments.FragmentTextPosts;
import com.finki.messageshoot.View.Interfaces.IEssentials;
import com.finki.messageshoot.ViewModel.MyViewModel;
import com.finki.messageshoot.databinding.ActivityMainBinding;
import com.finki.messageshoot.databinding.FragmentChatBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

public class MainActivity extends AppCompatActivity implements IEssentials {

    private ActivityMainBinding binding;
    private MyViewModel myViewModel;
    private FirebaseAuth firebaseAuth;
    private AppCompatActivity appCompatActivity;
    private Context context;

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

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        appCompatActivity = this;
        context = this;

        FragmentHome fragmentHome = FragmentHome.newInstance("Aleksandar", "Mitrevski");
        fragmentHome.setActivityMainBinding(binding);
        CustomFragmentManager.changeFragment(this, binding, fragmentHome, false);

        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);
        myViewModel.loadAllUsers();

        int paddingLeft = binding.navigationViewMainActivity.getPaddingLeft();
        int paddingRight = binding.navigationViewMainActivity.getPaddingRight();
        Log.d("Tag", "Padding left: " + paddingLeft);
        Log.d("Tag", "Padding right: " + paddingRight);
    }

    @Override
    public void addEventListeners() {
        myViewModel.getMutableLiveDataUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                String email = firebaseAuth.getCurrentUser().getEmail();

                for (User user : users) {
                    if (email.equals(user.getEmail())) {
                        ImageView imageViewCover = findViewById(R.id.imageViewCoverPhotoHeaderLayout);
                        TextView textViewNickname = findViewById(R.id.textViewNicknameHeaderLayout);
                        TextView textViewEmail = findViewById(R.id.textViewEmailHeaderLayout);
                        TextView textViewNicknameLabel = findViewById(R.id.textViewNicknameLabel);
                        TextView textViewEmailLabel = findViewById(R.id.textViewEmailLabel);

                        if (user.getCoverPictureUrl() == null || user.getCoverPictureUrl().isEmpty()) {
                            imageViewCover.setImageResource(R.mipmap.ic_launcher);
                        } else {
                            Picasso.get().load(user.getCoverPictureUrl()).into(imageViewCover);
                        }

                        if (user.getNickname() != null && !user.getNickname().isEmpty()) {
                            textViewNickname.setText(user.getNickname());
                        } else {
                            textViewNickname.setText("No nickname :(");
                        }

                        textViewEmail.setText(user.getEmail());

                        textViewEmail.setTextColor(ContextCompat.getColor(context, R.color.white));
                        textViewEmailLabel.setTextColor(ContextCompat.getColor(context, R.color.white));
                        textViewNickname.setTextColor(ContextCompat.getColor(context, R.color.white));
                        textViewNicknameLabel.setTextColor(ContextCompat.getColor(context, R.color.white));

                        break;
                    }
                }

            }
        });

        binding.navigationViewMainActivity.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.menuItem2) {

                    FragmentTextPosts fragmentTextPosts = FragmentTextPosts.newInstance();
                    CustomFragmentManager.changeFragment(appCompatActivity, binding, fragmentTextPosts, true);

                } else if (itemId == R.id.menuItem3) {
                    Toast.makeText(MainActivity.this, "but if you pour water on a rock, nothing happens", Toast.LENGTH_SHORT).show();
                } else if (itemId == R.id.menuItem4) {
                    Toast.makeText(MainActivity.this, "but if you pour water on a rock time after time after time, nothing happens", Toast.LENGTH_SHORT).show();
                } else if (itemId == R.id.menuItem5) {
                    Toast.makeText(MainActivity.this, "Item 5", Toast.LENGTH_SHORT).show();
                } else if (itemId == R.id.menuItem6) {
                    Toast.makeText(MainActivity.this, "Item 6", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "I don't know what you clicked", Toast.LENGTH_SHORT).show();
                }

                binding.drawerLayoutMainActivity.closeDrawer(GravityCompat.START);
                return false;
            }
        });
    }

    @Override
    public void additionalThemeChanges() {

    }
}