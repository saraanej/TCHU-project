package ch.epfl.tchu.game;

import java.util.ArrayList;
import java.util.List;

/**
 * The public enumerated type PlayerId, represents the identity of a player.
 *
 * @author Yasmin Ben Rahhal (329912)
 * @author Sara Anejjar (329905)
 */
public enum PlayerId {

    PLAYER_1,
    PLAYER_2,
    PLAYER_3,
    PLAYER_4;

    /**
     * List containing all the values of the enum type PlayerId.
     */
    private static List<PlayerId> ALL = new ArrayList<>();

    public static void setNumberPlayers(int number) { //todo: check si solution correcte
        for (int i = 1; i <= number; i++) {
            ALL.add(PlayerId.valueOf(String.format("PLAYER_%d",i)));
        }
    }

    public static List<PlayerId> all(){
        return List.copyOf(ALL);
    }

    /**
     * Integer containing the size of the enum type PlayerId.
     */
    public final static int COUNT = ALL.size();


    /**
     * @return The identity of the player following the one to whom
     * this method is applied.
     * Flexible for more than two players.
     */
    public PlayerId next() {
        int index = this.ordinal();
        return index == COUNT - 1 ? ALL.get(0) : ALL.get(index + 1);
    }
}
