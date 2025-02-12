package com.finki.messageshoot.Repository.Callbacks;

import com.finki.messageshoot.Model.TextPost;

import java.util.List;

public interface OnTextPostsLoaded {
    void onLoaded(List<TextPost> textPostList);
}
