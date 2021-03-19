package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerStateTest {

    // initialPrecondition
    @Test
    void initialPreconditionV1() {
        SortedBag.Builder<Card> cards = new SortedBag.Builder<>();
        cards.add(Card.RED);
        cards.add(Card.RED);
        cards.add(Card.RED);
        cards.add(Card.BLUE);
        cards.add(Card.BLUE);
        cards.add(Card.BLACK);
        SortedBag<Card> initialCards = cards.build();
        //the number of cards is more than 4
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            PlayerState playerState = PlayerState.initial(initialCards);
        });
    }
    @Test
    void initialPreconditionV2() {
        SortedBag.Builder<Card> cards = new SortedBag.Builder<>();
        cards.add(Card.RED);
        cards.add(Card.RED);
        cards.add(Card.RED);
        SortedBag<Card> initialCards = cards.build();
        //the number of cards is less than 4
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            PlayerState playerState = PlayerState.initial(initialCards);
        });
    }
    @Test
    void initialPreconditionV3() {
        SortedBag.Builder<Card> cards = new SortedBag.Builder<>();
        cards.add(Card.RED);
        cards.add(Card.RED);
        cards.add(Card.RED);
        cards.add(Card.BLUE);
        SortedBag<Card> initialCards = cards.build();
        //the number of cards is 4
        PlayerState playerState = PlayerState.initial(initialCards);
        System.out.println("Cards  : "+playerState.cards());
        System.out.println("Routes : "+playerState.routes());
        System.out.println("Tickets  : "+playerState.tickets());
        assertEquals("{BLUE, 3×RED}",playerState.cards().toString());
        assertEquals("[]",playerState.routes().toString());
        assertEquals("{}",playerState.tickets().toString());
    }

    // tickets
    @Test
    void tickets() {
        SortedBag.Builder<Ticket> tickets=new SortedBag.Builder<>();
        tickets.add(ChMap.tickets().get(0));
        tickets.add(ChMap.tickets().get(1));

        List<Route> routes= new ArrayList<>();
        routes.add(ChMap.routes().get(0));
        routes.add(ChMap.routes().get(2));

        SortedBag.Builder<Card> cards = new SortedBag.Builder<>();
        cards.add(Card.RED);
        cards.add(Card.RED);
        cards.add(Card.RED);
        cards.add(Card.BLUE);
        cards.add(Card.BLUE);
        cards.add(Card.BLACK);
        SortedBag<Card> playerCards = cards.build();

        PlayerState playerState=new PlayerState(tickets.build(),playerCards,routes);
//        System.out.println(playerState.tickets());
        assertEquals("{Bâle - Berne (5), Bâle - Brigue (10)}",playerState.tickets().toString());
    }

    // withAddedTickets
    @Test
    void withAddedTickets() {
        SortedBag.Builder<Ticket> tickets=new SortedBag.Builder<>();
        tickets.add(ChMap.tickets().get(0));
        tickets.add(ChMap.tickets().get(1));

        List<Route> routes= new ArrayList<>();
        routes.add(ChMap.routes().get(0));
        routes.add(ChMap.routes().get(2));

        SortedBag.Builder<Card> cards = new SortedBag.Builder<>();
        cards.add(Card.RED);
        cards.add(Card.RED);
        cards.add(Card.RED);
        cards.add(Card.BLUE);
        cards.add(Card.BLUE);
        cards.add(Card.BLACK);
        SortedBag<Card> playerCards = cards.build();

        PlayerState playerState=new PlayerState(tickets.build(),playerCards,routes);
        System.out.println(playerState.tickets().toString());
        SortedBag.Builder<Ticket> newtickets=new SortedBag.Builder<>();
        newtickets.add(ChMap.tickets().get(2));
        newtickets.add(ChMap.tickets().get(3));

        String expectedMsg = "{Berne - Coire (10), Bâle - Berne (5), Bâle - Brigue (10), Bâle - Saint-Gall (8)}";
        assertEquals(expectedMsg, playerState.withAddedTickets(newtickets.build()).tickets().toString());
    }

    // cards
    @Test
    void cards() {
        SortedBag.Builder<Ticket> tickets=new SortedBag.Builder<>();
        tickets.add(ChMap.tickets().get(0));
        tickets.add(ChMap.tickets().get(1));

        List<Route> routes= new ArrayList<>();
        routes.add(ChMap.routes().get(0));
        routes.add(ChMap.routes().get(2));

        SortedBag.Builder<Card> cards = new SortedBag.Builder<>();
        cards.add(Card.RED);
        cards.add(Card.RED);
        cards.add(Card.RED);
        cards.add(Card.BLUE);
        cards.add(Card.BLUE);
        cards.add(Card.BLACK);
        cards.add(Card.LOCOMOTIVE);
        SortedBag<Card> playerCards1 = cards.build();

        PlayerState playerState=new PlayerState(tickets.build(),playerCards1,routes);
        assertEquals("{BLACK, 2×BLUE, 3×RED, LOCOMOTIVE}",playerState.cards().toString());
    }

    // withAddedCard
    @Test
    void withAddedCard() {
        SortedBag.Builder<Ticket> tickets=new SortedBag.Builder<>();
        tickets.add(ChMap.tickets().get(0));
        tickets.add(ChMap.tickets().get(1));

        List<Route> routes= new ArrayList<>();
        routes.add(ChMap.routes().get(0));
        routes.add(ChMap.routes().get(2));

        SortedBag.Builder<Card> cards = new SortedBag.Builder<>();
        cards.add(Card.RED);
        cards.add(Card.RED);
        cards.add(Card.RED);
        cards.add(Card.BLUE);
        cards.add(Card.BLUE);
        cards.add(Card.BLACK);
        SortedBag<Card> playerCards = cards.build();

        PlayerState playerState=new PlayerState(tickets.build(),playerCards,routes);
//        System.out.println(playerState.cards().toString());
        Card additionalCard= Card.RED;
//        System.out.println(playerState.withAddedCard(additionalCard).cards().toString());
        assertEquals("{BLACK, 2×BLUE, 4×RED}",playerState.withAddedCard(additionalCard).cards().toString());
    }

    // withAddedCards
    @Test
    void withAddedCards() {
        SortedBag.Builder<Ticket> tickets=new SortedBag.Builder<>();
        tickets.add(ChMap.tickets().get(0));
        tickets.add(ChMap.tickets().get(1));

        List<Route> routes= new ArrayList<>();
        routes.add(ChMap.routes().get(0));
        routes.add(ChMap.routes().get(2));

        SortedBag.Builder<Card> cards = new SortedBag.Builder<>();
        cards.add(Card.RED);
        cards.add(Card.RED);
        cards.add(Card.RED);
        cards.add(Card.BLUE);
        cards.add(Card.BLUE);
        cards.add(Card.BLACK);
        SortedBag<Card> playerCards1 = cards.build();

        PlayerState playerState=new PlayerState(tickets.build(),playerCards1,routes);
        System.out.println(playerState.cards().toString());
        SortedBag.Builder<Card> additionalCards = new SortedBag.Builder<>();
        additionalCards.add(Card.RED);
        additionalCards.add(Card.RED);
        additionalCards.add(Card.RED);
        additionalCards.add(Card.BLUE);
        additionalCards.add(Card.BLUE);
        additionalCards.add(Card.BLACK);
        SortedBag<Card> playerCards2 = additionalCards.build();
//        System.out.println(playerState.withAddedCards(playerCards2).cards().toString());
        assertEquals("{2×BLACK, 4×BLUE, 6×RED}",playerState.withAddedCards(playerCards2).cards().toString());
    }

    // canClaimRoute
    @Test
    void canClaimRoute0() {
        SortedBag.Builder<Ticket> tickets=new SortedBag.Builder<>();
        tickets.add(ChMap.tickets().get(0));
        tickets.add(ChMap.tickets().get(1));

        List<Route> routes= new ArrayList<>();
        routes.add(ChMap.routes().get(0));
        routes.add(ChMap.routes().get(2));

        SortedBag.Builder<Card> cards = new SortedBag.Builder<>();
        cards.add(Card.RED);
        cards.add(Card.RED);
        cards.add(Card.RED);
        cards.add(Card.BLUE);
        cards.add(Card.BLUE);
        cards.add(Card.BLACK);
        SortedBag<Card> playerCards = cards.build();


        PlayerState playerState=new PlayerState(tickets.build(),playerCards,routes);
        // We don't possess this route, and we don't possess the required cards either
        Route routeToTake=ChMap.routes().get(3);
//        System.out.println(playerState.canClaimRoute(routeToTake));
        assertEquals(false,playerState.canClaimRoute(routeToTake));
    }
    @Test
    void canClaimRoute1() {
        SortedBag.Builder<Ticket> tickets=new SortedBag.Builder<>();
        tickets.add(ChMap.tickets().get(0));
        tickets.add(ChMap.tickets().get(1));

        List<Route> routes= new ArrayList<>();
        routes.add(ChMap.routes().get(2));

        SortedBag.Builder<Card> cards = new SortedBag.Builder<>();
        cards.add(Card.RED);
        cards.add(Card.RED);
        cards.add(Card.RED);
        cards.add(Card.BLUE);
        cards.add(Card.BLUE);
        cards.add(Card.BLACK);
        SortedBag<Card> playerCards1 = cards.build();

        PlayerState playerState=new PlayerState(tickets.build(),playerCards1,routes);
        // tunnel
        Route routeToTake=ChMap.routes().get(0);
//        System.out.println(playerState.canClaimRoute(routeToTake));
        assertEquals(false,playerState.canClaimRoute(routeToTake));
    }
  /*  @Test
    void canClaimRoute2() {

        SortedBag.Builder<Ticket> tickets=new SortedBag.Builder<>();
        tickets.add(ChMap.tickets().get(0));
        tickets.add(ChMap.tickets().get(1));

        List<Route> routes= new ArrayList<>();
        routes.add(ChMap.routes().get(0));
        routes.add(ChMap.routes().get(2));

        SortedBag.Builder<Card> cards = new SortedBag.Builder<>();
        cards.add(Card.RED);
        cards.add(Card.RED);
        cards.add(Card.RED);
        cards.add(Card.BLUE);
        cards.add(Card.BLUE);
        cards.add(Card.BLACK);
        SortedBag<Card> playerCards1 = cards.build();

        PlayerState playerState=new PlayerState(tickets.build(),playerCards1,routes);
        //on test avec une route qui est vide pour tcheck precondition
        Route routeToTake = null;
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            System.out.println(playerState.canClaimRoute(routeToTake));
        });
    }*/
    @Test
    void canClaimRoute3() {

        SortedBag.Builder<Ticket> tickets=new SortedBag.Builder<>();
        tickets.add(ChMap.tickets().get(0));
        tickets.add(ChMap.tickets().get(1));

        List<Route> routes= new ArrayList<>();
        routes.add(ChMap.routes().get(0));
        routes.add(ChMap.routes().get(2));

        SortedBag.Builder<Card> cards = new SortedBag.Builder<>();
        cards.add(Card.RED);
        cards.add(Card.RED);
        cards.add(Card.RED);
        cards.add(Card.BLUE);
        cards.add(Card.BLUE);
        cards.add(Card.BLACK);
        SortedBag<Card> playerCards1 = cards.build();

        PlayerState playerState=new PlayerState(tickets.build(),playerCards1,routes);
        // A route that we can take
        Route routeToTake = ChMap.routes().get(2);
//        System.out.println(playerState.canClaimRoute(routeToTake));
        assertEquals(true,playerState.canClaimRoute(routeToTake));
    }

    // possibleClaimCards
  /*  @Test
    void possibleClaimCardsException() {
        SortedBag.Builder<Ticket> tickets=new SortedBag.Builder<>();
        tickets.add(ChMap.tickets().get(0));
        tickets.add(ChMap.tickets().get(1));

        List<Route> routes= new ArrayList<>();
        routes.add(ChMap.routes().get(0));
        routes.add(ChMap.routes().get(2));

        SortedBag.Builder<Card> cards = new SortedBag.Builder<>();
        cards.add(Card.RED);
        cards.add(Card.RED);
        cards.add(Card.RED);
        cards.add(Card.BLUE);
        cards.add(Card.BLUE);
        cards.add(Card.BLACK);
        SortedBag<Card> playerCards = cards.build();

        PlayerState playerState=new PlayerState(tickets.build(),playerCards,routes);
        // We don't have the necessary cards to possess this route. this should rise an exception
        Route routeToTake=ChMap.routes().get(3);
        System.out.println(playerState.carCount());
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            System.out.println(playerState.possibleClaimCards(routeToTake));
        });
    }*/
    @Test
    void possibleClaimCards1() {
        SortedBag.Builder<Ticket> tickets=new SortedBag.Builder<>();
        tickets.add(ChMap.tickets().get(0));
        tickets.add(ChMap.tickets().get(1));

        List<Route> routes= new ArrayList<>();
        routes.add(ChMap.routes().get(0));
        routes.add(ChMap.routes().get(2));

        SortedBag.Builder<Card> cards = new SortedBag.Builder<>();
        cards.add(Card.RED);
        cards.add(Card.RED);
        cards.add(Card.RED);
        cards.add(Card.ORANGE);
        cards.add(Card.BLUE);
        cards.add(Card.BLUE);
        cards.add(Card.BLACK);
        cards.add(Card.LOCOMOTIVE);
        cards.add(Card.LOCOMOTIVE);
        SortedBag<Card> playerCards = cards.build();

        PlayerState playerState=new PlayerState(tickets.build(),playerCards,routes);
        // We can take this route
        Route routeToTake=ChMap.routes().get(7);
//        System.out.println(playerState.possibleClaimCards(routeToTake));//
        assertEquals("[{ORANGE, LOCOMOTIVE}, {2×LOCOMOTIVE}]",playerState.possibleClaimCards(routeToTake).toString());

    }
    @Test
    void possibleClaimCards2() {
        SortedBag.Builder<Ticket> tickets=new SortedBag.Builder<>();
        tickets.add(ChMap.tickets().get(0));
        tickets.add(ChMap.tickets().get(1));

        List<Route> routes= new ArrayList<>();
        routes.add(ChMap.routes().get(0));
        routes.add(ChMap.routes().get(2));

        SortedBag.Builder<Card> cards = new SortedBag.Builder<>();
        cards.add(Card.RED);
        cards.add(Card.RED);
        cards.add(Card.RED);
        cards.add(Card.ORANGE);
        cards.add(Card.BLUE);
        cards.add(Card.BLUE);
        cards.add(Card.BLACK);
        SortedBag<Card> playerCards = cards.build();

        PlayerState playerState=new PlayerState(tickets.build(),playerCards,routes);
        // We can take this route
        Route routeToTake=ChMap.routes().get(9);
//        System.out.println(playerState.possibleClaimCards(routeToTake));
        assertEquals("[{RED}]",playerState.possibleClaimCards(routeToTake).toString());

    }

    // possibleAdditionalCards
    @Test
    void possibleAdditionalCards_NoOptions(){
        SortedBag.Builder<Card> cardsBuilder = new SortedBag.Builder<>();
        cardsBuilder.add(Card.GREEN);
        cardsBuilder.add(Card.GREEN);
        cardsBuilder.add(Card.GREEN);
        cardsBuilder.add(Card.BLUE);
        cardsBuilder.add(Card.BLUE);
        cardsBuilder.add(Card.LOCOMOTIVE);
        cardsBuilder.add(Card.LOCOMOTIVE);
        SortedBag<Card> cards=cardsBuilder.build();

        SortedBag.Builder<Card> drawnCardsBuilder = new SortedBag.Builder<>();
        drawnCardsBuilder.add(Card.GREEN);
        drawnCardsBuilder.add(Card.BLUE);
        drawnCardsBuilder.add(Card.LOCOMOTIVE);

        SortedBag.Builder<Card> initialBuilder = new SortedBag.Builder<>();
        initialBuilder.add(Card.LOCOMOTIVE);
        initialBuilder.add(Card.LOCOMOTIVE);
        SortedBag<Card> initialCards = initialBuilder.build();

        System.out.println("Cartes en main au début du jeu : "+ cards);
        System.out.println("Carte-s posée-s pour s'emparer de la route/tunnel : "+initialCards);
        System.out.println("Carte-s qui reste-nt en main du joueur : "+cards.difference(initialCards));

        SortedBag.Builder<Ticket> ticketBuilder = new SortedBag.Builder<>();
        ticketBuilder.add(ChMap.tickets().get(0));


        PlayerState playerState = new PlayerState(ticketBuilder.build(), cards, List.of(ChMap.routes().get(0)));
        System.out.println("Options du joueur : "+playerState.possibleAdditionalCards(2, initialCards,drawnCardsBuilder.build()));
        assertEquals(List.of(), playerState.possibleAdditionalCards(2, initialCards,drawnCardsBuilder.build()));

    }
    @Test
    void possibleAdditionalCards_LocomotivesOnly(){
        SortedBag.Builder<Card> cardsBuilder = new SortedBag.Builder<>();
        cardsBuilder.add(Card.GREEN);
        cardsBuilder.add(Card.GREEN);
        cardsBuilder.add(Card.GREEN);
        cardsBuilder.add(Card.BLUE);
        cardsBuilder.add(Card.BLUE);
        cardsBuilder.add(Card.LOCOMOTIVE);
        cardsBuilder.add(Card.LOCOMOTIVE);
        cardsBuilder.add(Card.LOCOMOTIVE);
        cardsBuilder.add(Card.LOCOMOTIVE);
        SortedBag<Card> cards=cardsBuilder.build();

        SortedBag.Builder<Card> drawnCardsBuilder = new SortedBag.Builder<>();
        drawnCardsBuilder.add(Card.GREEN);
        drawnCardsBuilder.add(Card.BLUE);
        drawnCardsBuilder.add(Card.LOCOMOTIVE);

        SortedBag.Builder<Card> initialBuilder = new SortedBag.Builder<>();
        initialBuilder.add(Card.LOCOMOTIVE);
        initialBuilder.add(Card.LOCOMOTIVE);
        SortedBag<Card> initialCards = initialBuilder.build();

        // cartes additionnelles à jouer
        int additionalCardsCount = 2;

        System.out.println("Cartes en main au début du jeu : "+ cards);
        System.out.println("Carte-s posée-s pour s'emparer de la route/tunnel : "+initialCards);
        System.out.println("Carte-s qui reste-nt en main du joueur : "+cards.difference(initialCards));
        System.out.println("Carte additionnelles à jouer : "+additionalCardsCount);

        SortedBag.Builder<Ticket> ticketBuilder = new SortedBag.Builder<>();
        ticketBuilder.add(ChMap.tickets().get(0));


        PlayerState playerState = new PlayerState(ticketBuilder.build(), cards,List.of(ChMap.routes().get(0)));
        System.out.println("Options du joueur : "+playerState.possibleAdditionalCards(additionalCardsCount, initialCards,drawnCardsBuilder.build()));
        assertEquals("[{2×LOCOMOTIVE}]", playerState.possibleAdditionalCards(additionalCardsCount, initialCards,drawnCardsBuilder.build()).toString());

    }
    @Test
    void possibleAdditionalCards_NoLocomotives() {
        SortedBag.Builder<Card> cardsBuilder = new SortedBag.Builder<>();
        cardsBuilder.add(Card.GREEN);
        cardsBuilder.add(Card.GREEN);
        cardsBuilder.add(Card.GREEN);
        cardsBuilder.add(Card.BLUE);
        cardsBuilder.add(Card.BLUE);
        cardsBuilder.add(Card.LOCOMOTIVE);
        cardsBuilder.add(Card.LOCOMOTIVE);
        cardsBuilder.add(Card.LOCOMOTIVE);
        cardsBuilder.add(Card.LOCOMOTIVE);
        SortedBag<Card> cards = cardsBuilder.build();

        SortedBag.Builder<Card> drawnCardsBuilder = new SortedBag.Builder<>();
        drawnCardsBuilder.add(Card.GREEN);
        drawnCardsBuilder.add(Card.BLUE);
        drawnCardsBuilder.add(Card.LOCOMOTIVE);

        SortedBag.Builder<Card> initialBuilder = new SortedBag.Builder<>();
        initialBuilder.add(Card.GREEN);
        initialBuilder.add(Card.GREEN);
        SortedBag<Card> initialCards = initialBuilder.build();

        // cartes additionnelles à jouer
        int additionalCardsCount = 2;

        System.out.println("Cartes en main au début du jeu : " + cards);
        System.out.println("Carte-s posée-s pour s'emparer de la route/tunnel : " + initialCards);
        System.out.println("Carte-s qui reste-nt en main du joueur : " + cards.difference(initialCards));
        System.out.println("Carte additionnelles à jouer : "+additionalCardsCount);

        SortedBag.Builder<Ticket> ticketBuilder = new SortedBag.Builder<>();
        ticketBuilder.add(ChMap.tickets().get(0));

        PlayerState playerState = new PlayerState(ticketBuilder.build(), cards, List.of(ChMap.routes().get(0)));
        System.out.println("Options du joueur : " + playerState.possibleAdditionalCards(additionalCardsCount, initialCards, drawnCardsBuilder.build()));
        assertEquals("[{GREEN, LOCOMOTIVE}, {2×LOCOMOTIVE}]", playerState.possibleAdditionalCards(additionalCardsCount, initialCards,drawnCardsBuilder.build()).toString());
    }
    @Test
    void possibleAdditionalCards_LocomotiveAndCard() {
        SortedBag.Builder<Card> cardsBuilder = new SortedBag.Builder<>();
        cardsBuilder.add(Card.GREEN);
        cardsBuilder.add(Card.GREEN);
        cardsBuilder.add(Card.GREEN);
        cardsBuilder.add(Card.BLUE);
        cardsBuilder.add(Card.BLUE);
        cardsBuilder.add(Card.LOCOMOTIVE);
        cardsBuilder.add(Card.LOCOMOTIVE);
        cardsBuilder.add(Card.LOCOMOTIVE);
        cardsBuilder.add(Card.LOCOMOTIVE);
        SortedBag<Card> cards = cardsBuilder.build();

        SortedBag.Builder<Card> drawnCardsBuilder = new SortedBag.Builder<>();
        drawnCardsBuilder.add(Card.GREEN);
        drawnCardsBuilder.add(Card.BLUE);
        drawnCardsBuilder.add(Card.LOCOMOTIVE);

        SortedBag.Builder<Card> initialBuilder = new SortedBag.Builder<>();
        initialBuilder.add(Card.GREEN);
        initialBuilder.add(Card.LOCOMOTIVE);
        SortedBag<Card> initialCards = initialBuilder.build();

        // cartes additionnelles à jouer
        int additionalCardsCount = 3;

        System.out.println("Cartes en main au début du jeu : " + cards);
        System.out.println("Carte-s posée-s pour s'emparer de la route/tunnel : " + initialCards);
        System.out.println("Carte-s qui reste-nt en main du joueur : " + cards.difference(initialCards));
        System.out.println("Carte additionnelles à jouer : "+additionalCardsCount);
        SortedBag.Builder<Ticket> ticketBuilder = new SortedBag.Builder<>();
        ticketBuilder.add(ChMap.tickets().get(0));

        PlayerState playerState = new PlayerState(ticketBuilder.build(), cards, List.of(ChMap.routes().get(0)));
        System.out.println("Options du joueur : " + playerState.possibleAdditionalCards(additionalCardsCount, initialCards, drawnCardsBuilder.build()));
        assertEquals("[{2×GREEN, LOCOMOTIVE}, {GREEN, 2×LOCOMOTIVE}, {3×LOCOMOTIVE}]", playerState.possibleAdditionalCards(additionalCardsCount, initialCards,drawnCardsBuilder.build()).toString());
    }
    @Test
    void possibleAdditionalCards_CourseSample() {
        SortedBag.Builder<Card> cardsBuilder = new SortedBag.Builder<>();
        cardsBuilder.add(Card.GREEN);
        cardsBuilder.add(Card.GREEN);
        cardsBuilder.add(Card.GREEN);
        cardsBuilder.add(Card.BLUE);
        cardsBuilder.add(Card.BLUE);
        cardsBuilder.add(Card.LOCOMOTIVE);
        cardsBuilder.add(Card.LOCOMOTIVE);
        SortedBag<Card> cards = cardsBuilder.build();

        SortedBag.Builder<Card> drawnCardsBuilder = new SortedBag.Builder<>();
        drawnCardsBuilder.add(Card.GREEN);
        drawnCardsBuilder.add(Card.BLUE);
        drawnCardsBuilder.add(Card.LOCOMOTIVE);

        SortedBag.Builder<Card> initialBuilder = new SortedBag.Builder<>();
        initialBuilder.add(Card.GREEN);
        SortedBag<Card> initialCards = initialBuilder.build();
        // cartes additionnelles à jouer
        int additionalCardsCount = 2;

        System.out.println("Cartes en main au début du jeu : " + cards);
        System.out.println("Carte-s posée-s pour s'emparer de la route/tunnel : " + initialCards);
        System.out.println("Carte-s qui reste-nt en main du joueur : " + cards.difference(initialCards));
        System.out.println("Carte additionnelles à jouer : "+additionalCardsCount);

        SortedBag.Builder<Ticket> ticketBuilder = new SortedBag.Builder<>();
        ticketBuilder.add(ChMap.tickets().get(0));

        PlayerState playerState = new PlayerState(ticketBuilder.build(), cards, List.of(ChMap.routes().get(0)));
        System.out.println("Options du joueur : " + playerState.possibleAdditionalCards(additionalCardsCount, initialCards, drawnCardsBuilder.build()));
        assertEquals("[{2×GREEN}, {GREEN, LOCOMOTIVE}, {2×LOCOMOTIVE}]", playerState.possibleAdditionalCards(additionalCardsCount, initialCards,drawnCardsBuilder.build()).toString());
    }
    @Test
    void possibleAdditionalCards_AdditionalCardsCountException() {
        SortedBag.Builder<Card> cardsBuilder = new SortedBag.Builder<>();
        cardsBuilder.add(Card.GREEN);
        cardsBuilder.add(Card.GREEN);
        cardsBuilder.add(Card.GREEN);
        cardsBuilder.add(Card.BLUE);
        cardsBuilder.add(Card.BLUE);
        cardsBuilder.add(Card.LOCOMOTIVE);
        cardsBuilder.add(Card.LOCOMOTIVE);
        SortedBag<Card> cards = cardsBuilder.build();

        SortedBag.Builder<Card> drawnCardsBuilder = new SortedBag.Builder<>();
        drawnCardsBuilder.add(Card.GREEN);
        drawnCardsBuilder.add(Card.BLUE);
        drawnCardsBuilder.add(Card.LOCOMOTIVE);

        SortedBag.Builder<Card> initialBuilder = new SortedBag.Builder<>();
        initialBuilder.add(Card.GREEN);
        SortedBag<Card> initialCards = initialBuilder.build();

        System.out.println("Cartes en main au début du jeu : " + cards);
        System.out.println("Carte-s posée-s pour s'emparer de la route/tunnel : " + initialCards);
        System.out.println("Carte-s qui reste-nt en main du joueur : " + cards.difference(initialCards));

        SortedBag.Builder<Ticket> ticketBuilder = new SortedBag.Builder<>();
        ticketBuilder.add(ChMap.tickets().get(0));

        PlayerState playerState = new PlayerState(ticketBuilder.build(), cards, List.of(ChMap.routes().get(0)));
        // additional Cards Count = 0
        Exception exception0 = assertThrows(IllegalArgumentException.class, () -> {
            playerState.possibleAdditionalCards(0, initialCards, drawnCardsBuilder.build());
        });

        Exception exception4 = assertThrows(IllegalArgumentException.class, () -> {
            playerState.possibleAdditionalCards(4, initialCards, drawnCardsBuilder.build());
        });
    }
    @Test
    void possibleAdditionalCards_InitialCardsEmptyException() {
        SortedBag.Builder<Card> cardsBuilder = new SortedBag.Builder<>();
        cardsBuilder.add(Card.GREEN);
        cardsBuilder.add(Card.GREEN);
        cardsBuilder.add(Card.GREEN);
        cardsBuilder.add(Card.BLUE);
        cardsBuilder.add(Card.BLUE);
        cardsBuilder.add(Card.LOCOMOTIVE);
        cardsBuilder.add(Card.LOCOMOTIVE);
        SortedBag<Card> cards = cardsBuilder.build();

        SortedBag.Builder<Card> drawnCardsBuilder = new SortedBag.Builder<>();
        drawnCardsBuilder.add(Card.GREEN);
        drawnCardsBuilder.add(Card.BLUE);
        drawnCardsBuilder.add(Card.LOCOMOTIVE);

        SortedBag.Builder<Card> initialBuilder = new SortedBag.Builder<>();
        SortedBag<Card> initialCards = initialBuilder.build(); // empty sorted Bag

        System.out.println("Cartes en main au début du jeu : " + cards);
        System.out.println("Carte-s posée-s pour s'emparer de la route/tunnel : " + initialCards);
        System.out.println("Carte-s qui reste-nt en main du joueur : " + cards.difference(initialCards));

        SortedBag.Builder<Ticket> ticketBuilder = new SortedBag.Builder<>();
        ticketBuilder.add(ChMap.tickets().get(0));

        PlayerState playerState = new PlayerState(ticketBuilder.build(), cards, List.of(ChMap.routes().get(0)));
        // initial cards is empty
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            playerState.possibleAdditionalCards(2, initialCards, drawnCardsBuilder.build());
        });

    }
    @Test
    void possibleAdditionalCards_InitialCardsMoreThanTwoTypesException() {
        SortedBag.Builder<Card> cardsBuilder = new SortedBag.Builder<>();
        cardsBuilder.add(Card.GREEN);
        cardsBuilder.add(Card.GREEN);
        cardsBuilder.add(Card.GREEN);
        cardsBuilder.add(Card.BLUE);
        cardsBuilder.add(Card.BLUE);
        cardsBuilder.add(Card.LOCOMOTIVE);
        cardsBuilder.add(Card.LOCOMOTIVE);
        SortedBag<Card> cards = cardsBuilder.build();

        SortedBag.Builder<Card> drawnCardsBuilder = new SortedBag.Builder<>();
        drawnCardsBuilder.add(Card.GREEN);
        drawnCardsBuilder.add(Card.BLUE);
        drawnCardsBuilder.add(Card.LOCOMOTIVE);

        SortedBag.Builder<Card> initialBuilder = new SortedBag.Builder<>();
        initialBuilder.add(Card.LOCOMOTIVE); // first type
        initialBuilder.add(Card.RED); // second type
        initialBuilder.add(Card.GREEN); // third type
        SortedBag<Card> initialCards = initialBuilder.build();

        System.out.println("Cartes en main au début du jeu : " + cards);
        System.out.println("Carte-s posée-s pour s'emparer de la route/tunnel : " + initialCards);
        System.out.println("Carte-s qui reste-nt en main du joueur : " + cards.difference(initialCards));

        SortedBag.Builder<Ticket> ticketBuilder = new SortedBag.Builder<>();
        ticketBuilder.add(ChMap.tickets().get(0));

        PlayerState playerState = new PlayerState(ticketBuilder.build(), cards, List.of(ChMap.routes().get(0)));
        // initial cards contains more than two types (in this sample : three types)
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            playerState.possibleAdditionalCards(2, initialCards, drawnCardsBuilder.build());
        });
    }
    @Test
    void possibleAdditionalCards_DrawnCardsException() {
        SortedBag.Builder<Card> cardsBuilder = new SortedBag.Builder<>();
        cardsBuilder.add(Card.GREEN);
        cardsBuilder.add(Card.GREEN);
        cardsBuilder.add(Card.GREEN);
        cardsBuilder.add(Card.BLUE);
        cardsBuilder.add(Card.BLUE);
        cardsBuilder.add(Card.LOCOMOTIVE);
        cardsBuilder.add(Card.LOCOMOTIVE);
        SortedBag<Card> cards = cardsBuilder.build();

        SortedBag.Builder<Card> drawnCardsBuilder = new SortedBag.Builder<>();
        drawnCardsBuilder.add(Card.GREEN);
        drawnCardsBuilder.add(Card.BLUE);
        drawnCardsBuilder.add(Card.LOCOMOTIVE);
        drawnCardsBuilder.add(Card.LOCOMOTIVE);
        SortedBag<Card> drawnCards = drawnCardsBuilder.build(); // contains more than 3 cards

        SortedBag.Builder<Card> initialBuilder = new SortedBag.Builder<>();
        initialBuilder.add(Card.LOCOMOTIVE);
        initialBuilder.add(Card.GREEN);
        SortedBag<Card> initialCards = initialBuilder.build();

        System.out.println("Cartes en main au début du jeu : " + cards);
        System.out.println("Carte-s posée-s pour s'emparer de la route/tunnel : " + initialCards);
        System.out.println("Carte-s qui reste-nt en main du joueur : " + cards.difference(initialCards));

        SortedBag.Builder<Ticket> ticketBuilder = new SortedBag.Builder<>();
        ticketBuilder.add(ChMap.tickets().get(0));

        PlayerState playerState = new PlayerState(ticketBuilder.build(), cards, List.of(ChMap.routes().get(0)));
        // drawn cards contains more than 3 cards
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            playerState.possibleAdditionalCards(2, initialCards, drawnCards);
        });
    }

    // withClaimedRoute
    @Test
    void withClaimedRoute0(){
        SortedBag.Builder<Ticket> tickets=new SortedBag.Builder<>();
        tickets.add(ChMap.tickets().get(0));
        tickets.add(ChMap.tickets().get(1));

        List<Route> routes= new ArrayList<>();
        routes.add(ChMap.routes().get(0));
        routes.add(ChMap.routes().get(1));

        SortedBag.Builder<Card> cards = new SortedBag.Builder<>();
        cards.add(Card.RED);
        cards.add(Card.RED);
        cards.add(Card.RED);
        cards.add(Card.BLUE);
        cards.add(Card.BLUE);
        cards.add(Card.BLACK);
        SortedBag<Card> playerCards1 = cards.build();

        // claim cards
        SortedBag.Builder<Card> claimCardsBuilder = new SortedBag.Builder<>();
        // no locomtives
        claimCardsBuilder.add(Card.RED);
        claimCardsBuilder.add(Card.RED);
        claimCardsBuilder.add(Card.RED);

        SortedBag<Card> claimCards= claimCardsBuilder.build();
        // Route to claim
        Route claimedRoute = ChMap.routes().get(2);

        PlayerState playerState=new PlayerState(tickets.build(),playerCards1,routes);
//        System.out.println(playerState.withClaimedRoute(claimedRoute, claimCards).cards().toString());

        List<Route> newRoutes = new ArrayList<>(routes);
        newRoutes.add(claimedRoute);
        assertEquals("{BLACK, 2×BLUE}",playerState.withClaimedRoute(claimedRoute, claimCards).cards().toString());
        assertEquals(newRoutes.get(0).id(),playerState.withClaimedRoute(claimedRoute, claimCards).routes().get(0).id());
        assertEquals(newRoutes.get(1).id(),playerState.withClaimedRoute(claimedRoute, claimCards).routes().get(1).id());
        assertEquals(newRoutes.get(2).id(),playerState.withClaimedRoute(claimedRoute, claimCards).routes().get(2).id());

    }

    // ticketPoints
    @Test
    void ticketPoints0() {
        SortedBag.Builder<Ticket> tickets=new SortedBag.Builder<>();
        tickets.add(ChMap.tickets().get(0));
        tickets.add(ChMap.tickets().get(1));

        List<Route> routes= new ArrayList<>();
        routes.add(ChMap.routes().get(0));
        routes.add(ChMap.routes().get(2));

        SortedBag.Builder<Card> cards = new SortedBag.Builder<>();
        cards.add(Card.RED);
        cards.add(Card.RED);
        cards.add(Card.RED);
        cards.add(Card.BLUE);
        cards.add(Card.BLUE);
        cards.add(Card.BLACK);
        SortedBag<Card> playerCards = cards.build();


        PlayerState playerState=new PlayerState(tickets.build(),playerCards,routes);
        // the tickets objectives are not met by the players' cards
        //  System.out.println(playerState.ticketPoints());
        assertEquals(-15,playerState.ticketPoints());
    }
    @Test
    void ticketPoints1() {
        SortedBag.Builder<Ticket> tickets=new SortedBag.Builder<>();
        tickets.add(ChMap.tickets().get(6));

        List<Route> routes= new ArrayList<>();
        // these routes realise the objective of the first ticket
        routes.add(ChMap.routes().get(16));
        routes.add(ChMap.routes().get(62));
        routes.add(ChMap.routes().get(86));

        SortedBag.Builder<Card> cards = new SortedBag.Builder<>();
        cards.add(Card.RED);
        cards.add(Card.RED);
        cards.add(Card.RED);
        cards.add(Card.BLUE);
        cards.add(Card.BLUE);
        cards.add(Card.BLACK);
        SortedBag<Card> playerCards = cards.build();

        PlayerState playerState=new PlayerState(tickets.build(),playerCards,routes);
//        System.out.println(playerState.ticketPoints());
        // the only ticket the player has an objective being met by the player's cards
        assertEquals(6,playerState.ticketPoints());
    }
    @Test
    void ticketPoints2() {
        SortedBag.Builder<Ticket> tickets=new SortedBag.Builder<>();
        tickets.add(ChMap.tickets().get(0)); // this objective is not to be met with the player's routes
        tickets.add(ChMap.tickets().get(6)); // this objective is to be met with the player's routes

        List<Route> routes= new ArrayList<>();
        // these routes realise the objective of the first ticket
        routes.add(ChMap.routes().get(16));
        routes.add(ChMap.routes().get(62));
        routes.add(ChMap.routes().get(86));

        SortedBag.Builder<Card> cards = new SortedBag.Builder<>();
        cards.add(Card.RED);
        cards.add(Card.RED);
        cards.add(Card.RED);
        cards.add(Card.BLUE);
        cards.add(Card.BLUE);
        cards.add(Card.BLACK);
        SortedBag<Card> playerCards = cards.build();


        PlayerState playerState=new PlayerState(tickets.build(),playerCards,routes);
//        System.out.println(playerState.ticketPoints());
        // One ticket's objective is being met by the player's cards, the other ticket's is not
        assertEquals(1,playerState.ticketPoints());
    }

    // finalPoints
    @Test
    void finalPoints() {
        SortedBag.Builder<Ticket> tickets=new SortedBag.Builder<>();
        tickets.add(ChMap.tickets().get(0));
        tickets.add(ChMap.tickets().get(1));

        List<Route> routes= new ArrayList<>();
        routes.add(ChMap.routes().get(0));
        routes.add(ChMap.routes().get(2));

        SortedBag.Builder<Card> cards = new SortedBag.Builder<>();
        cards.add(Card.RED);
        cards.add(Card.RED);
        cards.add(Card.RED);
        cards.add(Card.BLUE);
        cards.add(Card.BLUE);
        cards.add(Card.BLACK);
        SortedBag<Card> playerCards = cards.build();
        PlayerState playerState=new PlayerState(tickets.build(),playerCards,routes);
        assertEquals(-4,playerState.finalPoints());
    }

}