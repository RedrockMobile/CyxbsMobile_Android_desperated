package com.mredrock.cyxbsmobile.data.model;

/**
 * Info of user
 * <p>
 * Created by David on 15/5/15.
 */
public class User {
    public User() {
    }

    public String stuNum;
    public String idNum;
    public String name;
    public String gender;
    public String classNum;
    public String major;
    public String college;
    public String grade;

    public static class UserWrapper extends Wrapper {
        public UserWrapper() {
        }

        public int status;
        public User data;
    }
}
