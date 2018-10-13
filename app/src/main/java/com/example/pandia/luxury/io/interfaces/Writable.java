package com.example.pandia.luxury.io.interfaces;

public interface Writable<T> {
    public void initializeWriter();
    public void writeEntry(T entry);
    public long writtenEntries();
    public void finishWriter();
}