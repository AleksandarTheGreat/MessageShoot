package com.finki.messageshoot.Repository.Implementations;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.finki.messageshoot.View.Activities.MainActivity;
import com.finki.messageshoot.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AuthenticationRepository {

    private Context context;
    private ActivityLoginBinding activityLoginBinding;
    private FirebaseAuth firebaseAuth;

    public AuthenticationRepository(Context context, ActivityLoginBinding activityLoginBinding){
        this.context = context;
        this.activityLoginBinding = activityLoginBinding;

        this.firebaseAuth = FirebaseAuth.getInstance();

    }

    public void loginUser(String email, String password){
        if (!validateEmail(email)){
            activityLoginBinding.textLayoutEmail.setError("Invalid email address");
            return;
        }

        activityLoginBinding.textLayoutEmail.setError("");
        if (!validatePassword(password)){
            activityLoginBinding.textLayoutPassword.setError("Password is too short");
            return;
        }

        activityLoginBinding.textLayoutPassword.setError("");
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(context, "Successfully logged in", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(context, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Tag", e.getLocalizedMessage());
                        Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean validateEmail(String email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean validatePassword(String password){
        return password.length() >= 8;
    }

}
