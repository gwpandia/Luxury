package com.example.pandia.luxury.io.interfaces;

public interface Readable<T> {
    public void initializeReader();
    public long entrySize();
    public boolean isInBound(long i);
    public T readEntry(long i);
    public void finishReader();
}
