package com.contec.phms.fragment;

public class UserData {
    private int list_position;
    private String user_left_text;
    private String user_right_text;

    public UserData() {
    }

    public UserData(String user_left_text2) {
        this.user_left_text = user_left_text2;
    }

    public UserData(String user_left_text2, String user_right_text2) {
        this.user_left_text = user_left_text2;
        this.user_right_text = user_right_text2;
    }

    public UserData(String user_left_text2, int list_position2) {
        this.user_left_text = user_left_text2;
        this.list_position = list_position2;
    }

    public UserData(String user_left_text2, String user_right_text2, int list_position2) {
        this.user_left_text = user_left_text2;
        this.user_right_text = user_right_text2;
        this.list_position = list_position2;
    }

    public String getUser_left_text() {
        return this.user_left_text;
    }

    public void setUser_left_text(String user_left_text2) {
        this.user_left_text = user_left_text2;
    }

    public String getUser_right_text() {
        return this.user_right_text;
    }

    public void setUser_right_text(String user_right_text2) {
        this.user_right_text = user_right_text2;
    }

    public int getList_position() {
        return this.list_position;
    }

    public void setList_position(int list_position2) {
        this.list_position = list_position2;
    }
}
