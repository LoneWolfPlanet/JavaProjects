package com.dyntrace.download.metric.util;

public enum FileType {
    CSV(1),
    JSON(2);
    
    private final int statusCode;

    FileType(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}