package com.finki.messageshoot.Model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class User {

    private String email;
    private String nickname;
    private String bio;
    private String profilePictureUrl;
    private String coverPictureUrl;

    public User(String email, String nickname, String bio, String profilePictureUrl, String coverPictureUrl) {
        this.email = email;
        this.nickname = nickname;
        this.bio = bio;
        this.profilePictureUrl = profilePictureUrl;
        this.coverPictureUrl = coverPictureUrl;
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

    public String getCoverPictureUrl() {
        return coverPictureUrl;
    }

    public void setCoverPictureUrl(String coverPictureUrl) {
        this.coverPictureUrl = coverPictureUrl;
    }
}
