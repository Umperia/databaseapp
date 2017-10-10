package com.company.ump.databaseapp;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.company.ump.databaseapp.image.ImageUtil;
import com.company.ump.databaseapp.profile.ItemListener;
import com.company.ump.databaseapp.profile.ProfileItem;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ResViewAdapter extends RecyclerView.Adapter<ResViewAdapter.ProfileViewHolder> {

    List<ProfileItem> profiles;
    ItemListener listener;
    ImageUtil imageUtil;

    public ResViewAdapter(List<ProfileItem> profiles, ImageUtil imageUtil, ItemListener listener) {
        this.profiles = profiles;
        this.listener = listener;
        this.imageUtil = imageUtil;
    }

    public List<ProfileItem> getProfiles() {
        return profiles;
    }

    void addProfile(ProfileItem profile) {
        getProfiles().add(profile);
        notifyDataSetChanged();
    }

    @Override
    public ProfileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        return new ProfileViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ProfileViewHolder holder, int position) {
        holder.tvName.setText(profiles.get(position).getName());
        holder.tvEmail.setText(profiles.get(position).getEmail());
        holder.tvPhone.setText(profiles.get(position).getPhone());
        imageUtil.getImageLoader().displayImage(profiles.get(position).getPath(),
                holder.avatarCard,
                imageUtil.getOptionsPhoto());
        holder.cv.setOnClickListener(v -> listener.getProfile(profiles.get(position)));
    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }

    public static class ProfileViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView tvName;
        TextView tvEmail;
        TextView tvPhone;
        CircleImageView avatarCard;

        ProfileViewHolder(View itemView) {
            super(itemView);
            cv =  itemView.findViewById(R.id.cv);
            tvName = itemView.findViewById(R.id.tv_name);
            tvEmail = itemView.findViewById(R.id.tv_email);
            tvPhone = itemView.findViewById(R.id.tv_phone);
            avatarCard = itemView.findViewById(R.id.avatar_card);
        }
    }
}
