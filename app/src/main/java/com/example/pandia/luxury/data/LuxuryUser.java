package com.example.pandia.luxury.data;

import com.example.pandia.luxury.util.Util;

public class LuxuryUser {
    public String mUserID;
    public String mUserCredential;

    public LuxuryUser(String userID, String userCredential) {
        mUserID = userID;
        mUserCredential = userCredential;
    }

    public String getmUserID() {
        return mUserID;
    }

    public void setmUserID(String userID) {
        if (Util.isValidString(userID)) {
            this.mUserID = userID;
        }
    }

    public String getmUserCredential() {
        return mUserCredential;
    }

    public void setmUserCredential(String userCredential) {
        if (Util.isValidString(userCredential)) {
            this.mUserCredential = userCredential;
        }
    }
}
