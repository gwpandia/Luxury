package com.example.pandia.luxury.interfaces;

import java.util.Collection;

public interface RangeReadable<T>  {
    public void initializeReader();
    public long entrySize();
    public boolean isInBound(long i);
    public Collection<T> readRangeEntries(long start, long end);
    public void finishReader();
}
