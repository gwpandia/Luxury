package com.example.pandia.luxury.io.interfaces;

public interface IReadable<T> {
    public void initializeReader();
    public long entrySize();
    public boolean isInBound(long i);
    public T readEntry(long i);
    public T readEntry(String key);
    public void finishReader();
}
