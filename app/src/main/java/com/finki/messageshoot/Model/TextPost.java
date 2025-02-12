package com.finki.messageshoot.Model;

import android.annotation.SuppressLint;
import android.os.Build;

import androidx.annotation.Nullable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TextPost {

    private long id;
    private String email;
    private String nickname;
    private String profilePicUrl;
    private String content;
    private LocalDateTime postedAt;
    private String postedAtString;
    private List<String> likesList;
    private List<Comment> commentsList;


    public TextPost(long id, String email, String nickname, String profilePicUrl, String content, String postedAtString, List<String> likesList) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.profilePicUrl = profilePicUrl;
        this.content = content;
        this.postedAtString = postedAtString;
        this.postedAt = parseDateTime(postedAtString);
        this.likesList = likesList;
    }

    private TextPost() {

    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (getClass() == null || obj.getClass() == null)
            return false;
        else if (getClass() != obj.getClass())
            return false;
        return id == ((TextPost) obj).id;
    }

    private LocalDateTime parseDateTime(String postedAtString){
        String [] parts = postedAtString.split("\\.");

        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);

        int hour = Integer.parseInt(parts[3]);
        int minute = Integer.parseInt(parts[4]);
        int second = Integer.parseInt(parts[5]);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return LocalDateTime.of(year, month, day, hour, minute, second);
        }
        return null;
    }

    @SuppressLint("NewApi")
    public String goodLookingDateTimeFormat(){
        @SuppressLint({"NewApi", "LocalSuppress"})
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy ' at ' HH:mm 'h'");
        return this.postedAt.format(dateTimeFormatter);
    }

    public static TextPost createTextPostForSaving(long id, String email, String nickname, String profilePicUrl, String content, String postedAtString){
        TextPost textPost = new TextPost();

        textPost.setId(id);
        textPost.setEmail(email);
        textPost.setNickname(nickname);
        textPost.setProfilePicUrl(profilePicUrl);
        textPost.setContent(content);
        textPost.setPostedAtString(postedAtString);

        return textPost;
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

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(LocalDateTime postedAt) {
        this.postedAt = postedAt;
    }

    public List<String> getLikesList() {
        return likesList;
    }

    public void setLikesList(List<String> likesList) {
        this.likesList = likesList;
    }

    public List<Comment> getCommentsList() {
        return commentsList;
    }

    public void setCommentsList(List<Comment> commentsList) {
        this.commentsList = commentsList;
    }

    public String getPostedAtString() {
        return postedAtString;
    }

    public void setPostedAtString(String postedAtString) {
        this.postedAtString = postedAtString;
    }
}
