package player;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Each player will belong to a group.
 * The group will share across player instances properties such as:
 * - total messages sent (per group), controlled by the field {@link GroupCounter#sentMessagesGroupCounter};
 * - total messages received (per group), controlled by the field {@link GroupCounter#receivedMessagesGroupCounter};
 * - if the group shoud be active or not, controlled by the field {@link GroupCounter#isRunning}
 */
public class GroupCounter {

    private AtomicBoolean isRunning;
    private AtomicInteger sentMessagesGroupCounter;
    private AtomicInteger receivedMessagesGroupCounter;

    public GroupCounter() {
        sentMessagesGroupCounter = new AtomicInteger();
        receivedMessagesGroupCounter = new AtomicInteger();
    }

    public AtomicInteger getSentMessagesGroupCounter() {
        return sentMessagesGroupCounter;
    }

    public AtomicInteger getReceivedMessagesGroupCounter() {
        return receivedMessagesGroupCounter;
    }

    public AtomicBoolean getIsRunning() {
        return isRunning;
    }

    public void setIsRunning(AtomicBoolean isRunning) {
        this.isRunning = isRunning;
    }
}
