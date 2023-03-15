package com.example.dev;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class UserList implements Parcelable {

    private List<String> userList;
    //private List<UserData> userData;

    public UserList()
    {
        userList = new ArrayList<>();
        userList.add("Test");
    }

    protected UserList(Parcel in) {
        in.readStringList(userList);
//        userData = new ArrayList<UserData>();
//        in.readList(userData,UserData.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(userList);
//        dest.writeList(userData);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserList> CREATOR = new Creator<UserList>() {
        @Override
        public UserList createFromParcel(Parcel in) {
            return new UserList(in);
        }

        @Override
        public UserList[] newArray(int size) {
            return new UserList[size];
        }
    };

    public List<String> getUserList(){return userList;}
    public void setUserlist(List<String> list){this.userList = list;}
}
