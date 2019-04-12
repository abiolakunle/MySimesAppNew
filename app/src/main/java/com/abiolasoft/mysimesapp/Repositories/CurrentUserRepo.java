package com.abiolasoft.mysimesapp.Repositories;

import com.abiolasoft.mysimesapp.Models.UserDetails;
import com.abiolasoft.mysimesapp.Utils.UserSharedPref;

public class CurrentUserRepo {
    private static final String USER_PREF_KEY = "currentUser";
    private static UserRepository userRepository = new UserRepository();
    private static UserSharedPref userSharedPref = new UserSharedPref();

    public static void updateCurrentUser(UserDetails userDetails) {
        userSharedPref.setObj(USER_PREF_KEY, userDetails);
        userRepository.addUser(userDetails);
    }

    public static UserDetails getOnline() {
        String Uid = ((UserDetails) userSharedPref.getObj(USER_PREF_KEY)).getId();
        UserDetails user = userRepository.getUserById(Uid);
        return user;
    }

    public static UserDetails getOffline() {
        UserDetails user = (UserDetails) userSharedPref.getObj(USER_PREF_KEY);
        return user;
    }
}
