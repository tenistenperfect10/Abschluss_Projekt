package org.texttechnology.parliament_browser_6_4.exception;

public class InputException extends Exception{

    public InputException() {
    }

    public InputException(Throwable pCause) {
        super(pCause);
    }

    public InputException(String pMessage) {
        super(pMessage);
    }

    public InputException(String pMessage, Throwable pCause) {
        super(pMessage, pCause);
    }
}
