package com.finki.messageshoot.Model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class User {

    private String email;
    private String nickname;
    private String bio;
    private String profilePictureUrl;

    public User(String email, String nickname, String bio, String profilePictureUrl) {
        this.email = email;
        this.nickname = nickname;
        this.bio = bio;
        this.profilePictureUrl = profilePictureUrl;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("Email: %s\nNickname: %s\nBio: %s\n", email, nickname, bio);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (getClass() != obj.getClass())
            return false;
        return email.equals(((User) obj).email);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }
}
