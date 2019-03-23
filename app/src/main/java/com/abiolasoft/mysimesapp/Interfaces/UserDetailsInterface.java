package com.abiolasoft.mysimesapp.Interfaces;

import java.util.List;

public interface UserDetailsInterface {

    int getBookCount();

    String getId();

    void setId(String id);

    String getDisplayName();

    void setDisplayName(String displayName);

    String getFirst_name();

    void setFirst_name(String first_name);

    String getLast_name();

    void setLast_name(String last_name);

    String getPhone();

    void setPhone(String phone);

    String getStatus();

    void setStatus(String status);

    String getImage_url();

    void setImage_url(String image_url);

    String getImage_thumb();

    void setImage_thumb(String image_thumb);

    String getEmail();

    void setEmail(String email);

    String getLevel();

    void setLevel(String level);

    List<String> getDocuments();

    void setDocuments(List<String> documents);

    void addToDocuments(String document);
}
