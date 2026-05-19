package com.example.arenareservas.exception;

public class ReservaNotFoundException extends RuntimeException {
    public ReservaNotFoundException(String mensaje) {
        super(mensaje);
    }
}