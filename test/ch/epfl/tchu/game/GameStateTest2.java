/*
 *	Author:      Malak Lahlou Nabil
 *	Date:        26 mars 2021
 */

package ch.epfl.tchu.game;

import java.util.*;

import org.junit.jupiter.api.Test;

import ch.epfl.tchu.SortedBag;

import static org.junit.jupiter.api.Assertions.*;

public class GameStateTest2 {
    // Stations - cities
    private static final Station BAD = new Station(0, "Baden");
    private static final Station BAL = new Station(1, "Bâle");
    private static final Station BEL = new Station(2, "Bellinzone");
    private static final Station BER = new Station(3, "Berne");
    private static final Station BRI = new Station(4, "Brigue");
    private static final Station BRU = new Station(5, "Brusio");
    private static final Station COI = new Station(6, "Coire");
    private static final Station DAV = new Station(7, "Davos");
    private static final Station DEL = new Station(8, "Delémont");
    private static final Station FRI = new Station(9, "Fribourg");
    private static final Station GEN = new Station(10, "Genève");
    private static final Station INT = new Station(11, "Interlaken");
    private static final Station KRE = new Station(12, "Kreuzlingen");
    private static final Station LAU = new Station(13, "Lausanne");
    private static final Station LCF = new Station(14, "La Chaux-de-Fonds");
    private static final Station LOC = new Station(15, "Locarno");
    private static final Station LUC = new Station(16, "Lucerne");
    private static final Station LUG = new Station(17, "Lugano");
    private static final Station MAR = new Station(18, "Martigny");
    private static final Station NEU = new Station(19, "Neuchâtel");
    private static final Station OLT = new Station(20, "Olten");
    private static final Station PFA = new Station(21, "Pfäffikon");
    private static final Station SAR = new Station(22, "Sargans");
    private static final Station SCE = new Station(23, "Schaffhouse");
    private static final Station SCZ = new Station(24, "Schwyz");
    private static final Station SIO = new Station(25, "Sion");
    private static final Station SOL = new Station(26, "Soleure");
    private static final Station STG = new Station(27, "Saint-Gall");
    private static final Station VAD = new Station(28, "Vaduz");
    private static final Station WAS = new Station(29, "Wassen");
    private static final Station WIN = new Station(30, "Winterthour");
    private static final Station YVE = new Station(31, "Yverdon");
    private static final Station ZOU = new Station(32, "Zoug");
    private static final Station ZUR = new Station(33, "Zürich");

    // Stations - countries
    private static final Station DE1 = new Station(34, "Allemagne");
    private static final Station DE2 = new Station(35, "Allemagne");
    private static final Station DE3 = new Station(36, "Allemagne");
    private static final Station DE4 = new Station(37, "Allemagne");
    private static final Station DE5 = new Station(38, "Allemagne");
    private static final Station AT1 = new Station(39, "Autriche");
    private static final Station AT2 = new Station(40, "Autriche");
    private static final Station AT3 = new Station(41, "Autriche");
    private static final Station IT1 = new Station(42, "Italie");
    private static final Station IT2 = new Station(43, "Italie");
    private static final Station IT3 = new Station(44, "Italie");
    private static final Station IT4 = new Station(45, "Italie");
    private static final Station IT5 = new Station(46, "Italie");
    private static final Station FR1 = new Station(47, "France");
    private static final Station FR2 = new Station(48, "France");
    private static final Station FR3 = new Station(49, "France");
    private static final Station FR4 = new Station(50, "France");

    // Countries
    private static final List<Station> DE = List.of(DE1, DE2, DE3, DE4, DE5);
    private static final List<Station> AT = List.of(AT1, AT2, AT3);
    private static final List<Station> IT = List.of(IT1, IT2, IT3, IT4, IT5);
    private static final List<Station> FR = List.of(FR1, FR2, FR3, FR4);

