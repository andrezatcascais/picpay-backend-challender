package dev.andie.picpay_backend_challender.exception;

public class InvalidTransactionException extends RuntimeException {

    /**
     * 
     * @param message
     * when I do this, it'll be able to handle this exception centrally, 
     * cuz it created a specific exception, so it can have a handler that 
     * handles this exception. Let's show the example when we create the web layer
     */
    public InvalidTransactionException(String message) {
        super(message);
    }

    
}
