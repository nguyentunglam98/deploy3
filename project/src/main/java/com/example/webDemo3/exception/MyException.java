package com.example.webDemo3.exception;

public class MyException extends Exception{
    private String message;

    public MyException() {
    }

    public MyException(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
