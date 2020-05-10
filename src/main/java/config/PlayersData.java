package config;

import java.text.MessageFormat;
import java.util.logging.Logger;

/**
 * Players data such as  initial message, default group size and default messge limit are stored in this class, by
 * calling the method {@link #loadPlayersData(Integer, Integer, String, String)}. If no data is provided by command line
 * java arguments, default values will be used. For simplicity, the default values are hardcoded in the constructor,
 * rather than loaded from a .properties file.
 */
public class PlayersData {



    private static PlayersData instance;

    private final Integer groupSize;
    private final Integer initiatorMessagesLimit;
    private final String initialMessage;
    private final String flagStopMessage;


    public static void loadPlayersData(Integer groupSize, Integer initiatorMessagesLimit, String initialMessage,
                                       String flagStopMessage) {
        if (instance == null) {
            instance = new PlayersData(initialMessage, flagStopMessage, groupSize, initiatorMessagesLimit);
            Logger.getAnonymousLogger().info(MessageFormat.format("following properties loaded: {0}", instance));
        }
    }

    /**
     * Set properties using user data, or use default values
     *
     * @param initialMessage String
     * @param flagStopMessage String
     * @param groupSize int
     * @param initiatorMessagesLimit int
     */
    private PlayersData(String initialMessage, String flagStopMessage, Integer groupSize,
                        Integer initiatorMessagesLimit) {

        this.initialMessage = initialMessage != null ? initialMessage : "Hello";
        this.flagStopMessage =
            flagStopMessage != null ? flagStopMessage : "No more messages for today, have some rest, you deserve it!";
        this.groupSize = groupSize != null ? groupSize : 1;
        this.initiatorMessagesLimit = initiatorMessagesLimit != null ? initiatorMessagesLimit : 10;
    }

    public String getInitialMessage() {
        return initialMessage;
    }

    public String getFlagStopMessage() {
        return flagStopMessage;
    }

    public int getGroupSize() {
        return groupSize;
    }

    public int getInitiatorMessagesLimit() {
        return initiatorMessagesLimit;
    }

    public static PlayersData getInstance() {
        return instance;
    }

    @Override
    public String toString() {
        return "PlayersData{" +
               "groupSize=" + groupSize +
               ", initiatorMessagesLimit=" + initiatorMessagesLimit +
               ", initialMessage='" + initialMessage + '\'' +
               ", flagStopMessage='" + flagStopMessage + '\'' +
               '}';
    }
}
