package com.cotrack.global;

public class UserDataHolder {

    private static final UserDataHolder holder = new UserDataHolder();

    public static UserDataHolder getInstance() {
        return holder;
    }
}
