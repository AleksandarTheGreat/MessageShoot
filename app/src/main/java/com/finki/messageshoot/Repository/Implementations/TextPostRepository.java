package com.finki.messageshoot.Repository.Implementations;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.finki.messageshoot.Model.TextPost;
import com.finki.messageshoot.Repository.Callbacks.OnTextPostsLoaded;
import com.finki.messageshoot.Repository.ITextPostRepository;
import com.finki.messageshoot.View.Adapters.TextPostAdapter;
import com.finki.messageshoot.ViewModel.ViewModelTextPost;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TextPostRepository implements ITextPostRepository {

    private Context context;
    private FirebaseDatabase firebaseDatabase;
    private ViewModelTextPost viewModelTextPost;
    private FragmentActivity fragmentActivity;
    public TextPostRepository(Context context, FragmentActivity fragmentActivity){
        this.context = context;
        this.firebaseDatabase = FirebaseDatabase.getInstance("https://social101-12725-default-rtdb.europe-west1.firebasedatabase.app/");
        this.fragmentActivity = fragmentActivity;
        this.viewModelTextPost = new ViewModelProvider(fragmentActivity).get(ViewModelTextPost.class);
    }

    @Override
    public void listAll(OnTextPostsLoaded onTextPostsLoaded) {
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<TextPost> textPostList = new ArrayList<>();

                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    String key = dataSnapshot.getKey();

                    long id = dataSnapshot.child("id").getValue(Long.class);
                    String email = dataSnapshot.child("email").getValue(String.class);
                    String nickname = dataSnapshot.child("nickname").getValue(String.class);
                    String profilePicUrl = dataSnapshot.child("profilePicUrl").getValue(String.class);
                    String content = dataSnapshot.child("content").getValue(String.class);
                    String postedAt = dataSnapshot.child("postedAtString").getValue(String.class);

                    TextPost textPost = new TextPost(id, email, nickname, profilePicUrl, content, postedAt, new ArrayList<>());
                    textPostList.add(textPost);
                }

                onTextPostsLoaded.onLoaded(textPostList);
                Log.d("Tag", "Loaded '" + textPostList.size() + "' text posts");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Tag", error.getMessage());
            }
        });
    }

    @Override
    public void add(String email, String nickname, String url, String content) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            long id = System.currentTimeMillis();
            LocalDateTime localDateTime = LocalDateTime.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.hh.mm.ss");
            String postedAtString = localDateTime.format(dateTimeFormatter);

            List<String> likesList = new ArrayList<>();
            TextPost textPost = new TextPost(id, email, nickname, url, content, postedAtString, likesList);

            String idd = id + "";
            DatabaseReference databaseReference = firebaseDatabase.getReference(idd);
            databaseReference.setValue(textPost)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            // This adds duplicates
                            // I'll remove it for now

//                            List<TextPost> textPostList = viewModelTextPost.getMutableLiveData().getValue();
//                            textPostList.add(textPost);
//                            viewModelTextPost.getMutableLiveData().setValue(textPostList);

                            Log.d("Tag", "Successfully added text post");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("Tag", e.getLocalizedMessage());
                        }
                    });
        }
    }

    @Override
    public void delete(TextPost textPost) {
        String idd = textPost.getId() + "";
        DatabaseReference databaseReference = firebaseDatabase.getReference(idd);
        databaseReference.removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
//                        List<TextPost> listTextPosts = viewModelTextPost.getMutableLiveData().getValue();
//                        listTextPosts.remove(textPost);
//                        viewModelTextPost.getMutableLiveData().setValue(listTextPosts);

                        Log.d("Tag", "Successfully deleted post with id" + idd);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Tag", e.getLocalizedMessage());
                    }
                });
    }

}
