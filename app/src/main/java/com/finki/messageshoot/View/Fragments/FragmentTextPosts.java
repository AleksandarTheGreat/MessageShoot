package com.finki.messageshoot.View.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.finki.messageshoot.R;
import com.finki.messageshoot.View.Interfaces.IEssentials;
import com.finki.messageshoot.databinding.FragmentTextPostsBinding;

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

    }

    @Override
    public void addEventListeners() {
        binding.fabAddTextPost.setOnClickListener(view -> {
            Toast.makeText(getContext(), "Hello there", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void additionalThemeChanges() {

    }
}