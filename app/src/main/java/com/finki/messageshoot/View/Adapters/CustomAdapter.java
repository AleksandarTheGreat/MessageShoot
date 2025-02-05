package com.finki.messageshoot.View.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.finki.messageshoot.Model.Helper.ThemeUtils;
import com.finki.messageshoot.Model.User;
import com.finki.messageshoot.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

import kotlin.jvm.internal.Lambda;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;
    private List<User> users;
    private boolean isNightModeOn;

    public CustomAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
        this.isNightModeOn = ThemeUtils.isNightModeOn(context);
    }

    @NonNull
    @Override
    public CustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.single_user_layout, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.MyViewHolder holder, int position) {
        User user = users.get(position);

        holder.textViewEmail.setText(user.getEmail());
        holder.textViewNickname.setText(user.getNickname());
        holder.textViewBio.setText(user.getBio());
        if (!user.getProfilePictureUrl().isEmpty())
            Picasso.get()
                    .load(user.getProfilePictureUrl())
                    .into(holder.imageViewProfilePicture);
        else
            holder.imageViewProfilePicture.setImageResource(R.drawable.ic_profile);

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        protected TextView textViewEmail;
        protected TextView textViewNickname;
        protected TextView textViewBio;
        protected ImageView imageViewProfilePicture;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textViewEmail = itemView.findViewById(R.id.textViewEmail);
            this.textViewNickname = itemView.findViewById(R.id.textViewNickname);
            this.textViewBio = itemView.findViewById(R.id.textViewBio);
            this.imageViewProfilePicture = itemView.findViewById(R.id.imageViewProfilePicture);
        }
    }

    private void additionalThemeChanges(MyViewHolder myViewHolder) {

    }
}








