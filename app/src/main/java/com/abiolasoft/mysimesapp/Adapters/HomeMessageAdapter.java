package com.abiolasoft.mysimesapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.abiolasoft.mysimesapp.Activities.HomeMessageCommentsActivity;
import com.abiolasoft.mysimesapp.Models.ImageMessage;
import com.abiolasoft.mysimesapp.R;
import com.abiolasoft.mysimesapp.Repositories.CurrentUserRepo;
import com.abiolasoft.mysimesapp.Utils.DbPaths;
import com.abiolasoft.mysimesapp.Utils.TimeAgo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeMessageAdapter extends RecyclerView.Adapter<HomeMessageAdapter.ViewHolder> {

    private Context context;
    private List<ImageMessage> messagesList;
    private List<String> messageIdList;


    public HomeMessageAdapter(List<ImageMessage> messagesList, List<String> messageIdList) {
        this.messagesList = messagesList;
        this.messageIdList = messageIdList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.home_single_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        Picasso.get().load(messagesList.get(position).getSender_image_url()).into(holder.userImage);
        holder.messageUser.setText(messagesList.get(position).getSender_name());
        holder.messageText.setText(messagesList.get(position).getMessage_text());
        int commentCount = messagesList.get(position).getMessageComments().size();
        holder.messageComments.setText(commentCount + ((commentCount > 1) ? " Comments" : " Comment"));
        if ((messagesList.get(position).getLike_list().contains(CurrentUserRepo.getOffline().getId()))) {
            holder.likeImage.setImageResource(R.mipmap.action_like_accent);
        }
        int likeCount = messagesList.get(position).getLike_list().size();
        holder.messageLikes.setText(likeCount + ((likeCount > 1) ? " Likes" : " Like"));
        holder.messageTime.setText(TimeAgo.getTimeAgo(Long.parseLong(messagesList.get(position).getMessage_time()), context));

        String messageImageUrl = messagesList.get(position).getImage_url();
        if (messageImageUrl != null) {
            Picasso.get().load(messageImageUrl).placeholder(R.drawable.post_placeholder).into(holder.messageImage);
            holder.messageImage.setVisibility(View.VISIBLE);
        }


        final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        final ImageMessage thisMessage = messagesList.get(position);

        holder.likeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean likeState = thisMessage.like_unlike(CurrentUserRepo.getOffline().getId());
                if (likeState) {
                    holder.likeImage.setImageResource(R.mipmap.action_like_accent);
                } else {
                    holder.likeImage.setImageResource(R.mipmap.action_like_gray);
                }

                firebaseFirestore.collection(DbPaths.HomeMessages.toString()).document(messageIdList.get(position)).set(thisMessage).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();

                    }
                });

            }

        });

        holder.messageComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent commentIntent = new Intent(context, HomeMessageCommentsActivity.class);
                context.startActivity(commentIntent);

            }
        });
    }

    private void notifyChange() {
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView messageUser, messageTime, messageLikes, messageComments, messageText;
        private ImageView messageImage, likeImage, commentImage;
        private CircleImageView userImage;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            messageUser = itemView.findViewById(R.id.home_msg_sender_tv);
            messageTime = itemView.findViewById(R.id.home_msg_time_tv);
            messageLikes = itemView.findViewById(R.id.home_msg_likes_tv);
            messageComments = itemView.findViewById(R.id.home_msg_comments_tv);
            messageText = itemView.findViewById(R.id.home_msg_tv);

            messageImage = itemView.findViewById(R.id.home_msg_image);
            likeImage = itemView.findViewById(R.id.home_msg_like_iv);
            commentImage = itemView.findViewById(R.id.home_msg_comment_iv);
            userImage = itemView.findViewById(R.id.home_msg_user_image);
        }
    }
}
