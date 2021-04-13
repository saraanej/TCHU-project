package ch.epfl.tchu.game;

import java.util.List;

/**
 * Modelizes the identity of the player
 *
 * @author Yasmin Ben Rahhal (329912)
 * @author Sara Anejjar (329905)
 */

public enum PlayerId {

    PLAYER_1,
    PLAYER_2;

    public final static List<PlayerId> ALL = List.of(PlayerId.values());
    public final static int COUNT = ALL.size();

    /**
     * @return (PlayerId) the identity of the player following the one to whom
     * this method is applied
     */
    public PlayerId next() {
        return this.equals(PLAYER_1) ? PLAYER_2 : PLAYER_1;
    }
}
