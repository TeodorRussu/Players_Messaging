package message;

import player.PlayerGroupType;

/**
 * Message object will be used to post messages by a player to the MessagePool
 * Each message will have properties like:
 * - sender: Message#senderGroup;
 * - recipient: Message#recipientGroup;
 * - messageText: Message#messageText;
 */
public class Message {

    private PlayerGroupType senderGroup;
    private PlayerGroupType recipientGroup;
    private String messageText = "initial";

    public PlayerGroupType getSenderGroup() {
        return senderGroup;
    }

    public void setSenderGroup(PlayerGroupType senderGroup) {
        this.senderGroup = senderGroup;
    }

    public PlayerGroupType getRecipientGroup() {
        return recipientGroup;
    }

    public void setRecipientGroup(PlayerGroupType recipientGroup) {
        this.recipientGroup = recipientGroup;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }
}


