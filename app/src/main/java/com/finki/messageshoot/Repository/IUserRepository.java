package com.finki.messageshoot.Repository;

import com.finki.messageshoot.Repository.Callbacks.OnCurrentUserLoadedCallback;
import com.finki.messageshoot.Repository.Callbacks.UsersLoadedCallback;

public interface IUserRepository {
    void listAll(UsersLoadedCallback usersLoadedCallback);
    void findCurrentUser(OnCurrentUserLoadedCallback onCurrentUserLoadedCallback);
}
