package player;

import config.PlayersData;
import message.Message;
import message.MessagePool;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

/**
 * The class defines the player behavior
 */

public class Player implements Runnable {

    Logger logger = Logger.getAnonymousLogger();

    private ReentrantLock lock;
    private MessagePool messagePool;
    private PlayerGroupType group;
    private PlayerGroupManager playerGroupManager;
    private String name;

    public Player(String name, PlayerGroupType group, PlayerGroupManager playerGroupManager, ReentrantLock lock) {
        this.name = name;
        this.playerGroupManager = playerGroupManager;
        this.group = group;
        this.lock = lock;
        this.messagePool = MessagePool.getInstance();
    }

    /**
     * When the player instances will start, the players group will be marked as running using GroupCounter#isRunning;
     * sendFirstMessage() will be invoked by first initiator player, and the messaging action will begin; Each Player
     * will run while the Group variable isRunning will be set to true; A player will retrieve the message and will
     * check if the recipient group is quals to its own group, if the condition is validated, the player will get the
     * message, will append the counter to the message text, and will post it MessagePool.
     * <p>
     * First initiator which will observe that the group reached the maximum allowed sent/received messages, will post a
     * flag message to the pool MessageApp#STOP_PLAYERS_MESSAGE Other Player(s) when will retrieve the flag message,
     * will stop.
     */
    public void run() {
        final int initiatorMessagesLimit = PlayersData.getInstance().getInitiatorMessagesLimit();
        final String flagStopMessage = PlayersData.getInstance().getFlagStopMessage();

        if (playerGroupManager.getIsRunning() == null) {
            playerGroupManager.setIsRunning(new AtomicBoolean(true));
        }
        while (messagePool.getMessage() == null) {
            sendFirstMessage();
        }

        while (playerGroupManager.getIsRunning().get()) {
            if (lock.tryLock()) {
                Message message = messagePool.getMessage();
                //if STOP_PLAYERS_MES
                // SAGE was retrieved, set the isRunning parameter to false

                if (message.getMessageText().equals(flagStopMessage)) {
                    playerGroupManager.getIsRunning().set(false);
                } else if (group.equals(message.getRecipientGroup())) {
                    logger.info(
                        String.format("player %s retrieved message %s", this.toString(), message.getMessageText()));
                    playerGroupManager.getReceivedMessagesGroupCounter().incrementAndGet();

                    // if instance is the initiator, already sent and received 10 messages, post the STOP_PLAYERS_MESSAGE in the MessagePool
                    if (group.equals(PlayerGroupType.INITIATOR)
                        && playerGroupManager.getSentMessagesGroupCounter().get() == initiatorMessagesLimit
                        && playerGroupManager.getReceivedMessagesGroupCounter().get()
                           == initiatorMessagesLimit) {

                        prepareAndPostMessage(message, message.getSenderGroup(), flagStopMessage);
                        playerGroupManager.getIsRunning().set(false);

                    } else {
                        String newMessage =
                            message.getMessageText() + (playerGroupManager.getSentMessagesGroupCounter().getAndIncrement());

                        prepareAndPostMessage(message, message.getSenderGroup(), newMessage);
                        logger.info(String.format("player %s sent the message %s", this.toString(), newMessage));
                    }
                }
                lock.unlock();
            }
        }
        logger.info(String.format("player %s finished the work", this.toString()));

    }

    private void sendFirstMessage() {
        if (lock.tryLock()) {
            final String initialMessage = PlayersData.getInstance().getInitialMessage();

            if (group.equals(PlayerGroupType.INITIATOR)) {
                playerGroupManager.getSentMessagesGroupCounter().getAndIncrement();
                prepareAndPostMessage(new Message(), PlayerGroupType.SUPPORTER,
                                      initialMessage);
                logger.info(String.format("player %s sent the message %s", this.toString(),
                                          messagePool.getMessage().getMessageText()));
            }

            lock.unlock();
        }
    }

    private void prepareAndPostMessage(Message message, PlayerGroupType recipientGroup, String messageText) {
        message.setMessageText(messageText);
        message.setRecipientGroup(recipientGroup);
        message.setSenderGroup(group);
        messagePool.setMessage(message);
    }

    @Override
    public String toString() {
        return "Player{" +
               "name='" + name + '\'' + group.toString() +
               '}';
    }
}
