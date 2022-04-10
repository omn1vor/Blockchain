package blockchain;

import java.io.Serializable;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public class Person implements Serializable {
    KeyPair keys;

    public Person() {
        keys = Signatures.generateKeys();
    }

    PrivateKey getPrivateKey() {
        return keys.getPrivate();
    }

    public PublicKey getPublicKey() {
        return keys.getPublic();
    }
}
