package com.example.arenawallet.exception;

public class BilleteraNotFoundException extends RuntimeException {
    public BilleteraNotFoundException(String mensaje) {
        super(mensaje);
    }
}