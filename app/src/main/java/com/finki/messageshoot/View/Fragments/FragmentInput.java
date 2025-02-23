package com.finki.messageshoot.View.Fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.finki.messageshoot.Model.User;
import com.finki.messageshoot.R;
import com.finki.messageshoot.Repository.Callbacks.OnTextPostSuccessfullyAdded;
import com.finki.messageshoot.View.Interfaces.IEssentials;
import com.finki.messageshoot.ViewModel.ViewModelTextPost;
import com.finki.messageshoot.ViewModel.ViewModelUsers;
import com.finki.messageshoot.databinding.ActivityMainBinding;
import com.finki.messageshoot.databinding.FragmentInputBinding;
import com.squareup.picasso.Picasso;

public class FragmentInput extends Fragment implements IEssentials {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private FragmentInputBinding binding;
    private FragmentTextPosts fragmentTextPosts;
    private ActivityMainBinding activityMainBinding;
    private ViewModelUsers viewModelUsers;
    private ViewModelTextPost viewModelTextPost;

    public FragmentInput() {
        // Required empty public constructor
    }

    public static FragmentInput newInstance(String param1, String param2) {
        FragmentInput fragment = new FragmentInput();
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
        binding = FragmentInputBinding.bind(inflater.inflate(R.layout.fragment_input, container, false));

        instantiateObjects();
        addEventListeners();

        return binding.getRoot();
    }

    @Override
    public void instantiateObjects() {
        viewModelTextPost = new ViewModelProvider(requireActivity()).get(ViewModelTextPost.class);

        viewModelUsers = new ViewModelProvider(requireActivity()).get(ViewModelUsers.class);
        viewModelUsers.getMutableLiveDataCurrentUser().observe(requireActivity(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user.getProfilePictureUrl() == null || user.getProfilePictureUrl().isEmpty())
                    binding.imageViewProfileFragmentInput.setImageResource(R.drawable.ic_profile);
                else Picasso.get().load(user.getProfilePictureUrl()).into(binding.imageViewProfileFragmentInput);

                binding.textViewEmailFragmentInput.setText(user.getEmail());
                binding.textViewNicknameFragmentInput.setText(user.getNickname());
            }
        });
    }

    @Override
    public void addEventListeners() {
        binding.buttonShareFragmentInput.setOnClickListener(view -> {
            String content = binding.textInputEditTextShareFragmentInput.getText().toString().trim();
            if (content.isEmpty()){
                Toast.makeText(getContext(), "Please enter something", Toast.LENGTH_SHORT).show();
                return;
            }

            User user = viewModelUsers.getMutableLiveDataCurrentUser().getValue();
            if (user == null){
                Toast.makeText(getContext(), "Somehow user is null??", Toast.LENGTH_SHORT).show();
                return;
            }

            viewModelTextPost.add(user.getEmail(), user.getNickname(), user.getProfilePictureUrl(), content, new OnTextPostSuccessfullyAdded() {
                @Override
                public void onAdded(boolean success) {
                    if (success){
                        Toast.makeText(getContext(), "Shared successfully", Toast.LENGTH_SHORT).show();
                        CustomFragmentManager.changeFragment((AppCompatActivity) requireActivity(), activityMainBinding, fragmentTextPosts, false);
                    } else {
                        Toast.makeText(getContext(), "Failed to share", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });

        binding.buttonCancelFragmentInput.setOnClickListener(view -> {
            binding.textInputEditTextShareFragmentInput.setText("");
        });
    }

    @Override
    public void additionalThemeChanges() {

    }

    public void setActivityMainBinding(ActivityMainBinding activityMainBinding){
        this.activityMainBinding = activityMainBinding;
    }

    public void setFragmentTextPosts(FragmentTextPosts fragmentTextPosts) {
        this.fragmentTextPosts = fragmentTextPosts;
    }
}