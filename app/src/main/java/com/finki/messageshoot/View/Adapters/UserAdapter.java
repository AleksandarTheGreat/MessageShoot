package com.finki.messageshoot.View.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.finki.messageshoot.Repository.Utils.ThemeUtils;
import com.finki.messageshoot.Model.User;
import com.finki.messageshoot.R;
import com.finki.messageshoot.databinding.ActivityMainBinding;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

    private Context context;
    private ActivityMainBinding activityMainBinding;
    private AppCompatActivity appCompatActivity;
    private List<User> users;
    private boolean isNightModeOn;

    public UserAdapter(Context context, ActivityMainBinding activityMainBinding, AppCompatActivity appCompatActivity, List<User> users) {
        this.context = context;
        this.activityMainBinding = activityMainBinding;
        this.appCompatActivity = appCompatActivity;
        this.users = users;
        this.isNightModeOn = ThemeUtils.isNightModeOn(context);
    }

    @NonNull
    @Override
    public UserAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.single_user_layout, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);

        myViewHolder.mainLinearLayout.setOnClickListener(v -> {
//            FragmentChat fragmentChat = FragmentChat.newInstance();
//            CustomFragmentManager.changeFragment(appCompatActivity, activityMainBinding, fragmentChat, true);
            Toast.makeText(context, "Not now pal, not now", Toast.LENGTH_SHORT).show();
        });

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.MyViewHolder holder, int position) {
        User user = users.get(position);

        holder.textViewEmail.setText(user.getEmail());
        holder.textViewNickname.setText(user.getNickname());
        holder.textViewBio.setText(user.getBio());

        Glide.with(context)
                .load(user.getProfilePictureUrl())
                .placeholder(R.drawable.ic_profile)
                .into(holder.imageViewProfilePicture);

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        protected LinearLayout mainLinearLayout;
        protected TextView textViewEmail;
        protected TextView textViewNickname;
        protected TextView textViewBio;
        protected ImageView imageViewProfilePicture;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mainLinearLayout = itemView.findViewById(R.id.linearLayoutSingleUser);
            this.textViewEmail = itemView.findViewById(R.id.textViewEmail);
            this.textViewNickname = itemView.findViewById(R.id.textViewNickname);
            this.textViewBio = itemView.findViewById(R.id.textViewBio);
            this.imageViewProfilePicture = itemView.findViewById(R.id.imageViewProfilePicture);
        }
    }

    private void additionalThemeChanges(MyViewHolder myViewHolder) {

    }
}








