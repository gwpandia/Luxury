package com.example.pandia.luxury.models;

import java.util.HashMap;

public class TaskParameters<T> {
    private T mTaskType;
    private HashMap<String, String> mParameters;

    public TaskParameters(T type) {
        mTaskType = type;
        mParameters = new HashMap<>();
    }

    public void addParameter(String paramName, String paramValue) {
        mParameters.put(paramName, paramValue);
    }

    public int getParameterSize() {
        return mParameters.size();
    }

    public String getParamter(String paramName) {
        return mParameters.getOrDefault(paramName, "");
    }

    public T getTaskType() {
        return mTaskType;
    }
}