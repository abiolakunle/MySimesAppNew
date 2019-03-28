package com.abiolasoft.mysimesapp.Models;

import java.util.ArrayList;
import java.util.List;

public class ImageMessage extends MessageModel {

    private String message_image;
    private String image_url;
    private String image_thumb;
    private List<String> like_list;
    private List<CommentMessage> messageComments;
    private String sender_image_url;


    public ImageMessage() {
        messageComments = new ArrayList<>();
        like_list = new ArrayList<>();
    }

    public String getSender_image_url() {
        return sender_image_url;
    }

    public void setSender_image_url(String sender_image_url) {
        this.sender_image_url = sender_image_url;
    }

    public void addComment(CommentMessage comment) {
        messageComments.add(comment);
    }

    public void removeComment(int comment_position) {
        messageComments.remove(comment_position);
    }

    public String getMessage_image() {
        return message_image;
    }

    public void setMessage_image(String message_image) {
        this.message_image = message_image;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getImage_thumb() {
        return image_thumb;
    }

    public void setImage_thumb(String image_thumb) {
        this.image_thumb = image_thumb;
    }

    public List<String> getLike_list() {
        return like_list;
    }

    public void setLike_list(List<String> like_list) {
        this.like_list = like_list;
    }

    public List<CommentMessage> getMessageComments() {
        return messageComments;
    }

    public void setMessageComments(List<CommentMessage> messageComments) {
        this.messageComments = messageComments;
    }

    public boolean like_unlike(String user_id) {
        boolean contains = like_list.contains(user_id);
        if (contains) {
            int position = like_list.indexOf(user_id);
            like_list.remove(position);
            return false;
        } else {
            like_list.add(user_id);
            return true;
        }
    }

}
