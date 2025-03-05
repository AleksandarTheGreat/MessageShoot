package com.finki.messageshoot.View.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.finki.messageshoot.databinding.ActivityMainBinding;
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
    private ActivityMainBinding activityMainBinding;
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
        viewModelTextPost.listAll();

        viewModelTextPost.getMutableLiveData().observe(requireActivity(), new Observer<List<TextPost>>() {
            @Override
            public void onChanged(List<TextPost> textPosts) {
                List<TextPost> sortedTextPosts = textPosts.stream()
                        .sorted(Comparator.comparing(TextPost::getPostedAt).reversed())
                        .collect(Collectors.toList());

                textPostAdapter = new TextPostAdapter(getContext(), sortedTextPosts, viewModelTextPost, viewModelUsers);

                binding.recyclerViewTextPosts.setLayoutManager(new LinearLayoutManager(getContext()));
                binding.recyclerViewTextPosts.setHasFixedSize(true);
                binding.recyclerViewTextPosts.setAdapter(textPostAdapter);

                binding.swipeRefreshLayoutFragmentTextPosts.setRefreshing(false);
            }
        });
    }

    @Override
    public void addEventListeners() {
        /**
         * This is generic, works for any kind of data change, but it is too much, since reloads everything
         * I need to update just the changed data, not reload everything...
         * So now I will try to apply listeners for the children, not the whole DB.
         *
         *
         * It seems successful, but I still need to do stuff
         */

//        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://social101-12725-default-rtdb.europe-west1.firebasedatabase.app");
//        DatabaseReference databaseReference = firebaseDatabase.getReference();
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                viewModelTextPost.listAll();
//                Toast.makeText(getContext(), "If this is called I swear..." , Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.d("Tag", error.getMessage());
//            }
//        });

        binding.swipeRefreshLayoutFragmentTextPosts.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModelTextPost.listAll();
            }
        });
    }

    @Override
    public void additionalThemeChanges() {

    }

    public void setActivityMainBinding(ActivityMainBinding activityMainBinding) {
        this.activityMainBinding = activityMainBinding;
    }
}