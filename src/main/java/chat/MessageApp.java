package chat;

import config.PlayersData;
import player.PlayerGroupManager;
import player.Player;
import player.PlayerGroupType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

public class MessageApp {

    private static Logger logger = Logger.getAnonymousLogger();

    /**
     * The program can work with groups of players and the group size may be passed as first argument (e.g if args[0]
     * will be 3, then 3 initiators and 3 supporters will play together, and their messages will be counted per group),
     * if there are no arguments provided, then default group size of 1 initiator and 1 supporter will be used
     * <p>
     * Second program parameter will define the messaging limit (sent and received by the initiator), if no argument is
     * provided, the default limit of 10 will be used
     */
    public static void main(String[] args) {
        logger.info("let the game begin!");
        loadPlayersData(args);

        ReentrantLock lock = new ReentrantLock();
        PlayerGroupManager initiatorsCounter = new PlayerGroupManager();
        PlayerGroupManager supportersCounter = new PlayerGroupManager();

        //init players threads
        List<Thread> allPlayers = new ArrayList<>();

        for (int i = 0; i < PlayersData.getInstance().getGroupSize(); i++) {
            Player initiator = new Player(String.valueOf(i), PlayerGroupType.INITIATOR, initiatorsCounter, lock);
            Player supporter = new Player(String.valueOf(i), PlayerGroupType.SUPPORTER, supportersCounter, lock);

            allPlayers.add(new Thread(initiator));
            allPlayers.add(new Thread(supporter));
        }

        //start the messaging process
        allPlayers.forEach(Thread::start);
        allPlayers.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                logger.warning(e.getMessage());
            }
        });

        logger.info("game over!");
    }

    private static void loadPlayersData(String[] args) {
        //assuming the data passed as args will be always valid, no program arguments parsing exception handled
        Integer groupSize= args.length > 0 ? Integer.parseInt(args[0]) : null;
        Integer initiatorMessagesLimit= args.length > 1 ? Integer.parseInt(args[1]) : null;
        String initialMessage = args.length > 2 ? args[2] : null;
        String flagStopMessage= args.length > 3 ? args[3] : null;

        PlayersData.loadPlayersData(groupSize, initiatorMessagesLimit, initialMessage, flagStopMessage);

    }
}
