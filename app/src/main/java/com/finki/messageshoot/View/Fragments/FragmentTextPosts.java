package com.finki.messageshoot.View.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.finki.messageshoot.Model.TextPost;
import com.finki.messageshoot.Model.User;
import com.finki.messageshoot.R;
import com.finki.messageshoot.Repository.Implementations.TextPostRepository;
import com.finki.messageshoot.View.Adapters.TextPostAdapter;
import com.finki.messageshoot.View.Interfaces.IEssentials;
import com.finki.messageshoot.ViewModel.ViewModelTextPost;
import com.finki.messageshoot.ViewModel.ViewModelUsers;
import com.finki.messageshoot.databinding.FragmentTextPostsBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.dialog.MaterialDialogs;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentTextPosts#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentTextPosts extends Fragment implements IEssentials {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private FragmentTextPostsBinding binding;
    private ViewModelTextPost viewModelTextPost;
    private ViewModelUsers viewModelUsers;
    private TextPostAdapter textPostAdapter;

    public FragmentTextPosts() {
        // Required empty public constructor
    }

    public static FragmentTextPosts newInstance() {
        FragmentTextPosts fragment = new FragmentTextPosts();

        Bundle args = new Bundle();
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
        binding = FragmentTextPostsBinding.bind(inflater.inflate(R.layout.fragment_text_posts, container, false));

        instantiateObjects();
        addEventListeners();
        additionalThemeChanges();

        return binding.getRoot();
    }

    @Override
    public void instantiateObjects() {
        viewModelUsers = new ViewModelProvider(requireActivity()).get(ViewModelUsers.class);

        viewModelTextPost = new ViewModelProvider(requireActivity()).get(ViewModelTextPost.class);
        viewModelTextPost.init(getContext(), requireActivity());
        viewModelTextPost.listAll();

        viewModelTextPost.getMutableLiveData().observe(requireActivity(), new Observer<List<TextPost>>() {
            @Override
            public void onChanged(List<TextPost> textPosts) {
                List<TextPost> sortedTextPosts = textPosts.stream()
                        .sorted(Comparator.comparing(TextPost::getPostedAt).reversed())
                        .collect(Collectors.toList());

                textPostAdapter = new TextPostAdapter(getContext(), sortedTextPosts, viewModelTextPost);

                binding.recyclerViewTextPosts.setLayoutManager(new LinearLayoutManager(getContext()));
                binding.recyclerViewTextPosts.setHasFixedSize(true);
                binding.recyclerViewTextPosts.setAdapter(textPostAdapter);
            }
        });
    }

    @Override
    public void addEventListeners() {
        binding.fabAddTextPost.setOnClickListener(view -> {
            LinearLayout.LayoutParams layoutParamsInput = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParamsInput.setMargins(60, 0, 60, 0);

            TextInputEditText textInputEditText = new TextInputEditText(getContext());
            textInputEditText.setLayoutParams(layoutParamsInput);
            textInputEditText.setHint("Share what's on your mind...");
            textInputEditText.setTextSize(14);

            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
            builder.setTitle("Text post")
                    .setIcon(R.mipmap.ic_launcher)
                    .setMessage("Enter a text post")
                    .setView(textInputEditText)
                    .setPositiveButton("Share with us", (dialog, which) -> {
                        String input = textInputEditText.getText().toString().trim();
                        if (input.isEmpty()) {
                            Toast.makeText(getContext(), "Please enter some input", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        User currentUser = viewModelUsers.getMutableLiveDataCurrentUser().getValue();
                        if (currentUser == null){
                            Toast.makeText(getContext(), "Current user is not loaded", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        viewModelTextPost.add(currentUser.getEmail(), currentUser.getNickname(), currentUser.getProfilePictureUrl(), input);
                    })
                    .setCancelable(true)
                    .show();
        });

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://social101-12725-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                viewModelTextPost.listAll();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Tag", error.getMessage());
            }
        });

    }

    @Override
    public void additionalThemeChanges() {

    }
}