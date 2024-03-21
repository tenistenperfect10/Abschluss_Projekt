package org.texttechnology.parliament_browser_6_4.exception;

/**
 * The {@code InputException} class is a custom exception that extends {@link Exception}.
 * It is designed to represent errors that occur due to invalid input provided to the system.
 * This class supports different constructors for specifying error messages and underlying causes of the exception.
 */
public class InputException extends Exception{
    /**
     * Constructs a new {@code InputException} with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a call to {@link #initCause}.
     */
    public InputException() {
    }

    /**
     * Constructs a new {@code InputException} with the specified cause and a detail message of
     * {@code (cause==null ? null : cause.toString())} (which typically contains the class and detail message of
     * {@code cause}). This constructor is useful for exceptions that are little more than wrappers for other throwables.
     *
     * @param pCause The cause (which is saved for later retrieval by the {@link #getCause()} method).
     *               A {@code null} value is permitted, and indicates that the cause is nonexistent or unknown.
     */
    public InputException(Throwable pCause) {
        super(pCause);
    }
    /**
     * Constructs a new {@code InputException} with the specified detail message. The cause is not initialized,
     * and may subsequently be initialized by a call to {@link #initCause}.
     *
     * @param pMessage The detail message. The detail message is saved for later retrieval by the {@link #getMessage()} method.
     */
    public InputException(String pMessage) {
        super(pMessage);
    }
    /**
     * Constructs a new {@code InputException} with the specified detail message and cause.
     * <p>Note that the detail message associated with {@code cause} is not automatically incorporated in
     * this exception's detail message.</p>
     *
     * @param pMessage The detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     * @param pCause   The cause (which is saved for later retrieval by the {@link #getCause()} method).
     *                 A {@code null} value is permitted, and indicates that the cause is nonexistent or unknown.
     */
    public InputException(String pMessage, Throwable pCause) {
        super(pMessage, pCause);
    }
}
