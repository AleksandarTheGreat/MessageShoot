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
import com.finki.messageshoot.Repository.Callbacks.OnTextPostSuccessfullyAdded;
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
                        String postedAtString = ds.child("postedAtString").getValue(String.class);

                        List<String> listLikes = new ArrayList<>();
                        for (DataSnapshot childLikes: ds.child("/listLikes").getChildren()){
                            listLikes.add(childLikes.getValue(String.class));
                        }

                        TextPost textPost = new TextPost(id, email, nickname, profilePicUrl, content, postedAtString, listLikes);
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
            ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("Adding...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            long id = System.currentTimeMillis();
            LocalDateTime localDateTime = LocalDateTime.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.HH.mm.ss");
            String postedAtString = localDateTime.format(dateTimeFormatter);

            TextPost textPost = TextPost.createTextPostForSaving(id, email, nickname, url, content, postedAtString);

            String path = email + "/" + id;
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
                                            progressDialog.dismiss();
                                            onTextPostSuccessfullyAdded.onAdded(true);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d("Tag", e.getLocalizedMessage());
                                            progressDialog.dismiss();
                                            onTextPostSuccessfullyAdded.onAdded(false);
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("Tag", e.getLocalizedMessage());
                            progressDialog.dismiss();
                            onTextPostSuccessfullyAdded.onAdded(false);
                        }
                    });
        }
    }

    @Override
    public void delete(TextPost textPost) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Deleting...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String idd = textPost.getId() + "";
        String path = textPost.getEmail() + "/" + idd;
        path = path.replace(".", ":::");

        DatabaseReference databaseReference = firebaseDatabase.getReference(path);
        databaseReference.removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("Tag", "Successfully deleted post with id" + idd);
                        progressDialog.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Tag", e.getLocalizedMessage());
                        progressDialog.dismiss();
                    }
                });
    }

}
