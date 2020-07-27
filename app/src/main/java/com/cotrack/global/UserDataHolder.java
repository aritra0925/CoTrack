package com.cotrack.global;

public class UserDataHolder {

    public static String USER_NAME;
    public static String USER_TYPE;
    public static String USER_ID;

    private static final UserDataHolder holder = new UserDataHolder();

    public static UserDataHolder getInstance() {
        return holder;
    }
}