    public static final Random NOM_RANDOM = new Random() {
        @Override
        public int nextInt(int i) {
            return i - 1;
        }
    };
/*
    @Test
    void constructionMethodInitialWorks() {

        SortedBag<Ticket> tickets = SortedBag
                .of(List.of(ChMap.tickets().get(0), ChMap.tickets().get(1),
                        ChMap.tickets().get(2), ChMap.tickets().get(3)));
        GameState gs = GameState.initial(tickets, NOM_RANDOM);

        assertEquals(tickets.size(), gs.ticketsCount());

        assertEquals(97, gs.cardState().deckSize());

        assertEquals(PlayerId.PLAYER_2, gs.currentPlayerId());

        assertEquals(4, gs.currentPlayerState().cards().size());

        assertEquals(4,
                gs.playerState(gs.currentPlayerId().next()).cards().size());

    }

    @Test
    public void constructorWorks() {

        SortedBag<Ticket> tickets = SortedBag.of(2, ChMap.tickets().get(0), 3,
                ChMap.tickets().get(1));

        Deck<Ticket> ticketDeck = Deck.of(tickets, NOM_RANDOM);

        SortedBag.Builder<Card> cards = new SortedBag.Builder<>();

        cards.add(Card.RED);
        cards.add(Card.RED);
        cards.add(Card.RED);
        cards.add(Card.BLUE);
        cards.add(Card.BLUE);
        cards.add(Card.BLACK);
        cards.add(Card.LOCOMOTIVE);
        cards.add(Card.GREEN);
        cards.add(Card.ORANGE);
        cards.add(Card.RED);
        cards.add(Card.WHITE);
        cards.add(Card.WHITE);
        cards.add(Card.BLACK);
        cards.add(Card.GREEN);

        SortedBag<Card> card = cards.build();

        Deck<Card> cardDeck = Deck.of(card, NOM_RANDOM);

        Map<PlayerId, PlayerState> playerState = new EnumMap<>(PlayerId.class);

        for (PlayerId id : PlayerId.ALL) {
            playerState.put(id, new PlayerState(SortedBag.of(),
                    cardDeck.topCards(4), List.of()));
            cardDeck = cardDeck.withoutTopCards(4);
        }

        GameState gameState = new GameState(ticketDeck, CardState.of(cardDeck),
                PlayerId.PLAYER_1, playerState, PlayerId.PLAYER_2);

        assertEquals(6, cardDeck.size());
        assertEquals(2, playerState.size());
        assertEquals(5, ticketDeck.size());
        assertEquals(4,
                gameState.playerState(PlayerId.PLAYER_1).cards().size());

        assertEquals(
                SortedBag.of(
                        List.of(Card.GREEN, Card.GREEN, Card.ORANGE, Card.RED)),
                gameState.playerState(PlayerId.PLAYER_2).cards());

    }

    @Test
    public void PlayerStateWorks() {

        SortedBag<Ticket> tickets = SortedBag.of(2, ChMap.tickets().get(0), 3,
                ChMap.tickets().get(1));

        Deck<Ticket> ticketDeck = Deck.of(tickets, NOM_RANDOM);

        SortedBag.Builder<Card> cards = new SortedBag.Builder<>();

        cards.add(Card.RED);
        cards.add(Card.RED);
        cards.add(Card.RED);
        cards.add(Card.BLUE);
        cards.add(Card.BLUE);
        cards.add(Card.BLACK);
        cards.add(Card.LOCOMOTIVE);
        cards.add(Card.GREEN);
        cards.add(Card.ORANGE);
        cards.add(Card.RED);
        cards.add(Card.WHITE);
        cards.add(Card.WHITE);
        cards.add(Card.BLACK);
        cards.add(Card.GREEN);

        SortedBag<Card> card = cards.build();

        Deck<Card> cardDeck = Deck.of(card, NOM_RANDOM);

        Map<PlayerId, PlayerState> playerState = new EnumMap<>(PlayerId.class);

        for (PlayerId id : PlayerId.ALL) {
            playerState.put(id, new PlayerState(SortedBag.of(),
                    cardDeck.topCards(4), List.of()));
            cardDeck = cardDeck.withoutTopCards(4);
        }

        GameState gameState = new GameState(ticketDeck, CardState.of(cardDeck),
                PlayerId.PLAYER_1, playerState, PlayerId.PLAYER_2);

        assertEquals(4,
                gameState.playerState(PlayerId.PLAYER_1).cards().size());

        assertEquals(
                SortedBag.of(
                        List.of(Card.GREEN, Card.GREEN, Card.ORANGE, Card.RED)),
                gameState.playerState(PlayerId.PLAYER_2).cards());

    }

    @Test
    void topTicketsThrows() {
        SortedBag<Ticket> tickets = SortedBag
                .of(List.of(ChMap.tickets().get(0), ChMap.tickets().get(1),
                        ChMap.tickets().get(2), ChMap.tickets().get(3)));
        GameState gs = GameState.initial(tickets, NOM_RANDOM);

        assertThrows(IllegalArgumentException.class, () -> {
            gs.topTickets(5);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            gs.topTickets(-1);
        });

    }

    @Test
    void topTicketsWorks() {
        SortedBag<Ticket> tickets = SortedBag
                .of(List.of(ChMap.tickets().get(0), ChMap.tickets().get(1),
                        ChMap.tickets().get(2), ChMap.tickets().get(3)));
        SortedBag<Ticket> tickets2 = SortedBag
                .of(List.of(ChMap.tickets().get(0), ChMap.tickets().get(3)));
        GameState gs = GameState.initial(tickets, NOM_RANDOM);

        assertEquals(2, gs.topTickets(2).size());

        assertEquals(tickets2, gs.topTickets(2));

    }

    @Test
    void withoutTopTicketsThrows() {
        SortedBag<Ticket> tickets = SortedBag
                .of(List.of(ChMap.tickets().get(0), ChMap.tickets().get(1),
                        ChMap.tickets().get(2), ChMap.tickets().get(3)));
        SortedBag<Ticket> tickets2 = SortedBag
                .of(List.of(ChMap.tickets().get(0), ChMap.tickets().get(3)));
        GameState gs = GameState.initial(tickets, NOM_RANDOM);

        assertThrows(IllegalArgumentException.class, () -> {
            gs.topTickets(5);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            gs.topTickets(-1);
        });

    }

    @Test
    void withoutTopTicketsWorks() {
        SortedBag<Ticket> tickets = SortedBag
                .of(List.of(ChMap.tickets().get(0), ChMap.tickets().get(1),
                        ChMap.tickets().get(2), ChMap.tickets().get(3)));
        List<Ticket> tickets2 = tickets.toList().subList(2, 4);
        GameState gs = GameState.initial(tickets, NOM_RANDOM);

        assertEquals(4, gs.withoutTopTickets(0).gameDeck.size());
        assertEquals(tickets, SortedBag
                .of(gs.withoutTopTickets(0).gameDeck.cards));

        assertEquals(2, gs.withoutTopTickets(2).gameDeck.size());
        assertEquals(SortedBag.of(tickets2), SortedBag
                .of(gs.withoutTopTickets(2).gameDeck.cards));
    }

    @Test
    void topCardThrows() {
        SortedBag<Ticket> tickets = SortedBag.of(2, ChMap.tickets().get(0), 3,
                ChMap.tickets().get(0));
        Deck<Ticket> ticketDeck = Deck.of(tickets, NOM_RANDOM);
        SortedBag.Builder<Card> cards = new SortedBag.Builder<>();
        cards.add(Card.RED);
        cards.add(Card.RED);
        cards.add(Card.RED);
        cards.add(Card.BLUE);
        cards.add(Card.BLUE);
        cards.add(Card.BLACK);
        cards.add(Card.LOCOMOTIVE);
        cards.add(Card.GREEN);
        cards.add(Card.ORANGE);
        cards.add(Card.RED);
        cards.add(Card.WHITE);
        cards.add(Card.WHITE);
        cards.add(Card.BLACK);
        cards.add(Card.GREEN);
        SortedBag<Card> card = cards.build();
        Deck<Card> cardDeck = Deck.of(card, NOM_RANDOM);
        Map<PlayerId, PlayerState> playerState = new EnumMap<>(PlayerId.class);

        for (PlayerId id : PlayerId.ALL) {
            playerState.put(id, new PlayerState(SortedBag.of(),
                    cardDeck.topCards(4), List.of()));
            cardDeck = cardDeck.withoutTopCards(4);
        }
        GameState gameState = new GameState(ticketDeck, CardState.of(cardDeck),
                PlayerId.PLAYER_1, playerState, PlayerId.PLAYER_2);

        GameState gameState2 = gameState.withoutTopCard();

        assertThrows(IllegalArgumentException.class, () -> {
            gameState2.topCard();
        });

    }

    @Test
    void topCardWorks() {
        SortedBag<Ticket> tickets = SortedBag.of(2, ChMap.tickets().get(0), 3,
                ChMap.tickets().get(0));
        Deck<Ticket> ticketDeck = Deck.of(tickets, NOM_RANDOM);
        SortedBag.Builder<Card> cards = new SortedBag.Builder<>();
        cards.add(Card.RED);
        cards.add(Card.RED);
        cards.add(Card.RED);
        cards.add(Card.BLUE);
        cards.add(Card.BLUE);
        cards.add(Card.BLACK);
        cards.add(Card.LOCOMOTIVE);
        cards.add(Card.GREEN);
        cards.add(Card.ORANGE);
        cards.add(Card.RED);
        cards.add(Card.WHITE);
        cards.add(Card.WHITE);
        cards.add(Card.BLACK);
        cards.add(Card.GREEN);
        SortedBag<Card> card = cards.build();
        Deck<Card> cardDeck = Deck.of(card, NOM_RANDOM);
        Map<PlayerId, PlayerState> playerState = new EnumMap<>(PlayerId.class);

        for (PlayerId id : PlayerId.ALL) {
            playerState.put(id, new PlayerState(SortedBag.of(),
                    cardDeck.topCards(4), List.of()));
            cardDeck = cardDeck.withoutTopCards(4);
        }
        GameState gameState = new GameState(ticketDeck, CardState.of(cardDeck),
                PlayerId.PLAYER_1, playerState, PlayerId.PLAYER_2);

        assertEquals(Card.LOCOMOTIVE, gameState.topCard());

    }

    @Test
    void withoutTopCardThrows() {
        SortedBag<Ticket> tickets = SortedBag.of(2, ChMap.tickets().get(0), 3,
                ChMap.tickets().get(0));
        Deck<Ticket> ticketDeck = Deck.of(tickets, NOM_RANDOM);
        SortedBag.Builder<Card> cards = new SortedBag.Builder<>();
        cards.add(Card.RED);
        cards.add(Card.RED);
        cards.add(Card.RED);
        cards.add(Card.BLUE);
        cards.add(Card.BLUE);
        cards.add(Card.BLACK);
        cards.add(Card.LOCOMOTIVE);
        cards.add(Card.GREEN);
        cards.add(Card.ORANGE);
        cards.add(Card.RED);
        cards.add(Card.WHITE);
        cards.add(Card.WHITE);
        cards.add(Card.BLACK);
        cards.add(Card.GREEN);
        SortedBag<Card> card = cards.build();
        Deck<Card> cardDeck = Deck.of(card, NOM_RANDOM);
        Map<PlayerId, PlayerState> playerState = new EnumMap<>(PlayerId.class);

        for (PlayerId id : PlayerId.ALL) {
            playerState.put(id, new PlayerState(SortedBag.of(),
                    cardDeck.topCards(4), List.of()));
            cardDeck = cardDeck.withoutTopCards(4);
        }
        GameState gameState = new GameState(ticketDeck, CardState.of(cardDeck),
                PlayerId.PLAYER_1, playerState, PlayerId.PLAYER_2);

        GameState gameState2 = gameState.withoutTopCard();

        assertThrows(IllegalArgumentException.class, () -> {
            gameState2.withoutTopCard();
        });

    }

    @Test
    void withoutTopCardWorks() {
        SortedBag<Ticket> tickets = SortedBag.of(2, ChMap.tickets().get(0), 3,
                ChMap.tickets().get(0));
        Deck<Ticket> ticketDeck = Deck.of(tickets, NOM_RANDOM);
        SortedBag.Builder<Card> cards = new SortedBag.Builder<>();
        cards.add(Card.RED);
        cards.add(Card.RED);
        cards.add(Card.RED);
        cards.add(Card.BLUE);
        cards.add(Card.BLUE);
        cards.add(Card.BLACK);
        cards.add(Card.LOCOMOTIVE);
        cards.add(Card.GREEN);
        cards.add(Card.ORANGE);
        cards.add(Card.RED);
        cards.add(Card.WHITE);
        cards.add(Card.WHITE);
        cards.add(Card.BLACK);
        cards.add(Card.GREEN);
        cards.add(Card.BLACK);
        cards.add(Card.GREEN);
        SortedBag<Card> card = cards.build();
        Deck<Card> cardDeck = Deck.of(card, NOM_RANDOM);
        Map<PlayerId, PlayerState> playerState = new EnumMap<>(PlayerId.class);

        for (PlayerId id : PlayerId.ALL) {
            playerState.put(id, new PlayerState(SortedBag.of(),
                    cardDeck.topCards(4), List.of()));
            cardDeck = cardDeck.withoutTopCards(4);
        }
        GameState gameState = new GameState(ticketDeck, CardState.of(cardDeck),
                PlayerId.PLAYER_1, playerState, PlayerId.PLAYER_2);

        assertEquals(2, gameState.withoutTopCard().cardState().deckSize());

    }

    @Test
    void withMoreDiscardedCardsWorks() {
        SortedBag<Ticket> tickets = SortedBag.of(2, ChMap.tickets().get(0), 3,
                ChMap.tickets().get(0));
        Deck<Ticket> ticketDeck = Deck.of(tickets, NOM_RANDOM);
        Map<PlayerId, PlayerState> playerState = new EnumMap<>(PlayerId.class);

        SortedBag.Builder<Card> cards = new SortedBag.Builder<>();
        cards.add(Card.RED);
        cards.add(Card.RED);
        cards.add(Card.RED);
        cards.add(Card.BLUE);
        cards.add(Card.BLUE);
        cards.add(Card.BLACK);
        cards.add(Card.LOCOMOTIVE);
        cards.add(Card.GREEN);
        cards.add(Card.ORANGE);
        cards.add(Card.RED);
        cards.add(Card.WHITE);
        cards.add(Card.WHITE);
        cards.add(Card.BLACK);
        cards.add(Card.GREEN);
        cards.add(Card.BLACK);
        cards.add(Card.GREEN);
        SortedBag<Card> card = cards.build();

        Deck<Card> cards2 = Deck.of(cards.build(), NOM_RANDOM);
        Deck<Card> cardsexpected = Deck.of(cards.build(), NOM_RANDOM);

        for (PlayerId id : PlayerId.ALL) {
            playerState.put(id, new PlayerState(SortedBag.of(),
                    cards2.topCards(4), List.of()));
            cards2 = cards2.withoutTopCards(4);
        }

        for (PlayerId id : PlayerId.ALL) {
            cardsexpected = cardsexpected.withoutTopCards(4);
        }

        CardState cardState = CardState.of(cardsexpected);
        cardState = cardState.withMoreDiscardedCards(
                SortedBag.of(1, Card.BLACK, 2, Card.GREEN));

        GameState gameState = new GameState(ticketDeck, CardState.of(cards2),
                PlayerId.PLAYER_1, playerState, PlayerId.PLAYER_2);

        assertEquals(3,
                gameState
                        .withMoreDiscardedCards(
                                SortedBag.of(1, Card.BLACK, 2, Card.GREEN))
                        .cardState().discardsSize());

        assertEquals(SortedBag.of(cardState.deck.cards),
                SortedBag.of(
                        gameState.cardState.deck.cards));

    }

    @Test
    void withCardsDeckRecreatedIfNeededWorksWithNonEmptyDeck() {
        SortedBag<Ticket> tickets = SortedBag.of(2, ChMap.tickets().get(0), 3,
                ChMap.tickets().get(0));
        Deck<Ticket> ticketDeck = Deck.of(tickets, NOM_RANDOM);
        Map<PlayerId, PlayerState> playerState = new EnumMap<>(PlayerId.class);

        SortedBag.Builder<Card> cards = new SortedBag.Builder<>();
        cards.add(Card.RED);
        cards.add(Card.RED);
        cards.add(Card.RED);
        cards.add(Card.BLUE);
        cards.add(Card.BLUE);
        cards.add(Card.BLACK);
        cards.add(Card.LOCOMOTIVE);
        cards.add(Card.GREEN);
        cards.add(Card.ORANGE);
        cards.add(Card.RED);
        cards.add(Card.WHITE);
        cards.add(Card.WHITE);
        cards.add(Card.BLACK);
        cards.add(Card.GREEN);
        SortedBag<Card> card = cards.build();

        Deck<Card> cards2 = Deck.of(cards.build(), NOM_RANDOM);
        Deck<Card> cardsexpected = Deck.of(cards.build(), NOM_RANDOM);

        for (PlayerId id : PlayerId.ALL) {
            playerState.put(id, new PlayerState(SortedBag.of(),
                    cards2.topCards(4), List.of()));
            cards2 = cards2.withoutTopCards(4);
        }

        for (PlayerId id : PlayerId.ALL) {
            cardsexpected = cardsexpected.withoutTopCards(4);
        }

        CardState cardState = CardState.of(cardsexpected);
        cardState = cardState.withMoreDiscardedCards(
                SortedBag.of(1, Card.BLACK, 2, Card.GREEN));

        GameState gameState = new GameState(ticketDeck, CardState.of(cards2),
                PlayerId.PLAYER_1, playerState, PlayerId.PLAYER_2);

        assertEquals(gameState,
                gameState.withCardsDeckRecreatedIfNeeded(NOM_RANDOM));
    }

    @Test
    void withCardsDeckRecreatedIfNeededWorksWithEmptyDeck() {
        SortedBag<Ticket> tickets = SortedBag.of(2, ChMap.tickets().get(0), 3,
                ChMap.tickets().get(0));
        Deck<Ticket> ticketDeck = Deck.of(tickets, NOM_RANDOM);
        Map<PlayerId, PlayerState> playerState = new EnumMap<>(PlayerId.class);

        SortedBag.Builder<Card> cards = new SortedBag.Builder<>();
        cards.add(Card.RED);
        cards.add(Card.RED);
        cards.add(Card.RED);
        cards.add(Card.BLUE);
        cards.add(Card.BLUE);
        cards.add(Card.BLACK);
        cards.add(Card.LOCOMOTIVE);
        cards.add(Card.GREEN);
        cards.add(Card.ORANGE);
        cards.add(Card.RED);
        cards.add(Card.WHITE);
        cards.add(Card.WHITE);
        cards.add(Card.BLACK);
        cards.add(Card.GREEN);
        SortedBag<Card> card = cards.build();

        Deck<Card> cards2 = Deck.of(cards.build(), NOM_RANDOM);
        Deck<Card> cardsexpected = Deck.of(cards.build(), NOM_RANDOM);

        for (PlayerId id : PlayerId.ALL) {
            playerState.put(id, new PlayerState(SortedBag.of(),
                    cards2.topCards(4), List.of()));
            cards2 = cards2.withoutTopCards(4);
        }

        for (PlayerId id : PlayerId.ALL) {
            cardsexpected = cardsexpected.withoutTopCards(4);
        }

        CardState cardState = CardState.of(cardsexpected);
        cardState = cardState.withMoreDiscardedCards(
                SortedBag.of(1, Card.BLACK, 2, Card.GREEN));

        GameState gameState = new GameState(ticketDeck, CardState.of(cards2),
                PlayerId.PLAYER_1, playerState, PlayerId.PLAYER_2);

        while (!gameState.cardState().isDeckEmpty()) {
            gameState = gameState.withoutTopCard();
        }

        SortedBag<Card> sb = SortedBag.of(List.of(Card.BLACK, Card.LOCOMOTIVE,
                Card.GREEN, Card.BLUE, Card.LOCOMOTIVE, Card.ORANGE));
        gameState = gameState.withMoreDiscardedCards(sb);

        assertTrue(gameState.cardState().isDeckEmpty());
        assertEquals(sb.size(),
                gameState.withCardsDeckRecreatedIfNeeded(NOM_RANDOM)
                        .cardState.deckSize());
        assertEquals(sb,
                SortedBag
                        .of(gameState.withCardsDeckRecreatedIfNeeded(NOM_RANDOM)
                                .cardState.deck.cards));

    }

    @Test
    void withInitiallyChosenTicketsThrows() {
        SortedBag<Ticket> tickets = SortedBag.of(2, ChMap.tickets().get(0), 3,
                ChMap.tickets().get(1));
        Deck<Ticket> ticketDeck = Deck.of(tickets, NOM_RANDOM);
        SortedBag.Builder<Card> cards = new SortedBag.Builder<>();
        cards.add(Card.RED);
        cards.add(Card.RED);
        cards.add(Card.RED);
        cards.add(Card.BLUE);
        cards.add(Card.BLUE);
        cards.add(Card.BLACK);
        cards.add(Card.LOCOMOTIVE);
        cards.add(Card.GREEN);
        cards.add(Card.ORANGE);
        cards.add(Card.RED);
        cards.add(Card.WHITE);
        cards.add(Card.WHITE);
        cards.add(Card.BLACK);
        cards.add(Card.GREEN);
        SortedBag<Card> card = cards.build();
        Deck<Card> cardDeck = Deck.of(card, NOM_RANDOM);
        Map<PlayerId, PlayerState> playerState = new EnumMap<>(PlayerId.class);

        for (PlayerId id : PlayerId.ALL) {
            playerState.put(id, new PlayerState(SortedBag.of(),
                    cardDeck.topCards(4), List.of()));
            cardDeck = cardDeck.withoutTopCards(4);
        }

        playerState.replace(PlayerId.PLAYER_1,
                new PlayerState(SortedBag.of(ChMap.tickets().get(2)),
                        cardDeck.topCards(4), List.of()));

        GameState gameState = new GameState(ticketDeck, CardState.of(cardDeck),
                PlayerId.PLAYER_1, playerState, PlayerId.PLAYER_2);

        assertThrows(IllegalArgumentException.class, () -> {
            gameState.withInitiallyChosenTickets(PlayerId.PLAYER_1, tickets)
            .currentPlayerState().tickets();
        });
  

    }

    @Test
    void withInitiallyChosenTicketsWorks() {
        SortedBag<Ticket> tickets = SortedBag.of(2, ChMap.tickets().get(0), 3,
                ChMap.tickets().get(1));
        Deck<Ticket> ticketDeck = Deck.of(tickets, NOM_RANDOM);
        SortedBag.Builder<Card> cards = new SortedBag.Builder<>();
        cards.add(Card.RED);
        cards.add(Card.RED);
        cards.add(Card.RED);
        cards.add(Card.BLUE);
        cards.add(Card.BLUE);
        cards.add(Card.BLACK);
        cards.add(Card.LOCOMOTIVE);
        cards.add(Card.GREEN);
        cards.add(Card.ORANGE);
        cards.add(Card.RED);
        cards.add(Card.WHITE);
        cards.add(Card.WHITE);
        cards.add(Card.BLACK);
        cards.add(Card.GREEN);
        SortedBag<Card> card = cards.build();
        Deck<Card> cardDeck = Deck.of(card, NOM_RANDOM);
        Map<PlayerId, PlayerState> playerState = new EnumMap<>(PlayerId.class);

        for (PlayerId id : PlayerId.ALL) {
            playerState.put(id, new PlayerState(SortedBag.of(),
                    cardDeck.topCards(4), List.of()));
            cardDeck = cardDeck.withoutTopCards(4);
        }

        GameState gameState = new GameState(ticketDeck, CardState.of(cardDeck),
                PlayerId.PLAYER_1, playerState, PlayerId.PLAYER_2);

        assertEquals(tickets,
                gameState.withInitiallyChosenTickets(PlayerId.PLAYER_1, tickets)
                        .currentPlayerState().tickets());

    }


    @Test
    public void lastTurnWorks(){

        SortedBag<Ticket> tickets = SortedBag.of(2, ChMap.tickets().get(0), 3,
                ChMap.tickets().get(1));

        Deck<Ticket> ticketDeck = Deck.of(tickets, NOM_RANDOM);

        SortedBag.Builder<Card> cards = new SortedBag.Builder<>();

        cards.add(Card.RED);
        cards.add(Card.RED);
        cards.add(Card.RED);
        cards.add(Card.BLUE);
        cards.add(Card.BLUE);
        cards.add(Card.BLACK);
        cards.add(Card.LOCOMOTIVE);
        cards.add(Card.GREEN);
        cards.add(Card.ORANGE);
        cards.add(Card.RED);
        cards.add(Card.WHITE);
        cards.add(Card.WHITE);
        cards.add(Card.BLACK);
        cards.add(Card.GREEN);

        SortedBag<Card> card = cards.build();

        Deck<Card> cardDeck = Deck.of(card, NOM_RANDOM);

        Map<PlayerId, PlayerState> playerState = new EnumMap<>(PlayerId.class);

        List<Route> routes = List.of(  new Route("AT1_STG_1", AT1, STG, 4, Route.Level.UNDERGROUND, null),
                new Route("AT2_VAD_1", AT2, VAD, 1, Route.Level.UNDERGROUND, Color.RED),
                new Route("BAD_BAL_1", BAD, BAL, 3, Route.Level.UNDERGROUND, Color.RED),
                new Route("BAD_OLT_1", BAD, OLT, 2, Route.Level.OVERGROUND, Color.VIOLET),
                new Route("BAD_ZUR_1", BAD, ZUR, 1, Route.Level.OVERGROUND, Color.YELLOW),
                new Route("BAL_DE1_1", BAL, DE1, 1, Route.Level.UNDERGROUND, Color.BLUE),
                new Route("BAL_DEL_1", BAL, DEL, 2, Route.Level.UNDERGROUND, Color.YELLOW),
                new Route("BAL_OLT_1", BAL, OLT, 2, Route.Level.UNDERGROUND, Color.ORANGE),
                new Route("BEL_LOC_1", BEL, LOC, 1, Route.Level.UNDERGROUND, Color.BLACK),
                new Route("BEL_LUG_1", BEL, LUG, 1, Route.Level.UNDERGROUND, Color.RED),
                new Route("BEL_LUG_2", BEL, LUG, 1, Route.Level.UNDERGROUND, Color.YELLOW),
                new Route("BEL_WAS_1", BEL, WAS, 4, Route.Level.UNDERGROUND, null),
                new Route("BEL_WAS_2", BEL, WAS, 4, Route.Level.UNDERGROUND, null),
                new Route("BER_FRI_1", BER, FRI, 1, Route.Level.OVERGROUND, Color.ORANGE),
                new Route("BER_FRI_2", BER, FRI, 1, Route.Level.OVERGROUND, Color.YELLOW),
                new Route("BER_INT_1", BER, INT, 3, Route.Level.OVERGROUND, Color.BLUE),
                new Route("BER_LUC_1", BER, LUC, 4, Route.Level.OVERGROUND, null),
                new Route("COI_SAR_1", COI, SAR, 1, Route.Level.UNDERGROUND, Color.WHITE),
                new Route("COI_SAR_1", COI, SAR, 1, Route.Level.UNDERGROUND, Color.WHITE),
              //  new Route("LUC_ZOU_2", LUC, ZOU, 1, Route.Level.OVERGROUND, Color.YELLOW)
                new Route("MAR_SIO_1", MAR, SIO, 2, Route.Level.UNDERGROUND, Color.GREEN)
                );
        for (PlayerId id : PlayerId.ALL) {
            playerState.put(id, new PlayerState(SortedBag.of(),
                    cardDeck.topCards(4), routes));
            cardDeck = cardDeck.withoutTopCards(4);
        }

        GameState lastgs = new GameState(ticketDeck, CardState.of(cardDeck), PlayerId.PLAYER_1,playerState,PlayerId.PLAYER_2);
        assertFalse(lastgs.lastTurnBegins());
        GameState lastgsT = new GameState(ticketDeck, CardState.of(cardDeck), PlayerId.PLAYER_1,playerState,null);
        assertTrue(lastgsT.lastTurnBegins());

    }

    @Test
    public void forNextTurnWorks(){
        SortedBag<Ticket> tickets = SortedBag.of(2, ChMap.tickets().get(0), 3,
                ChMap.tickets().get(1));

        Deck<Ticket> ticketDeck = Deck.of(tickets, NOM_RANDOM);

        SortedBag.Builder<Card> cards = new SortedBag.Builder<>();

        cards.add(Card.RED);
        cards.add(Card.RED);
        cards.add(Card.RED);
        cards.add(Card.BLUE);
        cards.add(Card.BLUE);
        cards.add(Card.BLACK);
        cards.add(Card.LOCOMOTIVE);
        cards.add(Card.GREEN);
        cards.add(Card.ORANGE);
        cards.add(Card.RED);
        cards.add(Card.WHITE);
        cards.add(Card.WHITE);
        cards.add(Card.BLACK);
        cards.add(Card.GREEN);

        SortedBag<Card> card = cards.build();

        Deck<Card> cardDeck = Deck.of(card, NOM_RANDOM);

        Map<PlayerId, PlayerState> playerState = new EnumMap<>(PlayerId.class);

        List<Route> routes = List.of(  new Route("AT1_STG_1", AT1, STG, 4, Route.Level.UNDERGROUND, null),
                new Route("AT2_VAD_1", AT2, VAD, 1, Route.Level.UNDERGROUND, Color.RED),
                new Route("BAD_BAL_1", BAD, BAL, 3, Route.Level.UNDERGROUND, Color.RED),
                new Route("BAD_OLT_1", BAD, OLT, 2, Route.Level.OVERGROUND, Color.VIOLET),
                new Route("BAD_ZUR_1", BAD, ZUR, 1, Route.Level.OVERGROUND, Color.YELLOW),
                new Route("BAL_DE1_1", BAL, DE1, 1, Route.Level.UNDERGROUND, Color.BLUE),
                new Route("BAL_DEL_1", BAL, DEL, 2, Route.Level.UNDERGROUND, Color.YELLOW),
                new Route("BAL_OLT_1", BAL, OLT, 2, Route.Level.UNDERGROUND, Color.ORANGE),
                new Route("BEL_LOC_1", BEL, LOC, 1, Route.Level.UNDERGROUND, Color.BLACK),
                new Route("BEL_LUG_1", BEL, LUG, 1, Route.Level.UNDERGROUND, Color.RED),
                new Route("BEL_LUG_2", BEL, LUG, 1, Route.Level.UNDERGROUND, Color.YELLOW),
                new Route("BEL_WAS_1", BEL, WAS, 4, Route.Level.UNDERGROUND, null),
                new Route("BEL_WAS_2", BEL, WAS, 4, Route.Level.UNDERGROUND, null),
                new Route("BER_FRI_1", BER, FRI, 1, Route.Level.OVERGROUND, Color.ORANGE),
                new Route("BER_FRI_2", BER, FRI, 1, Route.Level.OVERGROUND, Color.YELLOW),
                new Route("BER_INT_1", BER, INT, 3, Route.Level.OVERGROUND, Color.BLUE),
                new Route("BER_LUC_1", BER, LUC, 4, Route.Level.OVERGROUND, null),
                new Route("COI_SAR_1", COI, SAR, 1, Route.Level.UNDERGROUND, Color.WHITE),
                new Route("COI_SAR_1", COI, SAR, 1, Route.Level.UNDERGROUND, Color.WHITE),
                //  new Route("LUC_ZOU_2", LUC, ZOU, 1, Route.Level.OVERGROUND, Color.YELLOW)
                new Route("MAR_SIO_1", MAR, SIO, 2, Route.Level.UNDERGROUND, Color.GREEN)
        );
        for (PlayerId id : PlayerId.ALL) {
            playerState.put(id, new PlayerState(SortedBag.of(),
                    cardDeck.topCards(4), routes));
            cardDeck = cardDeck.withoutTopCards(4);
        }

        GameState lastgsF = new GameState(ticketDeck, CardState.of(cardDeck), PlayerId.PLAYER_1,playerState,PlayerId.PLAYER_2);
        GameState gsF = new GameState(ticketDeck, CardState.of(cardDeck), PlayerId.PLAYER_2, playerState, PlayerId.PLAYER_2);
        assertEquals(gsF.lastPlayer(), lastgsF.forNextTurn().lastPlayer());
        assertEquals(gsF.gameDeck, lastgsF.forNextTurn().gameDeck);
        assertEquals(gsF.cardState.deck.cards, lastgsF.forNextTurn().cardState.deck.cards);
        assertEquals(gsF.currentPlayerId(), lastgsF.forNextTurn().currentPlayerId());
        assertEquals(gsF.playerState, lastgsF.forNextTurn().playerState);

        GameState lastgsT = new GameState(ticketDeck, CardState.of(cardDeck), PlayerId.PLAYER_1,playerState,null);
        GameState gsT = new GameState(ticketDeck, CardState.of(cardDeck), PlayerId.PLAYER_2, playerState, lastgsT.currentPlayerId());
        assertEquals(gsT.lastPlayer(), lastgsT.forNextTurn().lastPlayer());
        assertEquals(gsT.gameDeck, lastgsT.forNextTurn().gameDeck);
        assertEquals(gsT.cardState.deck.cards, lastgsT.forNextTurn().cardState.deck.cards);
        assertEquals(gsT.currentPlayerId(), lastgsT.forNextTurn().currentPlayerId());
        assertEquals(gsT.playerState, lastgsT.forNextTurn().playerState);


    }


    @Test
    public void forNextTurnWorks2(){
        SortedBag<Ticket> tickets = SortedBag.of(2, ChMap.tickets().get(0), 3,
                ChMap.tickets().get(1));

        Deck<Ticket> ticketDeck = Deck.of(tickets, NOM_RANDOM);

        SortedBag.Builder<Card> cards = new SortedBag.Builder<>();

        cards.add(Card.RED);
        cards.add(Card.RED);
        cards.add(Card.RED);
        cards.add(Card.BLUE);
        cards.add(Card.BLUE);
        cards.add(Card.BLACK);
        cards.add(Card.LOCOMOTIVE);
        cards.add(Card.GREEN);
        cards.add(Card.ORANGE);
        cards.add(Card.RED);
        cards.add(Card.WHITE);
        cards.add(Card.WHITE);
        cards.add(Card.BLACK);
        cards.add(Card.GREEN);

        SortedBag<Card> card = cards.build();

        Deck<Card> cardDeck = Deck.of(card, NOM_RANDOM);

        Map<PlayerId, PlayerState> playerState = new EnumMap<>(PlayerId.class);

        List<Route> routes = List.of(  new Route("AT1_STG_1", AT1, STG, 4, Route.Level.UNDERGROUND, null),
                new Route("AT2_VAD_1", AT2, VAD, 1, Route.Level.UNDERGROUND, Color.RED),
                new Route("BAD_BAL_1", BAD, BAL, 3, Route.Level.UNDERGROUND, Color.RED),
                new Route("BAD_OLT_1", BAD, OLT, 2, Route.Level.OVERGROUND, Color.VIOLET),
                new Route("BAD_ZUR_1", BAD, ZUR, 1, Route.Level.OVERGROUND, Color.YELLOW),
                new Route("BAL_DE1_1", BAL, DE1, 1, Route.Level.UNDERGROUND, Color.BLUE),
                new Route("BAL_DEL_1", BAL, DEL, 2, Route.Level.UNDERGROUND, Color.YELLOW),
                new Route("BAL_OLT_1", BAL, OLT, 2, Route.Level.UNDERGROUND, Color.ORANGE),
                new Route("BEL_LOC_1", BEL, LOC, 1, Route.Level.UNDERGROUND, Color.BLACK),
                new Route("BEL_LUG_1", BEL, LUG, 1, Route.Level.UNDERGROUND, Color.RED),
                new Route("BEL_LUG_2", BEL, LUG, 1, Route.Level.UNDERGROUND, Color.YELLOW),
                new Route("BEL_WAS_1", BEL, WAS, 4, Route.Level.UNDERGROUND, null),
                new Route("BEL_WAS_2", BEL, WAS, 4, Route.Level.UNDERGROUND, null),
                new Route("BER_FRI_1", BER, FRI, 1, Route.Level.OVERGROUND, Color.ORANGE),
                new Route("BER_FRI_2", BER, FRI, 1, Route.Level.OVERGROUND, Color.YELLOW)
        );
        for (PlayerId id : PlayerId.ALL) {
            playerState.put(id, new PlayerState(SortedBag.of(),
                    cardDeck.topCards(4), routes));
            cardDeck = cardDeck.withoutTopCards(4);
        }

        GameState lastgsF = new GameState(ticketDeck, CardState.of(cardDeck), PlayerId.PLAYER_1,playerState,null);
        GameState gsF = new GameState(ticketDeck, CardState.of(cardDeck), PlayerId.PLAYER_2, playerState, null);
        assertEquals(gsF.lastPlayer(), lastgsF.forNextTurn().lastPlayer());
        assertEquals(gsF.gameDeck, lastgsF.forNextTurn().gameDeck);
        assertEquals(gsF.cardState.deck.cards, lastgsF.forNextTurn().cardState.deck.cards);
        assertEquals(gsF.currentPlayerId(), lastgsF.forNextTurn().currentPlayerId());
        assertEquals(gsF.playerState, lastgsF.forNextTurn().playerState);

        lastgsF = new GameState(ticketDeck, CardState.of(cardDeck), PlayerId.PLAYER_1,playerState,PlayerId.PLAYER_2);
        gsF = new GameState(ticketDeck, CardState.of(cardDeck), PlayerId.PLAYER_2, playerState, PlayerId.PLAYER_2);
        assertEquals(gsF.lastPlayer(), lastgsF.forNextTurn().lastPlayer());
        assertEquals(gsF.gameDeck, lastgsF.forNextTurn().gameDeck);
        assertEquals(gsF.cardState.deck.cards, lastgsF.forNextTurn().cardState.deck.cards);
        assertEquals(gsF.currentPlayerId(), lastgsF.forNextTurn().currentPlayerId());
        assertEquals(gsF.playerState, lastgsF.forNextTurn().playerState);
    }

    @Test
    public void withDrawnFaceUpThrows(){
        SortedBag<Ticket> tickets = SortedBag.of(2, ChMap.tickets().get(0), 3,
                ChMap.tickets().get(1));

        Deck<Ticket> ticketDeck = Deck.of(tickets, NOM_RANDOM);

        SortedBag.Builder<Card> cards = new SortedBag.Builder<>();

        cards.add(Card.RED);
        cards.add(Card.RED);

        SortedBag<Card> card = cards.build();

        Deck<Card> cardDeck = Deck.of(card, NOM_RANDOM);

        Map<PlayerId, PlayerState> playerState = new EnumMap<>(PlayerId.class);
        for (PlayerId id : PlayerId.ALL) {
            playerState.put(id, new PlayerState(SortedBag.of(),SortedBag.of()
                    , List.of()));
        }

        GameState gs = new GameState(ticketDeck,new CardState(SortedBag.of(5,Card.BLUE).toList(),cardDeck,SortedBag.of()),PlayerId.PLAYER_1,playerState,null);
        assertThrows(IllegalArgumentException.class, ()-> {
            gs.withDrawnFaceUpCard(1);
        });
    }

    @Test
    public void withBlindlyDrawnCardThrows(){
        SortedBag<Ticket> tickets = SortedBag.of(2, ChMap.tickets().get(0), 3,
                ChMap.tickets().get(1));

        Deck<Ticket> ticketDeck = Deck.of(tickets, NOM_RANDOM);

        SortedBag.Builder<Card> cards = new SortedBag.Builder<>();

        cards.add(Card.RED);
        cards.add(Card.RED);

        SortedBag<Card> card = cards.build();

        Deck<Card> cardDeck = Deck.of(card, NOM_RANDOM);

        Map<PlayerId, PlayerState> playerState = new EnumMap<>(PlayerId.class);
        for (PlayerId id : PlayerId.ALL) {
            playerState.put(id, new PlayerState(SortedBag.of(),SortedBag.of()
                    , List.of()));
        }

        GameState gs = new GameState(ticketDeck,new CardState(SortedBag.of(5,Card.BLUE).toList(),cardDeck,SortedBag.of()),PlayerId.PLAYER_1,playerState,null);
        assertThrows(IllegalArgumentException.class, ()-> {
            gs.withBlindlyDrawnCard();
        });
    }*/

}
