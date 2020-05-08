package player;

import chat.MessageApp;
import message.Message;
import message.MessagePool;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The class defines the player behavior
 */

public class Player implements Runnable {

    private ReentrantLock lock;
    private MessagePool messagePool;
    private PlayerGroup group;
    private GroupCounter groupCounter;
    //used for debugging
    private String name;

    public Player(String name, PlayerGroup group, GroupCounter groupCounter, ReentrantLock lock) {
        this.name = name;
        this.groupCounter = groupCounter;
        this.group = group;
        this.lock = lock;
        this.messagePool = MessagePool.getInstance();
    }

    public void setGroup(PlayerGroup group) {
        this.group = group;
    }

    /**
     * When the player instances will start, the players group will be marked as running using GroupCounter#isRunning;
     *sendFirstMessage() will be invoked by first initiator player, and the messaging action will begin;
     * Each Player will run while the Group variable isRunning will be set to true;
     * A player will retrieve the message and will check if the recipient group is quals to its own group,
     * if the condition is validated, the player will get the message, will append the counter to the message text, and will post it MessagePool.
     *
     * First initiator which will observe that the group reached the maximum allowed sent/received messages, will post a flag message to the pool MessageApp#STOP_PLAYERS_MESSAGE
     * Other Player(s) when will retrieve the flag message, will stop.
     */
    public void run() {
        if (groupCounter.getIsRunning() == null) {
            groupCounter.setIsRunning(new AtomicBoolean(true));
        }
        while (messagePool.getMessage() == null) {
            sendFirstMessage();
        }

        while (groupCounter.getIsRunning().get()) {
            if (lock.tryLock()) {
                Message message = messagePool.getMessage();
                //if STOP_PLAYERS_MESSAGE was retrieved, set the isRunning parameter to false
                if (message.getMessageText().equals(MessageApp.STOP_PLAYERS_MESSAGE)) {
                    groupCounter.getIsRunning().set(false);
                } else if (group.equals(message.getRecipientGroup())) {
                    System.out.println(String.format("player %s retrieved message %s", this.toString(), message.getMessageText()));
                    groupCounter.getReceivedMessagesGroupCounter().incrementAndGet();

                    // if instance is the initiator, already sent and received 10 messages, post the STOP_PLAYERS_MESSAGE in the MessagePool
                    if (group.equals(PlayerGroup.INITIATOR)
                        && groupCounter.getSentMessagesGroupCounter().get() == MessageApp.INITIATOR_MESSAGES_LIMIT
                        && groupCounter.getReceivedMessagesGroupCounter().get()
                           == MessageApp.INITIATOR_MESSAGES_LIMIT) {

                        prepareAndPostMessage(message, message.getSenderGroup(), MessageApp.STOP_PLAYERS_MESSAGE);
                        groupCounter.getIsRunning().set(false);

                    } else {
                        String newMessage =
                            message.getMessageText() + (groupCounter.getSentMessagesGroupCounter().getAndIncrement());
                        prepareAndPostMessage(message, message.getSenderGroup(), newMessage);
                        System.out.println(String.format("player %s sent the message %s", this.toString(), newMessage));
                    }
                }
                lock.unlock();
            }
        }
        System.out.println(String.format("player %s finished the work", this.toString()));

    }

    private void sendFirstMessage() {
        if (lock.tryLock()) {
            if (group.equals(PlayerGroup.INITIATOR)) {
                groupCounter.getSentMessagesGroupCounter().getAndIncrement();
                prepareAndPostMessage(new Message(), PlayerGroup.SUPPORTER, MessageApp.INITIAL_MESSAGE);
                System.out.println(String.format("player %s sent the message %s", this.toString(), messagePool.getMessage().getMessageText()));
            }
            lock.unlock();
        }
    }

    private void prepareAndPostMessage(Message message, PlayerGroup recipientGroup, String messageText) {
        message.setMessageText(messageText);
        message.setRecipientGroup(recipientGroup);
        message.setSenderGroup(group);
        messagePool.setMessage(message);
    }

}
