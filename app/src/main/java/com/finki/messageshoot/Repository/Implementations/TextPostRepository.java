package com.finki.messageshoot.Repository.Implementations;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.finki.messageshoot.Model.Comment;
import com.finki.messageshoot.Model.TextPost;
import com.finki.messageshoot.R;
import com.finki.messageshoot.Repository.Callbacks.OnTextPostSuccessfullyAdded;
import com.finki.messageshoot.Repository.Callbacks.OnTextPostSuccessfullyDeleted;
import com.finki.messageshoot.Repository.Callbacks.OnTextPostsLoaded;
import com.finki.messageshoot.Repository.ITextPostRepository;
import com.finki.messageshoot.View.Adapters.TextPostAdapter;
import com.finki.messageshoot.View.Fragments.CustomFragmentManager;
import com.finki.messageshoot.ViewModel.ViewModelTextPost;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firestore.v1.UpdateDocumentRequestOrBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TextPostRepository implements ITextPostRepository {

    private Context context;
    private FirebaseDatabase firebaseDatabase;
    private ViewModelTextPost viewModelTextPost;
    private FragmentActivity fragmentActivity;

    public TextPostRepository(Context context, FragmentActivity fragmentActivity) {
        this.context = context;
        this.firebaseDatabase = FirebaseDatabase.getInstance("https://social101-12725-default-rtdb.europe-west1.firebasedatabase.app");
        this.fragmentActivity = fragmentActivity;
        this.viewModelTextPost = new ViewModelProvider(fragmentActivity).get(ViewModelTextPost.class);
    }

    @Override
    public void listAll(OnTextPostsLoaded onTextPostsLoaded) {
        DatabaseReference databaseReference = firebaseDatabase.getReference("/textPosts");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    onTextPostsLoaded.onLoaded(new ArrayList<>());
                    return;
                }

                List<TextPost> textPostList = new ArrayList<>();

                // All emails
                // first level of collections
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    // All posts per user
                    // second level of collections
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        long id = ds.child("id").getValue(Long.class);
                        String email = ds.child("email").getValue(String.class);
                        String nickname = ds.child("nickname").getValue(String.class);
                        String profilePicUrl = ds.child("profilePicUrl").getValue(String.class);
                        String content = ds.child("content").getValue(String.class);

                        // read textPost postedAt
                        int year = ds.child("/postedAt").child("year").getValue(Integer.class);
                        int month = ds.child("postedAt").child("monthValue").getValue(Integer.class);
                        int day = ds.child("/postedAt").child("dayOfMonth").getValue(Integer.class);
                        int hour = ds.child("postedAt").child("hour").getValue(Integer.class);
                        int minute = ds.child("postedAt").child("minute").getValue(Integer.class);
                        int second = ds.child("postedAt").child("second").getValue(Integer.class);

                        LocalDateTime postedAt = null;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                             postedAt = LocalDateTime.of(year, month, day, hour, minute, second);
                        }

                        List<String> listLikes = new ArrayList<>();
                        for (DataSnapshot likesSnapshot: ds.child("/listLikes").getChildren()){
                            listLikes.add(likesSnapshot.getValue(String.class));
                        }

                        List<Comment> commentList = new ArrayList<>();
                        for (DataSnapshot commentSnapshot: ds.child("/commentList").getChildren()){
                            long commentId = commentSnapshot.child("id").getValue(Long.class);
                            String comment_email = commentSnapshot.child("email").getValue(String.class);
                            String comment_content = commentSnapshot.child("content").getValue(String.class);
                            String comment_profile_picture = commentSnapshot.child("profilePicUrl").getValue(String.class);

                            // read Comment postedAtDateTime
                            int cYear = commentSnapshot.child("postedAtDateTime").child("year").getValue(Integer.class);
                            int cMonth = commentSnapshot.child("postedAtDateTime").child("monthValue").getValue(Integer.class);
                            int cDay = commentSnapshot.child("/postedAtDateTime").child("dayOfMonth").getValue(Integer.class);
                            int cHour = commentSnapshot.child("postedAtDateTime").child("hour").getValue(Integer.class);
                            int cMinute = commentSnapshot.child("/postedAtDateTime").child("minute").getValue(Integer.class);
                            int cSecond = commentSnapshot.child("postedAtDateTime").child("second").getValue(Integer.class);

                            LocalDateTime postedAtDateTime = null;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                postedAtDateTime = LocalDateTime.of(cYear, cMonth, cDay, cHour, cMinute, cSecond);
                            }

                            Comment comment = new Comment(commentId, comment_email, comment_content, comment_profile_picture, postedAtDateTime);
                            commentList.add(comment);
                        }

                        TextPost textPost = new TextPost(id, email, nickname, profilePicUrl, content, postedAt, listLikes, commentList);
                        textPostList.add(textPost);
                    }
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
    public void add(String email, String nickname, String url, String content, OnTextPostSuccessfullyAdded onTextPostSuccessfullyAdded) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            long id = System.currentTimeMillis();
            LocalDateTime localDateTime = LocalDateTime.now();

            TextPost textPost = TextPost.createTextPostForSaving(id, email, nickname, url, content, localDateTime);

            String path = "/textPosts/" + email + "/" + id;
            path = path.replace(".", ":::");

            DatabaseReference databaseReference = firebaseDatabase.getReference(path);
            databaseReference.setValue(textPost)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("Tag", "Successfully added text post");

                            List<String> listLikes = new ArrayList<>();
                            listLikes.add(email);

                            DatabaseReference dbRef = databaseReference.child("listLikes");
                            dbRef.setValue(listLikes)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d("Tag", "Successfully added list");
                                            onTextPostSuccessfullyAdded.onAdded(true);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d("Tag", e.getLocalizedMessage());
                                            onTextPostSuccessfullyAdded.onAdded(false);
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("Tag", e.getLocalizedMessage());
                            onTextPostSuccessfullyAdded.onAdded(false);
                        }
                    });
        }
    }

    @Override
    public void delete(TextPost textPost, OnTextPostSuccessfullyDeleted onTextPostSuccessfullyDeleted) {
        String path = textPost.endpointPath();

        DatabaseReference databaseReference = firebaseDatabase.getReference(path);
        databaseReference.removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("Tag", "Successfully deleted post with id" + textPost.getId());
                        onTextPostSuccessfullyDeleted.onDeleted(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Tag", e.getLocalizedMessage());
                        onTextPostSuccessfullyDeleted.onDeleted(false);
                    }
                });
    }

}
