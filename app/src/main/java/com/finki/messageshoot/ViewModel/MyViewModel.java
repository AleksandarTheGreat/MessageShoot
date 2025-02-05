package com.finki.messageshoot.ViewModel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.finki.messageshoot.Model.User;
import com.finki.messageshoot.Repository.Callbacks.UsersLoadedCallback;
import com.finki.messageshoot.Repository.UserRepository;

import java.util.List;

public class MyViewModel extends ViewModel {

    private final UserRepository userRepository = new UserRepository();
    private final MutableLiveData<List<User>> mutableLiveDataUsers = new MutableLiveData<>();

    public void loadAllUsers(){
        userRepository.listAll(new UsersLoadedCallback() {
            @Override
            public void onUsersLoaded(List<User> users) {
                mutableLiveDataUsers.setValue(users);
                Log.d("Tag", "Users have been loaded from firebase");
                Log.d("Tag", users.toString());
            }
        });
    }

    public MutableLiveData<List<User>> getMutableLiveDataUsers(){
        return mutableLiveDataUsers;
    }

}
