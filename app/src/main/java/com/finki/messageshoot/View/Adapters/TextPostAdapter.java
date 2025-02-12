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
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

import kotlin.jvm.internal.Lambda;

public class TextPostAdapter extends RecyclerView.Adapter<TextPostAdapter.MyViewHolder> {

    private Context context;
    private List<TextPost> textPostList;
    private ViewModelTextPost viewModelTextPost;
    public TextPostAdapter(Context context, List<TextPost> textPostList, ViewModelTextPost viewModelTextPost){
        this.context = context;
        this.textPostList = textPostList;
        this.viewModelTextPost = viewModelTextPost;
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

        holder.textViewEmail.setText(textPost.getEmail());
        holder.textViewNickname.setText(textPost.getNickname());
        holder.textViewContent.setText(textPost.getContent());
        Picasso.get().load(textPost.getProfilePicUrl()).into(holder.imageViewProfilePic);

        holder.linearLayout.setOnClickListener(view -> {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
            builder.setTitle("DELETE?")
                    .setMessage("Are you sure you want to delete this post?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            viewModelTextPost.delete(textPost);
                        }
                    })
                    .setCancelable(true)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return textPostList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        protected LinearLayout linearLayout;
        protected ImageView imageViewProfilePic;
        protected TextView textViewEmail;
        protected TextView textViewNickname;
        protected TextView textViewContent;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            this.linearLayout = itemView.findViewById(R.id.linearLayoutTextPost);
            this.imageViewProfilePic = itemView.findViewById(R.id.imageViewTextPostProfilePic);
            this.textViewEmail = itemView.findViewById(R.id.textViewEmailTextPost);
            this.textViewNickname = itemView.findViewById(R.id.textViewNicknameTextPost);
            this.textViewContent = itemView.findViewById(R.id.textViewContentTextPost);
        }
    }
}
