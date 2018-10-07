package com.example.pandia.luxury.interfaces;

public interface Writable<T> {
    public void initializeWriter();
    public void writeEntry(T entry);
    public long writtenEntries();
    public void finishWriter();
}