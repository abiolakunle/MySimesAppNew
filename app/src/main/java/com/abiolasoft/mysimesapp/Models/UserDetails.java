package com.abiolasoft.mysimesapp.Models;

import com.abiolasoft.mysimesapp.Interfaces.UserDetailsInterface;

import java.util.ArrayList;
import java.util.List;

public class UserDetails implements UserDetailsInterface {

    private String id;
    private String displayName;
    private String first_name;
    private String last_name;
    private String phone;
    private String status;
    private String image_url;
    private String image_thumb;
    private String email;
    private String level;
    private List<String> documents;


    public UserDetails() {
        if (documents == null) {
            documents = new ArrayList<String>();
        }
    }

    @Override
    public int getBookCount() {
        return documents.size();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getFirst_name() {
        return first_name;
    }

    @Override
    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    @Override
    public String getLast_name() {
        return last_name;
    }

    @Override
    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    @Override
    public String getPhone() {
        return phone;
    }

    @Override
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String getImage_url() {
        return image_url;
    }

    @Override
    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    @Override
    public String getImage_thumb() {
        return image_thumb;
    }

    @Override
    public void setImage_thumb(String image_thumb) {
        this.image_thumb = image_thumb;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getLevel() {
        return level;
    }

    @Override
    public void setLevel(String level) {
        this.level = level;
    }

    @Override
    public List<String> getDocuments() {
        return documents;
    }

    @Override
    public void setDocuments(List<String> documents) {
        this.documents = documents;
    }

    @Override
    public void addToDocuments(String document) {
        documents.add(document);
    }

}
