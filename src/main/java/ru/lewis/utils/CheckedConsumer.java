package ru.lewis.utils;

import java.io.IOException;

@FunctionalInterface
public interface CheckedConsumer<T> {
    void accept(T t) throws IOException;
}
