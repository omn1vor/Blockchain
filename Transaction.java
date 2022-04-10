package blockchain;

import java.io.Serializable;
import java.security.PublicKey;

public class Transaction implements Serializable {
    private final int id;
    private final String text;
    private final Person person;
    private final PublicKey publicKey;
    private final byte[] signature;

    public Transaction(Person person, String text, int id) {
        this.person = person;
        this.text = text;
        this.id = id;
        this.publicKey = person.getPublicKey();
        this.signature = Signatures.sign(text, person.getPrivateKey());
    }

    public String getText() {
        return text;
    }

    public int getId() {
        return id;
    }

    public boolean verify() {
        return Signatures.verifySignature(text, signature, publicKey);
    }
}
