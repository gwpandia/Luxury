package com.example.pandia.luxury.data;

import com.example.pandia.luxury.util.ItemUtil;
import com.example.pandia.luxury.util.Util;

import java.util.Date;

public class BorrowItem {
    private long mDataBaseID;
    private String mUniqueID;
    //TODO: A user from contact ?
    private String mBorrower;
    private String mBorrowedItemID;
    private Date mBorrowDate;
    private Date mReturnDate;

    public BorrowItem(String borrower, String itemID) {
        setBorrower(borrower);
        setBorrowedItemID(itemID);
        mDataBaseID = -1;
        mUniqueID = ItemUtil.GenerateBorrowUniqueID(borrower, itemID);
    }

    public long getDataBaseID() {
        return mDataBaseID;
    }

    public void setDataBaseID(long databaseID) {
        this.mDataBaseID = databaseID;
    }

    public void setUniqueID (String uniqueID) {
        if (Util.isValidString(uniqueID)) {
            this.mUniqueID = uniqueID;
        }
    }

    public String getUniqueID() {
        return mUniqueID;
    }

    public String getBorrower() {
        return mBorrower;
    }

    public void setBorrower(String borrower) {
        if (Util.isValidString(borrower)) {
            this.mBorrower = borrower;
            mUniqueID = ItemUtil.GenerateBorrowUniqueID(mBorrower, mBorrowedItemID);
        }
    }

    public String getBorrowedItemID() {
        return mBorrowedItemID;
    }

    public void setBorrowedItemID(String borrowedItemID) {
        if (Util.isValidString(borrowedItemID)) {
            this.mBorrowedItemID = borrowedItemID;
            mUniqueID = ItemUtil.GenerateBorrowUniqueID(mBorrower, mBorrowedItemID);
        }
    }

    public Date getBorrowDate() {
        return mBorrowDate;
    }

    public void setBorrowDate(Date borrowDate) {
        this.mBorrowDate = borrowDate;
    }

    public Date getReturnDate() {
        return mReturnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.mReturnDate = returnDate;
    }
}
