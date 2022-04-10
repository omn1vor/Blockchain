package blockchain;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {

        Chat chat = new Chat();
        BlockChain chain = new BlockChain();

        int processesCount = Runtime.getRuntime().availableProcessors();
        ExecutorService miner = Executors.newFixedThreadPool(processesCount);
        ExecutorService client = Executors.newSingleThreadExecutor();

        client.submit(() -> {
            try {
                Thread.sleep(5);
            } catch (InterruptedException ignored) {}
            for (Transaction msg : chat.dialog) {
                try {
                    Thread.sleep(5);
                } catch (InterruptedException ignored) {}
                chain.addMessage(msg);
            }
        });

        IntStream.range(0, 15).forEach(ignored -> {
            Block newBlock = chain.newBlock();
            List<MineCommand> list = new ArrayList<>();
            IntStream.range(0, processesCount).forEach(ignoredAgain -> {
                list.add(new MineCommand(newBlock));
            });
            try {
                BlockCreationReport report = miner.invokeAny(list);
                newBlock.setMagicNumber(report);
                if (chain.validate(newBlock)) {
                    chain.addBlock(newBlock);
                    System.out.println(newBlock);
                } else {
                    System.out.println("wrong block, skipping");
                }
            } catch (InterruptedException e) {
                throw new RuntimeException("interrupted by the user!");
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });
        client.shutdown();
        miner.shutdown();
    }



}
