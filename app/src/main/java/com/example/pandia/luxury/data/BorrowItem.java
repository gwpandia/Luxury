package com.example.pandia.luxury.data;

import com.example.pandia.luxury.util.ItemUtil;
import com.example.pandia.luxury.util.Util;

import java.util.Date;

public class BorrowItem {

    public String mUniqueID;
    // A user from contact ?
    public String mBorrower;
    public String mBorrowedItemID;
    public Date mBorrowDate;
    public Date mReturnDate;

    public BorrowItem(String borrower, String itemID) {
        mBorrower = borrower;
        mBorrowedItemID = itemID;
        mUniqueID = ItemUtil.GenerateBorrowUniqueID(borrower, itemID);
    }

    public String getUniqueID() {
        return mUniqueID;
    }

    public String getBorrower() {
        return mBorrower;
    }

    public String getBorrowedItemID() {
        return mBorrowedItemID;
    }

    public void setBorrowedItemID(String borrowedItemID) {
        if (Util.isValidString(borrowedItemID)) {
            this.mBorrowedItemID = borrowedItemID;
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
