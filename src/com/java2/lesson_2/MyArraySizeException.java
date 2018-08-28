package com.java2.lesson_2;

public class MyArraySizeException extends Exception {
    public MyArraySizeException() {
        super("Array size must be 4x4");
    }
}
