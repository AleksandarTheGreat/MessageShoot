package com.finki.messageshoot.View.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.finki.messageshoot.Model.Helper.ThemeUtils;
import com.finki.messageshoot.Model.User;
import com.finki.messageshoot.R;
import com.finki.messageshoot.View.Activities.LoginActivity;
import com.finki.messageshoot.View.Adapters.UserAdapter;
import com.finki.messageshoot.View.Interfaces.IEssentials;
import com.finki.messageshoot.ViewModel.ViewModelUsers;
import com.finki.messageshoot.databinding.ActivityMainBinding;
import com.finki.messageshoot.databinding.FragmentHomeBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.stream.Collectors;


public class FragmentHome extends Fragment implements IEssentials {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private FragmentHomeBinding binding;
    private ViewModelUsers viewModelUsers;
    private boolean isNightModeOn;
    private UserAdapter customAdapter;
    private FirebaseAuth firebaseAuth;

    private ActivityMainBinding activityMainBinding;

    public FragmentHome() {
        // Required empty public constructor
    }

    public static FragmentHome newInstance(String param1, String param2) {
        FragmentHome fragment = new FragmentHome();
        Bundle args = new Bundle();

        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.bind(inflater.inflate(R.layout.fragment_home, container, false));

        instantiateObjects();
        addEventListeners();
        additionalThemeChanges();

        return binding.getRoot();
    }

    @Override
    public void instantiateObjects() {
        isNightModeOn = ThemeUtils.isNightModeOn(getContext());

        viewModelUsers = new ViewModelProvider(requireActivity()).get(ViewModelUsers.class);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void addEventListeners() {
        viewModelUsers.getMutableLiveDataCurrentUser().observe(requireActivity(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user.getProfilePictureUrl() == null || user.getProfilePictureUrl().isEmpty())
                    binding.imageViewProfile.setImageResource(R.drawable.ic_profile);
                else Picasso.get().load(user.getProfilePictureUrl()).into(binding.imageViewProfile);
            }
        });

        viewModelUsers.getMutableLiveDataUsers().observe(requireActivity(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                customAdapter = new UserAdapter(getContext(), activityMainBinding, (AppCompatActivity) getActivity(), users);

                binding.recyclerViewFragmentHome.setLayoutManager(new LinearLayoutManager(getContext()));
                binding.recyclerViewFragmentHome.setHasFixedSize(true);
                binding.recyclerViewFragmentHome.setAdapter(customAdapter);

                binding.swipeRefreshLayout.setRefreshing(false);
            }
        });

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModelUsers.loadAllUsers();
                Log.d("Tag", "This is called?");
            }
        });

        binding.searchViewFragmentHome.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<User> allUsers = viewModelUsers.getMutableLiveDataUsers().getValue();
                List<User> filteredUsers = allUsers
                        .stream()
                        .filter(user -> user.getEmail().contains(newText) || user.getNickname().contains(newText))
                        .collect(Collectors.toList());

                viewModelUsers.getMutableLiveDataUsers().setValue(filteredUsers);
                return true;
            }
        });

        binding.imageViewProfile.setOnClickListener(view -> {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
            builder.setTitle("Log out")
                    .setMessage("Are you sure you want to log out?")
                    .setIcon(R.drawable.ic_exit)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            firebaseAuth.signOut();

                            Intent intentGoToLogin = new Intent(getContext(), LoginActivity.class);
                            intentGoToLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intentGoToLogin);

                            Toast.makeText(getContext(), "Signing out", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                        dialog.dismiss();
                        Toast.makeText(getContext(), "That's what I thought", Toast.LENGTH_SHORT).show();
                    })
                    .setCancelable(true)
                    .show();
        });
    }

    @Override
    public void additionalThemeChanges() {

    }

    public void setActivityMainBinding(ActivityMainBinding activityMainBinding){
        this.activityMainBinding = activityMainBinding;
    }
}