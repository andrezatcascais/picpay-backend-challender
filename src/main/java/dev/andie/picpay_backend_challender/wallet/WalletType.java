package dev.andie.picpay_backend_challender.wallet;

public enum WalletType {
    COMMON(1), SHOPKEEPER(2);

    private int value;

    private WalletType(int value) {
        this.value = value;
    }

    // to interation with db
    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
    
}
