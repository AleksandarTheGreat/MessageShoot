package com.finki.messageshoot.View.Adapters;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.finki.messageshoot.Model.Comment;
import com.finki.messageshoot.Model.TextPost;
import com.finki.messageshoot.Model.User;
import com.finki.messageshoot.R;
import com.finki.messageshoot.Repository.Callbacks.OnTextPostSuccessfullyDeleted;
import com.finki.messageshoot.ViewModel.ViewModelTextPost;
import com.finki.messageshoot.ViewModel.ViewModelUsers;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.dialog.MaterialDialogs;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import kotlin.jvm.internal.Lambda;

public class TextPostAdapter extends RecyclerView.Adapter<TextPostAdapter.MyViewHolder> {

    private Context context;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private List<TextPost> textPostList;
    private ViewModelTextPost viewModelTextPost;
    private ViewModelUsers viewModelUsers;
    private User currentUser;
    private String currentEmail;
    private TextPostAdapter currentAdapter;
    private Handler handler;
    private static final String FIREBASE_DATABASE_URL = "https://social101-12725-default-rtdb.europe-west1.firebasedatabase.app/";

    public TextPostAdapter(Context context, List<TextPost> textPostList, ViewModelTextPost viewModelTextPost, ViewModelUsers viewModelUsers) {
        this.context = context;
        this.textPostList = textPostList;
        this.viewModelTextPost = viewModelTextPost;
        this.viewModelUsers = viewModelUsers;
        this.currentUser = viewModelUsers.getMutableLiveDataCurrentUser().getValue();

        this.firebaseAuth = FirebaseAuth.getInstance();
        this.firebaseDatabase = FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL);
        this.currentEmail = firebaseAuth.getCurrentUser().getEmail();

