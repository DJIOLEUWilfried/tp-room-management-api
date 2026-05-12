package com.iuc.tpiuc.exception.custom;

public class InvalidReservationTimeException extends RuntimeException {

    public InvalidReservationTimeException(String message) {
        super(message);
    }
}
