package com.finki.messageshoot.Repository.Implementations;

import android.util.Log;

import androidx.annotation.NonNull;

import com.finki.messageshoot.Model.User;
import com.finki.messageshoot.Repository.Callbacks.OnCurrentUserLoadedCallback;
import com.finki.messageshoot.Repository.Callbacks.UsersLoadedCallback;
import com.finki.messageshoot.Repository.IUserRepository;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserRepository implements IUserRepository {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private static final String COLLECTION_USERS = "Users";

    public UserRepository(){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    @Override
    public void listAll(UsersLoadedCallback usersLoadedCallback){
        firebaseFirestore.collection(COLLECTION_USERS)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<User> users = new ArrayList<>();
                        List<DocumentSnapshot> documentSnapshots = queryDocumentSnapshots.getDocuments();

                        for (DocumentSnapshot documentSnapshot: documentSnapshots){
                            Map<String, Object> userMap = (Map<String, Object>) documentSnapshot.get("user");

                            String email = (String) userMap.get("email");
                            String nickname = (String) userMap.get("nickname");
                            String bio = (String) userMap.get("bio");
                            String profilePictureUrl = (String) userMap.get("profilePhotoUrl");
                            String coverPictureUrl = (String) userMap.get("coverPhotoUrl");

                            User user = new User(email, nickname, bio, profilePictureUrl, coverPictureUrl);
                            users.add(user);
                        }

                        usersLoadedCallback.onUsersLoaded(users);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Tag", e.getLocalizedMessage());
                    }
                });
    }

    @Override
    public void findCurrentUser(OnCurrentUserLoadedCallback onCurrentUserLoadedCallback) {
        String email = firebaseAuth.getCurrentUser().getEmail();

        DocumentReference documentReference = firebaseFirestore.collection(COLLECTION_USERS).document(email);
        documentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Map<String, Object> userMap = (Map<String, Object>) documentSnapshot.get("user");

                        String email = (String) userMap.get("email");
                        String nickname = (String) userMap.get("nickname");
                        String bio = (String) userMap.get("bio");
                        String profilePictureUrl = (String) userMap.get("profilePhotoUrl");
                        String coverPictureUrl = (String) userMap.get("coverPhotoUrl");

                        User user = new User(email, nickname, bio, profilePictureUrl, coverPictureUrl);
                        onCurrentUserLoadedCallback.onCurrentUserLoaded(user);
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
