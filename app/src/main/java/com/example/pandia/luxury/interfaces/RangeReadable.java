package com.example.pandia.luxury.interfaces;

import java.util.Collection;

public interface RangeReadable<T>  {
    public Collection<T> readRangeEntries(long start, long end);
}
