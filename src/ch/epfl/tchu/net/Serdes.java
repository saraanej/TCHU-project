package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

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

    public static final Serde<PublicCardState>
}
