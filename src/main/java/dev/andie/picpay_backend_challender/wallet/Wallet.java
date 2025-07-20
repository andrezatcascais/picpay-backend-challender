package dev.andie.picpay_backend_challender.wallet;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("WALLETS")
public record Wallet(
        @Id
        Long id,
        String fullName,
        Long cpf,
        String email,
        String password,
        int type,
        BigDecimal balance) {

    /* 
   This method returns a Wallet object, as records are immutable. 
   Thus, any field update requires creating a new instance. 
   Here, the method subtracts the provided value from the balance.
     */
    public Wallet debit(BigDecimal value) {
        return new Wallet(id, fullName, cpf, email, password, type, balance.subtract(value));
    }

}
