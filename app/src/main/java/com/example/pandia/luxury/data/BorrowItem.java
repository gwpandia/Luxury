package com.example.pandia.luxury.data;

import com.example.pandia.luxury.util.ItemUtil;
import com.example.pandia.luxury.util.Util;

import java.util.Date;

import androidx.annotation.NonNull;

public class BorrowItem  extends BaseItem{
    //TODO: A user from contact ?
    private String mBorrower;
    private String mBorrowedItemID;
    private Date mBorrowDate;
    private Date mReturnDate;

    public BorrowItem(String borrower, String itemID) {
        super();
        setBorrower(borrower);
        setBorrowedItemID(itemID);
        mDataBaseID = -1;
        updateUniqueID();
    }

    public String getBorrower() {
        return mBorrower;
    }

    public void setBorrower(String borrower) {
        if (Util.isValidString(borrower)) {
            this.mBorrower = borrower;
            updateUniqueID();
        }
    }

    public String getBorrowedItemID() {
        return mBorrowedItemID;
    }

    public void setBorrowedItemID(String borrowedItemID) {
        if (Util.isValidString(borrowedItemID)) {
            this.mBorrowedItemID = borrowedItemID;
            updateUniqueID();
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

    @NonNull
    @Override
    public String toString() {
        String ret = "[BorrowItem] dbID: " + mDataBaseID + ", UniqueID: " + mUniqueID +
                ", Borrower: " + mBorrower + ", BorrowItemID: " + mBorrowedItemID +
                ", BorrowDate: " + Util.convertDateToString(mBorrowDate) +
                ", ReturnDate: " + Util.convertDateToString(mReturnDate);

        return ret;
    }

    @Override
    protected void updateUniqueID() {
        mUniqueID = ItemUtil.generateBorrowUniqueID(mBorrower, mBorrowedItemID,
                Util.convertLocalDateTimeToString(mCreateDate));
    }
}
