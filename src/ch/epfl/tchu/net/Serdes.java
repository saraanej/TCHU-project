package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;

public class Serdes {

    private Serdes(){
        throw new UnsupportedOperationException();
    }

    public static final Serde<Integer> INTEGER_SERDE = Serde.of(
            i -> Integer.toString(i),
            Integer::parseInt);

    public static final Serde<String> STRING_SERDE = new Serde<String>() {
        @Override
        public String serialize(String s) {
            return Base64.getEncoder().encodeToString(s.getBytes(StandardCharsets.UTF_8));
        }

        @Override
        public String deserialize(String str) {
            return new String(Base64.getDecoder().decode(str), StandardCharsets.UTF_8);
        }
    };

    public static final Serde<PlayerId> PLAYER_ID_SERDE = Serde.oneOf(PlayerId.ALL);

    public static final Serde<Player.TurnKind> TURN_KIND_SERDE = Serde.oneOf(Player.TurnKind.ALL);

    public static final Serde<Card> CARD_SERDE = Serde.oneOf(Card.ALL);

    public static final Serde<Route> ROUTE_SERDE = Serde.oneOf(ChMap.routes());

    public static final Serde<Ticket> TICKET_SERDE = Serde.oneOf(ChMap.tickets());

    public static final Serde<List<String>> LIST_STRING_SERDE = Serde.listOf(STRING_SERDE, ",");

    public static final Serde<List<Card>> LIST_CARD_SERDE = Serde.listOf(CARD_SERDE, ",");

    public static final Serde<List<Route>> LIST_ROUTE_SERDE = Serde.listOf(ROUTE_SERDE, ",");

    public static final Serde<SortedBag<Card>> SORTED_CARD_SERDE = Serde.bagOf(CARD_SERDE, ",");

    public static final Serde<SortedBag<Ticket>> SORTED_TICKET_SERDE = Serde.bagOf(TICKET_SERDE, ",");

    public static final Serde<List<SortedBag<Card>>> LIST_SORTED_CARD_SERDE = Serde.listOf(SORTED_CARD_SERDE, ";");

    public static final Serde<PublicCardState> PUBLIC_CARDSTATE_SERDE = new Serde<PublicCardState>() {
        @Override
        public String serialize(PublicCardState p) {
            String[] serialized = new String[]{LIST_CARD_SERDE.serialize(p.faceUpCards()),
                    INTEGER_SERDE.serialize(p.deckSize()), INTEGER_SERDE.serialize(p.discardsSize())};
            return String.join(";", serialized);
        }

        @Override
        public PublicCardState deserialize(String str) {
            String[] split = str.split(Pattern.quote(";"),-1);
            return new PublicCardState(LIST_CARD_SERDE.deserialize(split[0]),
                    INTEGER_SERDE.deserialize(split[1]), INTEGER_SERDE.deserialize(split[2]));
        }
    };

    public static final Serde<PublicPlayerState> PUBLIC_PLAYERSTATE_SERDE = new Serde<PublicPlayerState>() {
        @Override
        public String serialize(PublicPlayerState p) {
            String[] serialized = new String[]{INTEGER_SERDE.serialize(p.ticketCount()),
                    INTEGER_SERDE.serialize(p.cardCount()), LIST_ROUTE_SERDE.serialize(p.routes())};
            return String.join(";", serialized);
        }

        @Override
        public PublicPlayerState deserialize(String str) {
            String[] split = str.split(Pattern.quote(";"),-1);
            return new PublicPlayerState(INTEGER_SERDE.deserialize(split[0]), INTEGER_SERDE.deserialize(split[1]),
                    LIST_ROUTE_SERDE.deserialize(split[2]));
        }
    };

    public static final Serde<PlayerState> PLAYERSTATE_SERDE = new Serde<PlayerState>() {
        @Override
        public String serialize(PlayerState p) {
            String[] serialized = new String[]{SORTED_TICKET_SERDE.serialize(p.tickets()),
                    SORTED_CARD_SERDE.serialize(p.cards()), LIST_ROUTE_SERDE.serialize(p.routes())};
            return String.join(";", serialized);
        }

        @Override
        public PlayerState deserialize(String str) {
            String[] split = str.split(Pattern.quote(";"),-1);
            return new PlayerState(SORTED_TICKET_SERDE.deserialize(split[0]),SORTED_CARD_SERDE.deserialize(split[1]),
                    LIST_ROUTE_SERDE.deserialize(split[2]));
        }
    };

    public final static Serde<PublicGameState> PUBLIC_GAMESTATE_SERDE = new Serde<PublicGameState>() {
        @Override
        public String serialize(PublicGameState p) {
            String[] serialized = new String[]{INTEGER_SERDE.serialize(p.ticketsCount()),
                    PUBLIC_CARDSTATE_SERDE.serialize(p.cardState()), PLAYER_ID_SERDE.serialize(p.currentPlayerId()),
                    PUBLIC_PLAYERSTATE_SERDE.serialize(p.playerState(PlayerId.PLAYER_1)),
                    PUBLIC_PLAYERSTATE_SERDE.serialize(p.playerState(PlayerId.PLAYER_2)),
                    p.lastPlayer() == null ? "" : PLAYER_ID_SERDE.serialize(p.lastPlayer())};
            return String.join(":", serialized);
        }

        @Override
        public PublicGameState deserialize(String str) {
            String[] split = str.split(Pattern.quote(";"),-1);
            Map<PlayerId, PublicPlayerState> playerState = new EnumMap<>(PlayerId.class);
            playerState.put(PlayerId.PLAYER_1, PLAYERSTATE_SERDE.deserialize(split[3]));
            playerState.put(PlayerId.PLAYER_1, PLAYERSTATE_SERDE.deserialize(split[4]));
            return new PublicGameState(INTEGER_SERDE.deserialize(split[0]),PUBLIC_CARDSTATE_SERDE.deserialize(split[1]),
                    PLAYER_ID_SERDE.deserialize(split[2]),playerState, PLAYER_ID_SERDE.deserialize(split[5]));
        }
    };
}
