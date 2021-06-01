package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;

/**
 * The Serdes class, final and not instantiable, contains all the serdes useful for the project.
 * Each one of them is defined as a public, static, and final attribute of the class.
 *
 * @author Yasmin Ben Rahhal (329912)
 * @author Sara Anejjar (329905)
 */
public final class Serdes {

    private static final String COMMA_SEPARATOR = ",";
    private static final String SEMICOLON_SEPARATOR = ";";
    private static final String COLON_SEPARATOR = ":";
    private static final String EMPTY_STRING = "";

    /**
     * A Serde able to (de)serialize Integer values.
     */
    public static final Serde<Integer> INTEGER = Serde.of(
            i -> Integer.toString(i),
            Integer::parseInt);

    /**
     * A Serde able to (de)serialize String values.
     */
    public static final Serde<String> STRING = Serde.of(
            i -> Base64.getEncoder().encodeToString(i.getBytes(StandardCharsets.UTF_8)),
            s -> new String(Base64.getDecoder().decode(s), StandardCharsets.UTF_8));

    /**
     * A Serde able to (de)serialize Player_Id's elements.
     */
    public static final Serde<PlayerId> PLAYER_ID = Serde.oneOf(PlayerId.all());

    /**
     * A Serde able to (de)serialize TurnKind's elements.
     */
    public static final Serde<Player.TurnKind> TURN_KIND = Serde.oneOf(Player.TurnKind.ALL);

    /**
     * A Serde able to (de)serialize Card's elements.
     */
    public static final Serde<Card> CARD = Serde.oneOf(Card.ALL);

    /**
     * A Serde able to (de)serialize Route's elements.
     */
    public static final Serde<Route> ROUTE = Serde.oneOf(ChMap.routes());

    /**
     * A Serde able to (de)serialize Ticket's elements.
     */
    public static final Serde<Ticket> TICKET = Serde.oneOf(ChMap.tickets());

    /**
     * A Serde able to (de)serialize a list of String's elements.
     */
    public static final Serde<List<String>> LIST_STRING = Serde.listOf(STRING, COMMA_SEPARATOR);

    /**
     * A Serde able to (de)serialize a list of Card's elements.
     */
    public static final Serde<List<Card>> LIST_CARD = Serde.listOf(CARD, COMMA_SEPARATOR);

    /**
     * A Serde able to (de)serialize a list of Route's elements.
     */
    public static final Serde<List<Route>> LIST_ROUTE = Serde.listOf(ROUTE, COMMA_SEPARATOR);

    /**
     * A Serde able to (de)serialize a SortedBag of Card's elements.
     */
    public static final Serde<SortedBag<Card>> SORTED_CARD = Serde.bagOf(CARD, COMMA_SEPARATOR);

    /**
     * Serde able to (de)serialize a SortedBag of Ticket's elements.
     */
    public static final Serde<SortedBag<Ticket>> SORTED_TICKET = Serde.bagOf(TICKET, COMMA_SEPARATOR);

    /**
     * A Serde able to (de)serialize a list of SortedBags of Card's elements.
     */
    public static final Serde<List<SortedBag<Card>>> LIST_SORTED_CARD = Serde.listOf(SORTED_CARD, SEMICOLON_SEPARATOR);

    /**
     * A Serde able to (de)serialize PublicCardState's elements.
     */
    public static final Serde<PublicCardState> PUBLIC_CARDSTATE = new Serde<>() {
        @Override
        public String serialize(PublicCardState p) {
            String[] serialized = new String[]{LIST_CARD.serialize(p.faceUpCards()),
                    INTEGER.serialize(p.deckSize()), INTEGER.serialize(p.discardsSize())};
            return String.join(SEMICOLON_SEPARATOR, serialized);
        }

        @Override
        public PublicCardState deserialize(String str) {
            String[] split = str.split(Pattern.quote(SEMICOLON_SEPARATOR), -1);
            return new PublicCardState(LIST_CARD.deserialize(split[0]),
                    INTEGER.deserialize(split[1]), INTEGER.deserialize(split[2]));
        }
    };

    /**
     * A Serde able to (de)serialize PublicPlayerState's elements.
     */
    public static final Serde<PublicPlayerState> PUBLIC_PLAYERSTATE = new Serde<>() {
        @Override
        public String serialize(PublicPlayerState p) {
            String[] serialized = new String[]{INTEGER.serialize(p.ticketCount()),
                    INTEGER.serialize(p.cardCount()), LIST_ROUTE.serialize(p.routes())};
            return String.join(SEMICOLON_SEPARATOR, serialized);
        }

        @Override
        public PublicPlayerState deserialize(String str) {
            String[] split = str.split(Pattern.quote(SEMICOLON_SEPARATOR), -1);
            return new PublicPlayerState(INTEGER.deserialize(split[0]), INTEGER.deserialize(split[1]),
                    LIST_ROUTE.deserialize(split[2]));
        }
    };

