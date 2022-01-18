package com.yaani.searchengine.exception;

public class MissingRequiredFieldException extends BaseException{

    public MissingRequiredFieldException(String message) {
        super("Field '" + message + "' is required");
    }
}
