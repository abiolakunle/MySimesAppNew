package com.abiolasoft.mysimesapp.Models;

import com.abiolasoft.mysimesapp.Interfaces.SuperUserDetailsInterface;

public abstract class SuperUserDetails extends UserDetails implements SuperUserDetailsInterface {
    private String position;
    private String access_code;

    @Override
    public String getPosition() {
        return position;
    }

    @Override
    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public String getAccess_code() {
        return access_code;
    }

    @Override
    public void setAccess_code(String access_code) {
        this.access_code = access_code;
    }

}
