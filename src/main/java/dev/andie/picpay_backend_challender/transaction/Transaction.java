package dev.andie.picpay_backend_challender.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("TRANSACTIONS")
public record Transaction(
    @Id Long id,
    // Common User
    Long payer,

    // Shopkeeper User
    Long payee,
    BigDecimal value,
    // @CreatedDate will enable auditing on the database, 
    // Then set to the application 'PicpayBackendChallenderApplication' file 
    // and add this @EnableJdbcAuditing annotation
    @CreatedDate LocalDateTime createdAt ) {

        // construction
        public Transaction {
            // set to 2 decimal places
            value = value.setScale(2);
        }

}
