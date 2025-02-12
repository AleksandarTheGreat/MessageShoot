package com.finki.messageshoot.ViewModel;

import android.content.Context;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.finki.messageshoot.Model.TextPost;
import com.finki.messageshoot.Repository.Callbacks.OnTextPostsLoaded;
import com.finki.messageshoot.Repository.Implementations.TextPostRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ViewModelTextPost extends ViewModel {

    private final MutableLiveData<List<TextPost>> mutableLiveData = new MutableLiveData<>();
    private TextPostRepository textPostRepository;

    public void init(Context context, FragmentActivity fragmentActivity) {
        this.textPostRepository = new TextPostRepository(context, fragmentActivity);
    }


    public void listAll() {
        this.textPostRepository.listAll(textPostList -> mutableLiveData.setValue(textPostList));
    }

    public void add(String email, String nickname, String url, String content) {
        this.textPostRepository.add(email, nickname, url, content);
    }

    public void delete(TextPost textPost){
        this.textPostRepository.delete(textPost);
    }


    public MutableLiveData<List<TextPost>> getMutableLiveData() {
        return mutableLiveData;
    }
}





