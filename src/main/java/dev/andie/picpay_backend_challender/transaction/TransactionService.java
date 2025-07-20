package dev.andie.picpay_backend_challender.transaction;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.andie.picpay_backend_challender.authorization.AuthorizerService;
import dev.andie.picpay_backend_challender.exception.InvalidTransactionException;
import dev.andie.picpay_backend_challender.wallet.Wallet;
import dev.andie.picpay_backend_challender.wallet.WalletRepository;
import dev.andie.picpay_backend_challender.wallet.WalletType;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final AuthorizerService authorizerService;

    public TransactionService(
            TransactionRepository transactionRepository,
            WalletRepository walletRepository, AuthorizerService authorizerService) {
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
        this.authorizerService = authorizerService;
    }

    /**
     *
     * @param transaction
     * @return To do @Transactional annotation on method, it means that if an
     * operation that has interation with the Database fails, then it'll do the
     * rollback of all these operations performed in the database.
     */
    @Transactional
    public Transaction createTransaction(Transaction transaction) {
        // 1. Validate the transaction based on business rules.Create the structure of
        // folders and packages, I have three entities
        validate(transaction);

        // 2. Create the Transaction.
        var newTransaction = transactionRepository.save(transaction);

        // 3. Debit the Wallet from Common User
        // With get(), I obtain the returned registration effectively.
        var walletPayer = walletRepository.findById(transaction.payer()).get();
        // The wallet must subtract the transaction amount from its balance, and the
        // Wallet can know it.
        // The 'debit' method removes the amount from the wallet.
        walletRepository.save(walletPayer.debit(transaction.value()));

        // 4. Call the external services.
        // Don't put on about Autorized here, cuz we must separate the responsability,
        // so let's create a new service to delegated this responsability
        // let's use to call the service 
        authorizerService.authorize(newTransaction);

        // return the transaction
        return newTransaction;

    }

    // We need to validate the data here to allow querying from the database.
    // The Bean Validation library can handle simple checks, as validating the
    // fields,
    // but we require more thorough validations in this case. We will use this
    // method.
    // Suggestion: Implementing Custom Annotation Validation According to Business
    // Rules
    // in Java with Spring Boot.
    // For simplicity, we'll implement it in the method itself.
    /**
     *
     * @param transaction
     *
     * 3 rules should be verified to know if the transaction is valid: 1. The
     * payer has a common user wallet 2. The payer has enough balance 3. The
     * payer is not the payee (shopkeeper).
     *
     */
    private void validate(Transaction transaction) {
        /**
         * To verify these three rules, we will use a Lambda function strategy
         * to chain everything together in a single command.
         */
        // Why did we search for the payee (recipient or shopkeeper) first (1.)?
        // If I didn't use this data to do any verification? Because now that we have
        // two maps
        // what can we do at the end of this internal map,
        // this one that checks for a isTransactionValid(transaction, payer) - continue
        // line 85
        // 1. getId of the payee (shopkeeper)
        walletRepository.findById(transaction.payee())
                // 2. When the payee is found so it'll map the result to find now the payer
                .map(payee -> walletRepository.findById(transaction.payer())
                // when we do this, we're applying SOLID to improve readability, maintainability
                // and
                // clarity, the code becomes much easier to read.
                .map(payer -> isTransactionValid(transaction, payer) ? transaction : null)
                // else(:) null, because if it return null in this mapping,
                // it'll be able to handle it, and what behavior do we want to be activated
                // when there is no valid transaction being returned
                // we can put on this orElseThrow() and throw a specif exception, to say the
                // transaction
                // is invalid, if it doesn't find a record at the end of this mapping operation
                // it was created the specific "InvalidTransactionException", that we can use
                // here (orElseThrow) and inform
                // the message. Any of these criteria were not met, and then we can pass the
                // transaction so the person can evaluate which
                // of the criteria was not met, so we will concatenate with the transaction i
                // fact
                // .orElseThrow(() -> new InvalidTransactionException("Invalid transaction - " +
                // transaction)))
                .orElseThrow(() -> new InvalidTransactionException(
                "Invalid transaction - %s".formatted(transaction))))
                // if it doesn't find the recipient (payer / common user) or if return null, so
                // an exception will throw.
                // We'll use an action-launching approach, an this approach is very common, when
                // we work with business operations,
                // we create a specific exception for the business that will facilitate the
                // readability, analyzing
                // or debbugging the behavior of application.
                .orElseThrow(() -> new InvalidTransactionException("Invalid transaction - %s".formatted(transaction)));

    }

    private boolean isTransactionValid(Transaction transaction, Wallet payer) {
        // 3. and then map the result again. This payer will be to use:
        // 3.1 to check if the its type is equal "Common User";
        // 3.2 To check will be if there is a balance in the payer's account,
        // it must be greater than or equal to zero, it means will ensure the payer has
        // the exact balance to make the transaction, or more than that, never less.
        // 3.3 And finally, the third check is to know if the Ids are different.
        // If the payer's ID is not equal to the shopkeeper's ID.
        // If it is true ? return the transaction
        return payer.type() == WalletType.COMMON.getValue()
                && payer.balance().compareTo(transaction.value()) >= 0
                && !payer.id().equals(transaction.payee());
    }
}
