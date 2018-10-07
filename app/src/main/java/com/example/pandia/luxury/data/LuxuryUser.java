package com.example.pandia.luxury.data;

import com.example.pandia.luxury.util.Util;

public class LuxuryUser {
    public String mUserID;
    public String mUserCredential;

    public LuxuryUser(String userID, String userCredential) {
        setmUserID(userID);
        setmUserCredential(userCredential);
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
        if (Util.isValidString(userCredential) && Util.isValidString(mUserID)) {
            String salt = new StringBuilder(mUserID).reverse().toString();
            this.mUserCredential = Util.hashCredential(userCredential, salt);
        }
    }
}
