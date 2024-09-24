package com.us.news.aggregationservice.util;

@FunctionalInterface
public interface ThrowingSupplier<T> {
    T get() throws Exception;
}