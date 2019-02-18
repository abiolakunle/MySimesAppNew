package com.abiolasoft.mysimesapp.Interfaces;

import java.util.List;

public interface UserRepositoryInterface<User> {

    List<User> loadAllUsers();
    void addUser(User user);
    void updateUser(User user);
    User GetUserById(String user_id);

}
