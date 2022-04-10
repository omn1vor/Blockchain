package blockchain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Block implements Serializable {
    static final long serialVersionUID = 1L;

    private final long id;
    private final long timeStamp;
    private final String prevHash;
    private final int zerosNum;
    private long magicNumber;
    private long buildingTime;
    private long workerId;
    private final List<Transaction> transactions;

    public Block(int zerosNum, Block prevBlock, List<Transaction> transactions) {
        this.zerosNum = zerosNum;
        this.id = (prevBlock == null ? 0 : prevBlock.getId()) + 1;
        this.transactions = transactions;
        timeStamp = new Date().getTime();
        prevHash = prevBlock == null ? "0" : prevBlock.getHash();
    }

    public long getId() {
        return id;
    }

    public List<Transaction> getMessages() {
        return transactions;
    }

    public String getPrevHash() {
        return prevHash;
    }

    public String getHash() {
        return getHash(this.magicNumber);
    }

    public String getHash(long magicNumber) {
        String blockInfo = String.format("%s;%s;%d;%s", id, timeStamp, magicNumber, prevHash);
        return StringUtil.applySha256(blockInfo);
    }

    public long getBuildingTime() {
        return buildingTime;
    }

    @Override
    public String toString() {
        return String.format("Block:%n" +
                "Created by miner%d%n" +
                "Miner%d gets 100 VC %n" +
                "Id: %s%n" +
                "Timestamp: %s%n" +
                "Magic number: %d%n" +
                "Hash of the previous block:%n%s%n" +
                "Hash of the block:%n%s%n" +
                "%s%n" +
                "Block was generating for %d seconds%n" +
                "%s%n",
                workerId, workerId, id, timeStamp, magicNumber, prevHash, getHash(),
                getFormattedBlockData(), buildingTime, getDeltaNString());
    }

    public int getZerosNumDelta() {
        final int STABLE_TIME = 1;
        final int LONG_TIME = 5;
        long lastBuildTime = getBuildingTime();
        if (lastBuildTime < STABLE_TIME) {
            return 1;
        } else if (lastBuildTime > LONG_TIME) {
            return -1;
        }
        return 0;
    }

    public BlockCreationReport generateMagicNumber() {
        BlockCreationReport result = new BlockCreationReport();
        long start = new Date().getTime();
        long number;
        do {
            number = (long) (Math.random() * Long.MAX_VALUE);
        } while (!getHash(number).startsWith("0".repeat(zerosNum)));

        result.magicNumber = number;
        result.buildingTime = (new Date().getTime() - start) / 100; // it should be 1000
        // but tests give you only 15 seconds to build 15 blocks, so we make it build simpler
        // blocks by making our seconds 10 times longer.
        result.minerId = Thread.currentThread().getId();
        return result;
    }

    public void setMagicNumber(BlockCreationReport report) {
        magicNumber = report.magicNumber;
        workerId = report.minerId;
        buildingTime = report.buildingTime;
    }

    private String getDeltaNString() {
        String deltaNString = "N stays the same";
        int deltaN = getZerosNumDelta();
        if (deltaN > 0) {
            deltaNString = "N was increased to " + (zerosNum + deltaN);
        } else if (deltaN < 0) {
            deltaNString = "N was decreased to " + (zerosNum + deltaN);
        }
        return deltaNString;
    }

    private String getFormattedBlockData() {
        if (transactions.isEmpty()) {
            return "Block data: no messages";
        }
        return String.format("Block data:%n%s",
                transactions.stream()
                        .map(Transaction::getText)
                        .collect(Collectors.joining(System.lineSeparator())));
    }
}

class BlockCreationReport {
    long magicNumber;
    long buildingTime;
    long minerId;
}