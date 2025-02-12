package com.finki.messageshoot.View.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.finki.messageshoot.Model.TextPost;
import com.finki.messageshoot.R;
import com.finki.messageshoot.ViewModel.ViewModelTextPost;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.dialog.MaterialDialogs;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

import kotlin.jvm.internal.Lambda;

public class TextPostAdapter extends RecyclerView.Adapter<TextPostAdapter.MyViewHolder> {

    private Context context;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private List<TextPost> textPostList;
    private ViewModelTextPost viewModelTextPost;
    private String currentEmail;
    public TextPostAdapter(Context context, List<TextPost> textPostList, ViewModelTextPost viewModelTextPost){
        this.context = context;
        this.textPostList = textPostList;
        this.viewModelTextPost = viewModelTextPost;
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.firebaseDatabase = FirebaseDatabase.getInstance("https://social101-12725-default-rtdb.europe-west1.firebasedatabase.app/");
        this.currentEmail = firebaseAuth.getCurrentUser().getEmail();
    }

    @NonNull
    @Override
    public TextPostAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.single_text_post, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TextPostAdapter.MyViewHolder holder, int position) {
        TextPost textPost = textPostList.get(position);
        String textPostEmail = textPost.getEmail();

        holder.textViewEmail.setText(textPost.getEmail());
        holder.textViewNickname.setText(textPost.getNickname());
        holder.textViewContent.setText(textPost.getContent());
        holder.textViewPostedAt.setText(textPost.goodLookingDateTimeFormat());
        Picasso.get().load(textPost.getProfilePicUrl()).into(holder.imageViewProfilePic);

        if (textPostEmail.equals(currentEmail)) holder.imageViewDelete.setVisibility(View.VISIBLE);
        else holder.imageViewDelete.setVisibility(View.GONE);

        if (textPost.getLikesList().contains(currentEmail)) holder.imageViewLikes.setImageResource(R.drawable.ic_full_heart);
        else holder.imageViewLikes.setImageResource(R.drawable.ic_empty_heart);

        holder.textViewLikes.setText(textPost.getLikesList().size() + " likes");
        holder.imageViewLikes.setOnClickListener(view -> {
            String replacedEmail = textPostEmail.replace(".", ":::");
            String path = replacedEmail + "/" + textPost.getId() + "/listLikes/";

            if (!textPost.getLikesList().contains(currentEmail)){
                textPost.getLikesList().add(currentEmail);
                holder.imageViewLikes.setImageResource(R.drawable.ic_full_heart);

            } else {
                textPost.getLikesList().remove(currentEmail);
                holder.imageViewLikes.setImageResource(R.drawable.ic_empty_heart);
            }

            DatabaseReference databaseReference = firebaseDatabase.getReference(path);
            databaseReference.setValue(textPost.getLikesList());
        });

        holder.imageViewDelete.setOnClickListener(view -> {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
            builder.setTitle("DELETE?")
                    .setMessage("Are you sure you want to delete this post?")
                    .setPositiveButton("Yes", (dialog, which) -> viewModelTextPost.delete(textPost))
                    .setNegativeButton("Cancel", ((dialog, which) -> dialog.dismiss()))
                    .setCancelable(true)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return textPostList.size();
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
        }
    }
}
