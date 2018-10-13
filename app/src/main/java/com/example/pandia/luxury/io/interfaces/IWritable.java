package com.example.pandia.luxury.io.interfaces;

public interface IWritable<T> {
    public void initializeWriter();
    public void writeEntry(T entry);
    public void removeEntry(String uniqueID);
    public void removeEntry(long id);
    public long writtenEntries();
    public void finishWriter();
}