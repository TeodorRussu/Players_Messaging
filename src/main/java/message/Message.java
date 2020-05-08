package message;

import player.GroupCounter;
import player.PlayerGroup;

/**
 * Message object will be used to post messages by a player to the MessagePool
 * Each message will have properties like:
 * - sender: Message#senderGroup;
 * - recipient: Message#recipientGroup;
 * - messageText: Message#messageText;
 */
public class Message {

    private PlayerGroup senderGroup;
    private PlayerGroup recipientGroup;
    private String messageText = "initial";

    public PlayerGroup getSenderGroup() {
        return senderGroup;
    }

    public void setSenderGroup(PlayerGroup senderGroup) {
        this.senderGroup = senderGroup;
    }

    public PlayerGroup getRecipientGroup() {
        return recipientGroup;
    }

    public void setRecipientGroup(PlayerGroup recipientGroup) {
        this.recipientGroup = recipientGroup;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }
}


