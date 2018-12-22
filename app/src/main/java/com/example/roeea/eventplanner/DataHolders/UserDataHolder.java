package com.example.roeea.eventplanner.DataHolders;

import com.example.roeea.eventplanner.ObjectClasses.User;

/**
 * This class track and reserve the information about the user
 * that is currently logged in our application in his device.
 */
public class UserDataHolder {
    private User user = null;

    private static final UserDataHolder userDataHolderInstance = new UserDataHolder();

    public static UserDataHolder getUserDataHolderInstance() {
        return userDataHolderInstance;
    }

    private UserDataHolder() {
    }

    /**
     * @return An authenticated user
     */
    public User getAuthenticatedUser() {
        return user;
    }

    /**
     * Sets the current logged user.
     * @param user
     */
    public void setAuthenticatedUser(User user) {
        this.user = user;
    }

}