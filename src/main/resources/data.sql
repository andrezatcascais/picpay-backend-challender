/* clean all tables */
DELETE FROM TRANSACTIONS;

DELETE FROM WALLETS;

/* insert common user  */
INSERT INTO 
    WALLETS (
        ID, FULL_NAME, CPF, EMAIL, "PASSWORD", "TYPE", BALANCE
        ) 
VALUES (
    1, 'John - common user', 12345678900, 'john@test.com', '123456', 1, 1000.0    
);

/* insert shopkeeper user  */
INSERT INTO 
    WALLETS (
        ID, FULL_NAME, CPF, EMAIL, "PASSWORD", "TYPE", BALANCE
        ) 
VALUES (
    2, 'Mary - shopkeeper user', 12345678901, 'mary@test.com', '123456', 2, 1000.0    
);

