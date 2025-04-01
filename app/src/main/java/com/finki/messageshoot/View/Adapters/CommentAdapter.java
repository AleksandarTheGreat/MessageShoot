package com.finki.messageshoot.View.Adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.finki.messageshoot.Model.Comment;
import com.finki.messageshoot.R;

import java.time.LocalDateTime;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {

    private Context context;
    private List<Comment> commentList;

    public CommentAdapter(Context context, List<Comment> commentList){
        this.context = context;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public CommentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.single_comment_layout, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.MyViewHolder holder, int position) {
        Comment comment = commentList.get(position);

        holder.textViewEmail.setText(comment.getEmail());
        holder.textViewContent.setText(comment.getContent());
        holder.textViewPostedAt.setText(comment.getPostedAtDateTime().toString());
        Glide.with(context)
                .load(comment.getProfilePicUrl())
                .into(holder.imageViewProfilePicture);
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        protected ImageView imageViewProfilePicture;
        protected TextView textViewEmail;
        protected TextView textViewContent;
        protected TextView textViewPostedAt;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            this.imageViewProfilePicture = itemView.findViewById(R.id.imageViewProfileCommentLayout);
            this.textViewEmail = itemView.findViewById(R.id.textViewEmailCommentLayout);
            this.textViewContent = itemView.findViewById(R.id.textViewContentCommentLayout);
            this.textViewPostedAt = itemView.findViewById(R.id.textViewPostedAtCommentLayout);
        }
    }

    private String calculateDaysAgo(String postedAt){
        String [] results = postedAt.split("\\.");

        String day = results[0];
        String month = results[1];
        String year = results[2];

        return day + "." + month + "." + year + " at " + results[3] + ":" + results[4] + "h";
    }
}





