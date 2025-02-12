package com.finki.messageshoot.Repository.Callbacks;

import com.finki.messageshoot.Model.User;

public interface OnCurrentUserLoadedCallback {
    void onCurrentUserLoaded(User user);
}
