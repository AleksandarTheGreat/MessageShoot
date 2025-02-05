package com.finki.messageshoot.Repository.Callbacks;

import com.finki.messageshoot.Model.User;

import java.util.List;

public interface UsersLoadedCallback {
    void onUsersLoaded(List<User> users);
}
