package com.abiolasoft.mysimesapp.Models;

import java.util.List;

public class Ebook {



    private String book_id;
    private String file_name;
    private String file_size;
    private String time_uploaded;
    private String uploaded_by;
    private String file_type;
    private String book_url;
    private String user_id;



    private List<String> book_tags;


    public Ebook() {
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

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUrl() {
        return book_url;
    }

    public void setUrl(String url) {
        this.book_url = url;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getFile_size() {
        return file_size;
    }

    public void setFile_size(String file_size) {
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


}



