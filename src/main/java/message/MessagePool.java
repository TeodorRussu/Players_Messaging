package message;

/**
 * The class will act as a broker to store and offer messages between Players. Messages will be posted and stored in the
 * field {@link MessagePool#message};
 */
public class MessagePool {

    private Message message;

    private MessagePool() {
    }

    private static class Holder {
        private static final MessagePool INSTANCE = new MessagePool();
    }

    public static MessagePool getInstance() {
        return Holder.INSTANCE;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
