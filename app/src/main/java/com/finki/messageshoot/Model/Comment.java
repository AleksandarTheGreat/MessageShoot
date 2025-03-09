package com.finki.messageshoot.Model;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.time.LocalDateTime;

public class Comment {

    private long id;
    private String email;
    private String content;
    private String profilePicUrl;
    private String postedAt;
    private LocalDateTime postedAtDateTime;

    private Comment(){

    }
    public Comment(long id, String email, String content, String profilePicUrl, String postedAt) {
        this.id = id;
        this.email = email;
        this.content = content;
        this.profilePicUrl = profilePicUrl;
        this.postedAt = postedAt;
    }

    public static Comment createCommentForSaving(long id, String email, String content, String profilePicUrl, String postedAt){
        Comment comment = new Comment();

        comment.setId(id);
        comment.setEmail(email);
        comment.setContent(content);
        comment.setProfilePicUrl(profilePicUrl);
        comment.setPostedAt(postedAt);

        return comment;
    }

    @SuppressLint("DefaultLocale")
    @NonNull
    @Override
    public String toString() {
        return String.format("Comment id: %d\nEmail: %s\nContent: %s\nProfile pic url: %s\nPosted at: %s", id, email, content, profilePicUrl, postedAt);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (getClass() == null || obj.getClass() == null)
            return false;
        else if (getClass() != obj.getClass())
            return false;
        return id == ((Comment) obj).id;
    }

    @SuppressLint("NewApi")
    private LocalDateTime transformToDateTime(String postedAt){
        String [] parts = postedAt.split("/.");

        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);

        int hour = Integer.parseInt(parts[3]);
        int minute = Integer.parseInt(parts[4]);
        int seconds = Integer.parseInt(parts[5]);

        return LocalDateTime.of(year, month, day, hour, minute, seconds);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public String getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(String postedAt) {
        this.postedAt = postedAt;
    }

    public LocalDateTime getPostedAtDateTime() {
        return postedAtDateTime;
    }

    public void setPostedAtDateTime(LocalDateTime postedAtDateTime) {
        this.postedAtDateTime = postedAtDateTime;
    }
}









