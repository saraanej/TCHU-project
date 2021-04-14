package ch.epfl.tchu.game;

import java.util.List;

/**
 * Modelizes the identity of the player.
 *
 * @author Yasmin Ben Rahhal (329912)
 * @author Sara Anejjar (329905)
 */

public enum PlayerId {

    PLAYER_1,
    PLAYER_2;

    /**
     * List containing all the values of the enum type PlayerId.
     */
    public final static List<PlayerId> ALL = List.of(PlayerId.values());
    /**
     * Integer containing the size of the enum type PlayerId.
     */
    public final static int COUNT = ALL.size();

    /**
     * @return (PlayerId) The identity of the player following the one to whom
     * this method is applied.
     */
    public PlayerId next() {
        return this.equals(PLAYER_1) ? PLAYER_2 : PLAYER_1;
    }
}
