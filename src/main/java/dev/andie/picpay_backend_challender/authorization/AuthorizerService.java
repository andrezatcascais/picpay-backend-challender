package dev.andie.picpay_backend_challender.authorization;

import org.springframework.stereotype.Service;

import dev.andie.picpay_backend_challender.PicpayBackendChallenderApplication;
import dev.andie.picpay_backend_challender.transaction.Transaction;

// in this class we'll call the external service
@Service
public class AuthorizerService {

    private final PicpayBackendChallenderApplication picpayBackendChallenderApplication;

    AuthorizerService(PicpayBackendChallenderApplication picpayBackendChallenderApplication) {
        this.picpayBackendChallenderApplication = picpayBackendChallenderApplication;
    }

    public void authorize(Transaction transaction) {
        // call the external service
        // and it means that will authorize or no
        
        
    }
    
}