        this.currentAdapter = this;
        this.handler = new Handler(Looper.getMainLooper());
    }

    @NonNull
    @Override
    public TextPostAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.single_text_post, parent, false);

        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TextPostAdapter.MyViewHolder holder, int position) {
        TextPost textPost = textPostList.get(position);

        setUpTheUiViewHolder(holder, textPost);
        setUpBasicEventListeners(holder, textPost);

        holder.createValueEventListener(firebaseDatabase, currentAdapter, textPostList, textPost, position, handler);
    }

    @Override
    public int getItemCount() {
        return textPostList.size();
    }


    @Override
    public void onViewRecycled(@NonNull MyViewHolder holder) {
        super.onViewRecycled(holder);

        int position = holder.getAdapterPosition();
        if (position != -1) {
            TextPost textPost = textPostList.get(position);
            holder.removeValueEventListener(firebaseDatabase, textPost);
        }
    }


    static class MyViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imageViewDelete;
        protected ImageView imageViewProfilePic;
        protected TextView textViewEmail;
        protected TextView textViewNickname;
        protected TextView textViewContent;
        protected TextView textViewPostedAt;
        protected TextView textViewLikes;
        protected TextView textViewComments;
        protected ImageView imageViewLikes;
        protected ImageView imageViewComments;
        protected ValueEventListener valueEventListener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            this.imageViewDelete = itemView.findViewById(R.id.imageViewDeleteTextPost);
            this.imageViewProfilePic = itemView.findViewById(R.id.imageViewTextPostProfilePic);
            this.textViewEmail = itemView.findViewById(R.id.textViewEmailTextPost);
            this.textViewNickname = itemView.findViewById(R.id.textViewNicknameTextPost);
            this.textViewContent = itemView.findViewById(R.id.textViewContentTextPost);
            this.textViewPostedAt = itemView.findViewById(R.id.textViewPostedAtTextPost);
            this.textViewLikes = itemView.findViewById(R.id.textViewLikesTextPost);
            this.textViewComments = itemView.findViewById(R.id.textViewCommentsTextPost);
            this.imageViewLikes = itemView.findViewById(R.id.imageViewLikeTextPost);
            this.imageViewComments = itemView.findViewById(R.id.imageViewCommentTextPost);
        }

        public void createValueEventListener(FirebaseDatabase firebaseDatabase,
                                             TextPostAdapter textPostAdapter,
                                             List<TextPost> textPostList,
                                             TextPost textPost,
                                             int position,
                                             Handler handler) {

            String path = textPost.endpointPath();
            DatabaseReference databaseReference = firebaseDatabase.getReference(path);
            valueEventListener = new ValueEventListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // remove from the adapter list
                    // remove from the UI aka viewHolder
                    if (!snapshot.exists()) {
                        textPostList.remove(position);
                        textPostAdapter.notifyDataSetChanged();

                        // Maybe remove the valueEventListener here somehow,
                        // when the textPost and view are deleted.

                        return;
                    }

                    long id = snapshot.child("id").getValue(Long.class);
                    String content = snapshot.child("content").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    String nickname = snapshot.child("nickname").getValue(String.class);
                    String postedAt = snapshot.child("postedAtString").getValue(String.class);
                    String profilePicture = snapshot.child("profilePicUrl").getValue(String.class);

                    List<String> listLikes = new ArrayList<>();
                    for (DataSnapshot likesChild : snapshot.child("/listLikes").getChildren()) {
                        listLikes.add(likesChild.getValue(String.class));
                    }

                    List<Comment> commentList = new ArrayList<>();
                    for (DataSnapshot commentSnapshot : snapshot.child("/commentList").getChildren()) {
                        long commentId = commentSnapshot.child("id").getValue(Long.class);
                        String comment_email = commentSnapshot.child("email").getValue(String.class);
                        String comment_content = commentSnapshot.child("content").getValue(String.class);
                        String comment_profile_picture = commentSnapshot.child("profilePicUrl").getValue(String.class);
                        String comment_posted_at = commentSnapshot.child("postedAt").getValue(String.class);

                        Comment comment = Comment.createCommentForSaving(commentId, comment_email, comment_content, comment_profile_picture, comment_posted_at);
                        commentList.add(comment);
                    }

                    TextPost tp = new TextPost(id, email, nickname, profilePicture, content, postedAt, listLikes, commentList);
                    TextPost currentTp = textPostList.get(position);

                    if (!tp.equals(currentTp)) {
                        textPostList.set(position, tp);
                        handler.post(() -> {
                            textPostAdapter.notifyItemChanged(position);
                        });
                        Log.d("Tag", "Changes have happened");
                    } else {
                        Log.d("Tag", "Nothing new has happened");
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("Tag", "Canceled: " + error.getMessage());
                }
            };

            databaseReference.addValueEventListener(valueEventListener);
            Log.d("Tag", "Listener added");
        }

        public void removeValueEventListener(FirebaseDatabase firebaseDatabase, TextPost textPost) {
            String path = textPost.endpointPath();

            DatabaseReference databaseReference = firebaseDatabase.getReference(path);
            databaseReference.removeEventListener(valueEventListener);

            Log.d("Tag", "Listener removed");
        }
    }

    @SuppressLint("SetTextI18n")
    private void setUpTheUiViewHolder(MyViewHolder holder, TextPost textPost) {
        holder.textViewEmail.setText(textPost.getEmail());
        holder.textViewNickname.setText(textPost.getNickname());
        holder.textViewContent.setText(textPost.getContent());
        holder.textViewPostedAt.setText(textPost.goodLookingDateTimeFormat());
        holder.textViewLikes.setText(textPost.likesCount() + " likes");
        holder.textViewComments.setText(textPost.commentsCount() + " comments");

        Glide.with(context)
                .load(textPost.getProfilePicUrl())
                .placeholder(R.drawable.ic_profile)
                .error(R.drawable.ic_profile)
                .into(holder.imageViewProfilePic);

        if (textPost.getEmail().equals(currentEmail))
            holder.imageViewDelete.setVisibility(View.VISIBLE);
        else holder.imageViewDelete.setVisibility(View.GONE);

        if (textPost.getLikesList().contains(currentEmail))
            holder.imageViewLikes.setImageResource(R.drawable.ic_full_heart);
        else holder.imageViewLikes.setImageResource(R.drawable.ic_empty_heart);

    }

    private void setUpBasicEventListeners(MyViewHolder holder, TextPost textPost) {
        holder.imageViewLikes.setOnClickListener(view -> {
            String pathToLikes = textPost.endpointPath() + "/listLikes/";

            // SO I can have changes in the listener, I am not updating the current textPost likes list...
            // If I do no changes will be done in the listener, that is why I create a whole different new list.
            // To update the changes at the db endpoint but not locally.
            // Since the listener will update them locally at every device.

            // Maybe fetch the list of likes before updating??
            List<String> newLikedList = new ArrayList<>(textPost.getLikesList());

            if (!textPost.getLikesList().contains(currentEmail)) {
                newLikedList.add(currentEmail);
                holder.imageViewLikes.setImageResource(R.drawable.ic_full_heart);

            } else {
                newLikedList.remove(currentEmail);
                holder.imageViewLikes.setImageResource(R.drawable.ic_empty_heart);
            }

            DatabaseReference databaseReference = firebaseDatabase.getReference(pathToLikes);
            databaseReference.setValue(newLikedList);
        });

        holder.imageViewComments.setOnClickListener(view -> {
            View bottomDialogView = LayoutInflater.from(context).inflate(R.layout.comments_layout, null);

            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
            bottomSheetDialog.setContentView(bottomDialogView);
            bottomSheetDialog.setCancelable(true);

            ImageView imageViewBack = bottomDialogView.findViewById(R.id.imageViewBackBottomDialog);
            imageViewBack.setOnClickListener(v -> {
                bottomSheetDialog.dismiss();
            });

            ImageView imageViewProfile = bottomDialogView.findViewById(R.id.imageViewProfileBottomDialog);
            Glide.with(context)
                    .load(currentUser.getProfilePictureUrl())
                    .placeholder(R.drawable.ic_profile)
                    .error(R.drawable.ic_profile)
                    .into(imageViewProfile);

            EditText editText = bottomDialogView.findViewById(R.id.editTextAddCommentBottomDialog);

            @SuppressLint({"MissingInflatedId", "LocalSuppress"})
            Button buttonPost = bottomDialogView.findViewById(R.id.buttonPostBottomDialog);
            buttonPost.setOnClickListener(v -> {
                String text = editText.getText().toString().trim();
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();

                addAComment(text, textPost, bottomSheetDialog);
            });

            // Build the comments here
            // On a separate thread would be nice
            Log.d("Tag", "ALl comments: ");
            Log.d("Tag", textPost.getCommentsList().toString());
            buildAllCommentsForAPost(textPost, bottomDialogView);

            bottomSheetDialog.show();
        });

        holder.imageViewDelete.setOnClickListener(view -> {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
            builder.setTitle("DELETE?")
                    .setMessage("Are you sure you want to delete this post?")
                    .setIcon(R.drawable.ic_x)
                    .setPositiveButton("Yes", (dialog, which) -> {

                        ProgressDialog progressDialog = new ProgressDialog(context, R.style.CustomProgressDialogTheme);
                        progressDialog.setTitle("Deleting...");
                        progressDialog.setCancelable(false);
                        progressDialog.show();

                        viewModelTextPost.delete(textPost, new OnTextPostSuccessfullyDeleted() {
                            @Override
                            public void onDeleted(boolean success) {
                                if (success)
                                    Toast.makeText(context, "Successfully deleted textPost", Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(context, "Failed to delete textPost", Toast.LENGTH_SHORT).show();

                                progressDialog.dismiss();
                            }
                        });
                    })
                    .setNegativeButton("Cancel", ((dialog, which) -> dialog.dismiss()))
                    .setCancelable(true)
                    .show();
        });
    }

    private void addAComment(String text, TextPost textPost, BottomSheetDialog bottomSheetDialog) {
        String path = textPost.endpointPath() + "/commentList";
        DatabaseReference databaseReference = firebaseDatabase.getReference(path);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    long id = System.currentTimeMillis();
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.HH.mm.ss");
                    String postedAt = LocalDateTime.now().format(dateTimeFormatter);

                    Comment comment = Comment.createCommentForSaving(id, currentEmail, text, currentUser.getProfilePictureUrl(), postedAt);
                    List<Comment> commentList = new ArrayList<>(textPost.getCommentsList());

                    commentList.add(comment);
                    databaseReference.setValue(commentList)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    bottomSheetDialog.dismiss();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    bottomSheetDialog.dismiss();
                                }
                            });

                    if (snapshot.exists()) {
                        Log.d("Tag", "Comment list for textPost " + textPost.getId() + " exists");
                    } else {
                        Log.d("Tag", "Comment list for textPost " + textPost.getId() + " does NOT exist");
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Tag", error.getMessage());
            }
        });
    }

    private void buildAllCommentsForAPost(TextPost textPost, View bottomDialogView){
        new Thread(() -> {
            List<Comment> commentList = textPost.getCommentsList();
            for (Comment comment: commentList){

                LinearLayout.LayoutParams layoutParamsMain = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParamsMain.setMargins(32, 0, 32, 48);

                LinearLayout linearLayoutMain = new LinearLayout(context);
                linearLayoutMain.setLayoutParams(layoutParamsMain);
                linearLayoutMain.setOrientation(LinearLayout.HORIZONTAL);
                linearLayoutMain.setGravity(Gravity.CENTER_VERTICAL);



                LinearLayout.LayoutParams layoutParamsCardView = new LinearLayout.LayoutParams(80,80);
                layoutParamsCardView.setMargins(0,0,0,0);

                CardView cardView = new CardView(context);
                cardView.setLayoutParams(layoutParamsCardView);
                cardView.setRadius(50);


                LinearLayout.LayoutParams layoutParamsImageView = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                layoutParamsImageView.setMargins(0,0,0,0);

                ImageView imageView = new ImageView(context);
                imageView.setLayoutParams(layoutParamsImageView);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);


                LinearLayout.LayoutParams layoutParamsSecond = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParamsSecond.setMargins(32, 0, 32, 0);

                LinearLayout linearLayoutSecond = new LinearLayout(context);
                linearLayoutSecond.setLayoutParams(layoutParamsSecond);
                linearLayoutSecond.setOrientation(LinearLayout.VERTICAL);


                LinearLayout.LayoutParams layoutParamsTextViewEmail = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParamsTextViewEmail.setMargins(0,0,0,0);

                TextView textViewEmail = new TextView(context);
                textViewEmail.setLayoutParams(layoutParamsTextViewEmail);
                textViewEmail.setText(comment.getEmail());
                textViewEmail.setTextColor(ContextCompat.getColor(context, R.color.white));
                textViewEmail.setTextSize(14);
                textViewEmail.setTypeface(Typeface.DEFAULT_BOLD);

                LinearLayout.LayoutParams layoutParamsTextViewContent = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParamsTextViewEmail.setMargins(0,0,0,6);

                TextView textViewContent = new TextView(context);
                textViewContent.setLayoutParams(layoutParamsTextViewContent);
                textViewContent.setText(comment.getContent());
                textViewContent.setTextColor(ContextCompat.getColor(context, R.color.white));
                textViewContent.setTextSize(16);


                handler.post(() -> {
                    Glide.with(context).load(comment.getProfilePicUrl()).into(imageView);
                    cardView.addView(imageView);
                    linearLayoutSecond.addView(textViewEmail);
                    linearLayoutSecond.addView(textViewContent);

                    linearLayoutMain.addView(cardView);
                    linearLayoutMain.addView(linearLayoutSecond);

                    LinearLayout linearLayoutComments = bottomDialogView.findViewById(R.id.linearLayoutComments);
                    linearLayoutComments.addView(linearLayoutMain);
                });
            }
        }).start();
    }
}














