package blockchain;

import java.io.*;

public class IOUtil {
    private static final String fileName = "chain.data";

    public static boolean fileExists() {
        File file = new File(fileName);
        return file.isFile();
    }

    public static void save(BlockChain chain) {
        try (FileOutputStream fos = new FileOutputStream(fileName);
             BufferedOutputStream bos = new BufferedOutputStream(fos);
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(chain);
        } catch (IOException e) {
            throw new RuntimeException("Error while saving block chain", e);
        }
    }

    public static BlockChain load() {
        try (FileInputStream fis = new FileInputStream(fileName);
             BufferedInputStream bis = new BufferedInputStream(fis);
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            return (BlockChain) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Error while saving block chain", e);
        }
    }
}
