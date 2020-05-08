package chat;

import player.GroupCounter;
import player.Player;
import player.PlayerGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class MessageApp {

    //initiators and supporters groups size
    private static final int GROUP_SIZE = 1;
    public static final int INITIATOR_MESSAGES_LIMIT = 10;
    public static final String INITIAL_MESSAGE = "Hello";
    public static final String STOP_PLAYERS_MESSAGE = "No more messages for today, have some rest, you deserve it!";

    public static void main(String[] args) {

        System.out.println("let the game begin!");

        ReentrantLock lock = new ReentrantLock();
        GroupCounter initiatorsCounter = new GroupCounter();
        GroupCounter supportersCounter = new GroupCounter();

        //init players threads
        List<Thread> allPlayers = new ArrayList<>();
        for (int i = 0; i < GROUP_SIZE; i++) {
            Player initiator = new Player(String.valueOf(i), PlayerGroup.INITIATOR, initiatorsCounter, lock);
            Player supporter = new Player(String.valueOf(i), PlayerGroup.SUPPORTER, supportersCounter, lock);

            Thread threadInitiator = new Thread(initiator);
            Thread threadSupporter = new Thread(supporter);
            allPlayers.add(threadInitiator);
            allPlayers.add(threadSupporter);
        }

        //start the messaging process
        allPlayers.forEach(Thread::start);
        allPlayers.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        System.out.println("game over!");
    }
}
