package com.abiolasoft.mysimesapp.Models;

import java.util.ArrayList;
import java.util.List;

public class EBook {


    private String uploader_id;
    private String book_title;
    private String description;
    private long file_size;
    private String time_uploaded;
    private String uploaded_by;
    private String file_type;
    private String book_url;
    private String book_id;
    private List<String> book_tags;
    private List<String> book_custom_tags;

    public EBook() {
        if (book_tags == null) {
            book_tags = new ArrayList<>();
        }
        if (book_custom_tags == null) {
            book_custom_tags = new ArrayList<>();
        }
    }

    public void addCustomTag(String tag) {
        book_custom_tags.add(tag);
    }

    public List<String> getBook_custom_tags() {
        return book_custom_tags;
    }

    public void setBook_custom_tags_tags(List<String> book_custom_tags) {
        this.book_custom_tags = book_custom_tags;
    }


    public void addTag(String tag) {
        book_tags.add(tag);
    }

    public String getBook_url() {
        return book_url;
    }

    public void setBook_url(String book_url) {
        this.book_url = book_url;
    }

    public List<String> getBook_tags() {
        return book_tags;
    }

    public void setBook_tags(List<String> book_tags) {
        this.book_tags = book_tags;
    }

    public String getUploader_id() {
        return uploader_id;
    }

    public void setUploader_id(String uploader_id) {
        this.uploader_id = uploader_id;
    }

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public String getUrl() {
        return book_url;
    }

    public void setUrl(String url) {
        this.book_url = url;
    }

    public String getBook_title() {
        return book_title;
    }

    public void setBook_title(String book_title) {
        this.book_title = book_title;
    }

    public long getFile_size() {
        return file_size;
    }

    public void setFile_size(long file_size) {
        this.file_size = file_size;
    }

    public String getTime_uploaded() {
        return time_uploaded;
    }

    public void setTime_uploaded(String time_uploaded) {
        this.time_uploaded = time_uploaded;
    }

    public String getUploaded_by() {
        return uploaded_by;
    }

    public void setUploaded_by(String uploaded_by) {
        this.uploaded_by = uploaded_by;
    }

    public String getFile_type() {
        return file_type;
    }

    public void setFile_type(String file_type) {
        this.file_type = file_type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}



