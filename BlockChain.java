package blockchain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class BlockChain implements Serializable {
    static final long serialVersionUID = 1L;

    private final List<Block> chain = new ArrayList<>();
    private int zerosNum = 0;
    private final List<Transaction> pendingTransactions = new ArrayList<>();
    private int maxPrevMessageId = 0;

    public BlockChain() {

    }

    public int getZerosNum() {
        return zerosNum;
    }

    public Block newBlock() {
        List<Transaction> transactions = new ArrayList<>();
        if (chain.size() > 0) {
            transactions = new ArrayList<>(pendingTransactions);
            pendingTransactions.clear();
            maxPrevMessageId = transactions.stream().mapToInt(Transaction::getId).max().orElse(0);
        }
        return new Block(getZerosNum(), lastBlock(), transactions);
    }

    public void addBlock(Block block) {
        chain.add(block);
        zerosNum += block.getZerosNumDelta();
    }

    public boolean validate(Block newBlock) {
        Block prevBlock = lastBlock();

        int prevMaxId = prevBlock == null ? 0 : prevBlock.getMessages().stream()
                .mapToInt(Transaction::getId)
                .max().orElse(0);
        int maxId = newBlock.getMessages().stream()
                .mapToInt(Transaction::getId)
                .min().orElse(Integer.MAX_VALUE);
        if (maxId < prevMaxId) {
            System.out.println("The new block has IDs that are earlier than the previous block IDs.");
            return false;
        }

        String prevHash = prevBlock == null ? "0" : prevBlock.getHash();
        if (!newBlock.getPrevHash().equals(prevHash)) {
            System.out.println("The new block's hash for previous block differs from the previous block hash.");
            return false;
        }
        return true;
    }

    public void addMessage(Transaction msg) {
        if (!msg.verify()) {
            System.out.printf("The signature for message '%s' is wrong!%n", msg.getText());
            return;
        }
        if (msg.getId() < maxPrevMessageId) {
            System.out.println("Trying to add message with id from a previous blocks! Denied.");
            return;
        }
        pendingTransactions.add(msg);
    }

    Block lastBlock() {
        return chain.isEmpty() ? null : chain.get(chain.size() - 1);
    }
}

class MineCommand implements Callable<BlockCreationReport> {
    private final Block block;

    public MineCommand(Block block) {
        this.block = block;
    }

    public BlockCreationReport call() {
        return block.generateMagicNumber();
    }
}

