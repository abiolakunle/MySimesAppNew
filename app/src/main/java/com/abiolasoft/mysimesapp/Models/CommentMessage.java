package com.abiolasoft.mysimesapp.Models;

import java.util.ArrayList;
import java.util.List;

public class CommentMessage extends MessageModel {
    private List<String> like_list;

    public CommentMessage() {
        like_list = new ArrayList<>();
    }

    public List<String> getLike_list() {
        return like_list;
    }

    public void setLike_list(List<String> like_list) {
        this.like_list = like_list;
    }

    public void like_unlike(String user_id) {
        boolean contains = like_list.contains(user_id);
        if (contains) {
            int position = like_list.indexOf(user_id);
            like_list.remove(position);
        } else {
            like_list.add(user_id);
        }
    }

}
