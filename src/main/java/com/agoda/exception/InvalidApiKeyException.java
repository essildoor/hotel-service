package com.agoda.exception;

/**
 * Created by Andrey Kapitonov on 11/26/15.
 */
public class InvalidApiKeyException extends SecurityException {

    /**
     * Constructs a <code>SecurityException</code> with the specified
     * detail message.
     *
     * @param s the detail message.
     */
    public InvalidApiKeyException(String s) {
        super(s);
    }
}
