package com.example.pandia.luxury.interfaces;

public interface Readable<T> {
    public void initializeReader();
    public int entrySize();
    public boolean isInBound(int i);
    public T readEntry(int i);
    public void finishReader();
}
