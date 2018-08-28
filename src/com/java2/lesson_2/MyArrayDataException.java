package com.java2.lesson_2;

public class MyArrayDataException extends Exception {
    public MyArrayDataException(int i, int j, Throwable cause) {
        super("Wrong data in cell [" + i + "][" + j + "]. Error: " + cause.toString());
    }
}