    /**
     * A Serde able to (de)serialize PlayerState's elements.
     */
    public static final Serde<PlayerState> PLAYERSTATE = new Serde<>() {
        @Override
        public String serialize(PlayerState p) {
            String[] serialized = new String[]{SORTED_TICKET.serialize(p.tickets()),
                    SORTED_CARD.serialize(p.cards()), LIST_ROUTE.serialize(p.routes())};
            return String.join(SEMICOLON_SEPARATOR, serialized);
        }

        @Override
        public PlayerState deserialize(String str) {
            String[] split = str.split(Pattern.quote(SEMICOLON_SEPARATOR), -1);
            return new PlayerState(SORTED_TICKET.deserialize(split[0]),
                    SORTED_CARD.deserialize(split[1]),
                    LIST_ROUTE.deserialize(split[2]));
        }
    };

    /**
     * A Serde able to (de)serialize Trail's elements.
     */
    public static final Serde<Trail> TRAIL = new Serde<>() {
        @Override
        public String serialize(Trail trail) {
            List<String> serialized = new ArrayList<>();
            for(Route route : trail.getRoutes()){
                serialized.add(ROUTE.serialize(route));
            }
            return String.join(COMMA_SEPARATOR, serialized);
        }

        @Override
        public Trail deserialize(String str) {
            String[] split = str.split(Pattern.quote(COMMA_SEPARATOR), -1);
            List<Route> routes = new ArrayList<>();
            for(String route : split){
                routes.add(ROUTE.deserialize(route));
            }
            return Trail.longest(routes);
        }
    };

    /**
     * A Serde able to (de)serialize Map<PlayerId, Integer> elements.
     */
    public static final Serde<Map<PlayerId, Integer>>  MAP_ID_INTEGER = new Serde<>() {
        @Override
        public String serialize(Map<PlayerId, Integer> playerIdIntegerMap) {
            List<String> serialized = new ArrayList<>();
            for (PlayerId id : PlayerId.all())
                serialized.add(INTEGER.serialize(playerIdIntegerMap.get(id)));
            return String.join(COMMA_SEPARATOR, serialized);
        }

        @Override
        public Map<PlayerId, Integer> deserialize(String str) {
            String[] split = str.split(Pattern.quote(COMMA_SEPARATOR), -1);
            Map<PlayerId,Integer> playerPoints = new EnumMap<>(PlayerId.class);
            for (PlayerId id : PlayerId.all())
                playerPoints.put(id, INTEGER.deserialize(split[id.ordinal()]));
            return playerPoints;
        }
    };

    /**
     * A Serde able to (de)serialize Map<PlayerId, Trail> elements.
     */
    public static final Serde<Map<PlayerId, Trail>>  MAP_ID_TRAIL = new Serde<>() {
        @Override
        public String serialize(Map<PlayerId, Trail> playerIdTrailMap) {
            List<String> serialized = new ArrayList<>();
            for (PlayerId id : PlayerId.all())
                serialized.add(TRAIL.serialize(playerIdTrailMap.get(id)));
            return String.join(COMMA_SEPARATOR, serialized);
        }

        @Override
        public Map<PlayerId, Trail> deserialize(String str) {
            String[] split = str.split(Pattern.quote(COMMA_SEPARATOR), -1);
            Map<PlayerId, Trail> playersTrails = new EnumMap<>(PlayerId.class);
            for (PlayerId id : PlayerId.all())
                playersTrails.put(id, TRAIL.deserialize(split[id.ordinal()]));
            return playersTrails;
        }
    };


    /**
     * A Serde able to (de)serialize PublicGameState's elements.
     */
    public final static Serde<PublicGameState> PUBLIC_GAMESTATE = new Serde<>() {
        @Override
        public String serialize(PublicGameState p) {

            List<String> serialized = new ArrayList<>();
            List<String> s1 = List.of(INTEGER.serialize(p.ticketsCount()),
                    PUBLIC_CARDSTATE.serialize(p.cardState()), PLAYER_ID.serialize(p.currentPlayerId()));

            serialized.addAll(s1);

            for (PlayerId id : PlayerId.all())
                serialized.add(PUBLIC_PLAYERSTATE.serialize(p.playerState(id)));

            serialized.add(p.lastPlayer() == null ? EMPTY_STRING : PLAYER_ID.serialize(p.lastPlayer()));

            return String.join(COLON_SEPARATOR, serialized);
        }

        @Override
        public PublicGameState deserialize(String str) {
            String[] split = str.split(Pattern.quote(COLON_SEPARATOR), -1);

            //create the map here with for each
            Map<PlayerId,PublicPlayerState> playerState = new EnumMap<>(PlayerId.class);
            for (PlayerId id : PlayerId.all())
                playerState.put(id, PUBLIC_PLAYERSTATE.deserialize(split[3 + id.ordinal()]));

            int lastIndex = split.length-1;
            return new PublicGameState(INTEGER.deserialize(split[0]), PUBLIC_CARDSTATE.deserialize(split[1]),
                    PLAYER_ID.deserialize(split[2]), playerState,
                    split[lastIndex].isEmpty() ? null : PLAYER_ID.deserialize(split[lastIndex]));
        }
    };


    /**
     * Private default constructor, should not be used.
     *
     * @throws UnsupportedOperationException if called.
     */
    private Serdes() {
        throw new UnsupportedOperationException();
    }
}
