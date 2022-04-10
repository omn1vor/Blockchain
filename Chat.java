package blockchain;

import java.util.ArrayList;
import java.util.List;

public class Chat {
    List<Transaction> dialog = new ArrayList<>();
    MessageIdGenerator IdGenerator = new MessageIdGenerator();

    public Chat() {
        initDialog();
    }

    private void initDialog() {
        Person mike = new Person();
        Person nick = new Person();

        dialog.add(new Transaction(mike, "Hey, Nick.", IdGenerator.getId()));
        dialog.add(new Transaction(nick, "Hey, Mike.", IdGenerator.getId()));
        dialog.add(new Transaction(mike, "How are you doing in Belarus?", IdGenerator.getId()));
        dialog.add(new Transaction(nick, "To be honest, not very good.\n" +
                "Nick: President Lukashenko proved to be a hard nut to crack.\n" +
                "Nick: They are professional and well organised.", IdGenerator.getId()));
        dialog.add(new Transaction(mike, "Yes. Sure.", IdGenerator.getId()));
    }
}

class MessageIdGenerator {
    private int id = 0;

    public int getId() {
        return ++id;
    }
}
