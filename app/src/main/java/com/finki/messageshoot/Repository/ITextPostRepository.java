package com.finki.messageshoot.Repository;

import com.finki.messageshoot.Model.TextPost;
import com.finki.messageshoot.Repository.Callbacks.OnTextPostSuccessfullyAdded;
import com.finki.messageshoot.Repository.Callbacks.OnTextPostSuccessfullyDeleted;
import com.finki.messageshoot.Repository.Callbacks.OnTextPostsLoaded;

public interface ITextPostRepository {

    void listAll(OnTextPostsLoaded onTextPostsLoaded);
    void add(String email, String nickname, String url, String content, OnTextPostSuccessfullyAdded onTextPostSuccessfullyAdded);
    void delete(TextPost textPost, OnTextPostSuccessfullyDeleted onTextPostSuccessfullyDeleted);
}
