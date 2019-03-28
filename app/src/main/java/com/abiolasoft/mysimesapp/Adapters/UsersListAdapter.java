package com.abiolasoft.mysimesapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.abiolasoft.mysimesapp.Models.UserDetails;
import com.abiolasoft.mysimesapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class UsersListAdapter extends RecyclerView.Adapter<UsersListAdapter.ViewHolder> {
    private Context context;
    private List<UserDetails> userList;

    public UsersListAdapter(List<UserDetails> usersList) {
        this.userList = usersList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.user_list_single_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Picasso.get().load(userList.get(position).getImage_thumb())
                .placeholder(R.drawable.com_facebook_profile_picture_blank_square)
                .into(holder.userListCiv);
        holder.userListTv.setText(userList.get(position).getDisplayName());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView userListCiv;
        private TextView userListTv;

        public ViewHolder(View itemView) {
            super(itemView);

            userListCiv = itemView.findViewById(R.id.user_list_civ);
            userListTv = itemView.findViewById(R.id.user_list_name_tv);
        }
    }
}
