package com.us.news.common.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResponseWrapper<T> {
    private boolean success;
    private String message;
    private T data;
    private Object details;

    public ResponseWrapper(boolean success, String message, T data, Object details) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.details = details;
    }

    public ResponseWrapper(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
}