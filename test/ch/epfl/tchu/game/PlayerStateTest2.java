package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerStateTest2 {

    //stations
    private static final Station BAL = new Station(1, "Bâle");
    private static final Station BER = new Station(3, "Berne");

    //tickets:
    //city to city
    Ticket ticketBalBer = new Ticket(BAL, BER, 5);
    //city to country
    Ticket ticketFromBE = ChMap.tickets().get(34);
    //country to country
    Ticket getTicketFromDE = ChMap.tickets().get(39);

    //unique trip for ticket
    //multiple trips on ticket

    @Test
    public void playerStateConstructorWorks(){
        //player's tickets
        List<Ticket> tickets = List.of(ticketFromBE, ticketBalBer, getTicketFromDE);
        SortedBag<Ticket> ticketList = SortedBag.of(tickets);

        //player's cards
        List<Card> idk2 = List.of(Card.BLUE, Card.BLACK,Card.VIOLET, Card.RED, Card.ORANGE);
        SortedBag<Card> cards2 = SortedBag.of(idk2);

        //player's routes
        List<Route> routes = List.of(ChMap.routes().get(0), ChMap.routes().get(4),ChMap.routes().get(7));

        //player's state
        PlayerState playerState = new PlayerState(ticketList, cards2, routes);

        assertEquals(ticketList, playerState.tickets());
        assertEquals(cards2, playerState.cards());
        assertEquals(routes, playerState.routes());
    }

    @Test
    public void playerStateInitialTestExceptionsToBeThrown(){
        //empty
        SortedBag<Card> cardEmpty = SortedBag.of();
        //more than 4
        List<Card> idk2 = List.of(Card.BLUE, Card.BLACK,Card.VIOLET, Card.RED, Card.ORANGE);
        SortedBag<Card> cards2 = SortedBag.of(idk2);
        //less than 4
        List<Card> idk = List.of(Card.BLUE, Card.BLACK,Card.VIOLET);
        SortedBag<Card> cards = SortedBag.of(idk);

        assertThrows(IllegalArgumentException.class, ()->{
            PlayerState.initial(cardEmpty);
        });
        assertThrows(IllegalArgumentException.class, ()->{
            PlayerState.initial(cards2);
        });
        assertThrows(IllegalArgumentException.class, ()->{
            PlayerState.initial(cards);
        });

    }

    @Test
    public void initialTestPlayerStateReturned(){
        //should be the state where the player has no routes no tickets and the given initial cards

        List<Card> idk2 = List.of(Card.BLUE, Card.BLACK, Card.VIOLET, Card.RED);
        SortedBag<Card> cards2 = SortedBag.of(idk2);

        //same cards
        assertEquals(cards2, PlayerState.initial(cards2).cards());
        //0 routes
        assertEquals(List.of(), PlayerState.initial(cards2).routes());
        //no tickets
        assertEquals(SortedBag.of(), PlayerState.initial(cards2).tickets());
    }

    @Test
    public void ReturnsTicketsTest(){
        //empty List
        SortedBag<Ticket> noTickets = SortedBag.of();
        PlayerState playerState = new PlayerState(noTickets, SortedBag.of(1,Card.BLUE, 2,Card.VIOLET), List.of());
        //not empty
        List<Ticket> tickets = List.of(ticketFromBE, ticketBalBer, getTicketFromDE);
        SortedBag<Ticket> ticketList = SortedBag.of(tickets);
        PlayerState playerState1 = new PlayerState(ticketList, SortedBag.of(1,Card.BLUE, 2,Card.VIOLET), List.of());

        assertEquals(SortedBag.of(), playerState.tickets());
        assertEquals(ticketList, playerState1.tickets());
    }

    @Test
    public void withAddedTicketsTest(){
        //player's tickets
        List<Ticket> tickets = List.of(ticketFromBE, ticketBalBer, getTicketFromDE);
        SortedBag<Ticket> ticketList = SortedBag.of(tickets);

        //player's cards
        List<Card> idk2 = List.of(Card.BLUE, Card.BLACK,Card.VIOLET, Card.RED, Card.ORANGE);
        SortedBag<Card> cards2 = SortedBag.of(idk2);

        //player's routes
        List<Route> routes = List.of(ChMap.routes().get(0), ChMap.routes().get(4),ChMap.routes().get(7));

        //player's state
        PlayerState playerState = new PlayerState(ticketList, cards2, routes);

        //ajout des billets ( +3 billets )
        Ticket ticket1 = ChMap.tickets().get(34);
        Ticket ticket2 = ChMap.tickets().get(30);
        Ticket ticket3 = ChMap.tickets().get(20);
        List<Ticket> ticketsList = List.of(ticket1, ticket2, ticket3);
        SortedBag<Ticket> ticketss = SortedBag.of(ticketsList);

        PlayerState newPlayerState = playerState.withAddedTickets(ticketss);

        SortedBag<Ticket> totalTickets = ticketList.union(ticketss);
        assertEquals(totalTickets, newPlayerState.tickets());

        //ajout de 0 billet
        PlayerState newPlayerState2 = playerState.withAddedTickets(SortedBag.of());
        assertEquals(ticketList, newPlayerState2.tickets());

    }

    @Test
    public void cardsTest(){
        //player's cards
        List<Card> idk2 = List.of(Card.BLUE, Card.BLACK,Card.VIOLET, Card.RED, Card.ORANGE);
        SortedBag<Card> cards2 = SortedBag.of(idk2);

        PlayerState playerState = new PlayerState(SortedBag.of(), cards2, List.of());
        assertEquals(cards2, playerState.cards());

        //no cards
        PlayerState playerState2 = new PlayerState(SortedBag.of(), SortedBag.of(), List.of());
        assertEquals(SortedBag.of(), playerState2.cards());
    }

    @Test
    public void withAddedCardTest(){
        List<Card> idk2 = List.of(Card.BLUE, Card.BLACK, Card.VIOLET, Card.RED);
        SortedBag<Card> cards2 = SortedBag.of(idk2);
        PlayerState playerState = new PlayerState(SortedBag.of(), cards2, List.of());
        PlayerState newPlayerState = playerState.withAddedCard(Card.WHITE);
        SortedBag<Card> totalCards = cards2.union(SortedBag.of(Card.WHITE));

        assertEquals(totalCards, newPlayerState.cards());

    }

    @Test
    public void withAddedCardsTest(){
        List<Card> idk2 = List.of(Card.BLUE, Card.BLACK, Card.VIOLET, Card.RED);
        SortedBag<Card> cards2 = SortedBag.of(idk2);
        PlayerState playerState = new PlayerState(SortedBag.of(), cards2, List.of());
        PlayerState newPlayerState = playerState.withAddedCards(SortedBag.of(2, Card.BLUE, 1, Card.WHITE));
        SortedBag<Card> totalCards = cards2.union(SortedBag.of(2, Card.BLUE, 1, Card.WHITE));

        assertEquals(totalCards, newPlayerState.cards());
    }

    @Test
    public void canClaimRouteTest(){
        //Berne - Interlaken
        //list of cards : 1) 3 blue
        PlayerState playerState = new PlayerState(SortedBag.of(), SortedBag.of(3, Card.BLUE), List.of());
        assertTrue(playerState.canClaimRoute(ChMap.routes().get(15)));

        PlayerState playerState1 = new PlayerState(SortedBag.of(), SortedBag.of(), List.of());
        assertFalse(playerState1.canClaimRoute(ChMap.routes().get(15)));

        List<Route> routes = new ArrayList<>();
        for (int i = 0; i<18; i++){
            routes.add(ChMap.routes().get(i));
        }
        PlayerState playerState3 = new PlayerState(SortedBag.of(), SortedBag.of(3, Card.BLUE), routes);
        assertFalse(playerState3.canClaimRoute(ChMap.routes().get(15)));


        //Berne - Lucerne
        //list of cards : 1) 4 Blue 2) 4 yellow 3)4  white
        PlayerState playerState2 = new PlayerState(SortedBag.of(), SortedBag.of(4, Card.WHITE), List.of());
        assertTrue(playerState2.canClaimRoute(ChMap.routes().get(17)));

        PlayerState playerState7 = new PlayerState(SortedBag.of(), SortedBag.of(4, Card.YELLOW), List.of());
        assertTrue(playerState7.canClaimRoute(ChMap.routes().get(17)));

        //tunnel : Interlaken - Brigue
        //list of cards : 1) 1 loco, 1white 2) 2 white 3) 2 loco
        PlayerState playerState4 = new PlayerState(SortedBag.of(), SortedBag.of(1, Card.LOCOMOTIVE, 1, Card.WHITE), List.of());
        assertTrue(playerState4.canClaimRoute(ChMap.routes().get(20)));

        PlayerState playerState5 = new PlayerState(SortedBag.of(), SortedBag.of(2, Card.WHITE), List.of());
        assertTrue(playerState5.canClaimRoute(ChMap.routes().get(20)));

        PlayerState playerState6 = new PlayerState(SortedBag.of(), SortedBag.of(2, Card.LOCOMOTIVE), List.of());
        assertTrue(playerState6.canClaimRoute(ChMap.routes().get(20)));

    }

    @Test
    public void possibleClaimCardsTest(){
        //Throws exception when player doesn't have enough cars to take the route
        List<Route> routes = new ArrayList<>();
        for (int i = 0; i<18; i++){
            routes.add(ChMap.routes().get(i));
        }
        PlayerState playerState = new PlayerState(SortedBag.of(), SortedBag.of(3, Card.BLUE), routes);
        assertThrows(IllegalArgumentException.class, ()->{
            playerState.possibleClaimCards(ChMap.routes().get(20)); //route bern-interlaken

        });

        //retourne la liste de tous les ensembles de cartes que le joueur pourrait
        //utiliser pour prendre possession de la route donnée (returns a sortedbag)

        //Route : Berne-Lucerne
        List<Card> cardsList = new ArrayList<>();
        for (int i = 1; i<5; i++){
            for(Card card : Card.CARS){
                cardsList.add(card);
            }
        }
        SortedBag<Card> cardSortedBag = SortedBag.of(cardsList);

        List<SortedBag<Card>> sortedCards = new ArrayList<>();
        for(Card card : Card.CARS){
            sortedCards.add(SortedBag.of(4, card));
        }

        //SortedBag<Card> cards = SortedBag.of(cardsList);
        //List<SortedBag<Card>> listOfCards = List.of(SortedBag.of(cardsList));
        PlayerState playerState1 = new PlayerState(SortedBag.of(), cardSortedBag, List.of());
        assertEquals(sortedCards, playerState1.possibleClaimCards(ChMap.routes().get(17)));

    }

    @Test
    public void possibleClaimCardsTestForPlayerHasOnlySomeOfTheCards(){
        List<Card> cardsList = new ArrayList<>();
        List<Card> cars = List.of(Card.VIOLET, Card.BLUE, Card.WHITE);
        for (int i = 1; i<5; i++){
            for(Card card : cars){
                cardsList.add(card);
            }
        }
        SortedBag<Card> cardSortedBag = SortedBag.of(cardsList);

        List<SortedBag<Card>> sortedCards = new ArrayList<>();
        for(Card card : cars){
            sortedCards.add(SortedBag.of(4, card));
        }

        PlayerState playerState1 = new PlayerState(SortedBag.of(), cardSortedBag, List.of());
        assertEquals(sortedCards, playerState1.possibleClaimCards(ChMap.routes().get(17))); //route Berne-Lucerne
    }

    @Test
    public void possibleAdditionalCardsTest(){
        //List<SortedBag<Card>> possibleAdditionalCards(int additionalCardsCount, SortedBag<Card> initialCards, SortedBag<Card> drawnCards)
        //qui retourne la liste de tous les ensembles de cartes que le joueur pourrait utiliser pour s'emparer d'un tunnel,
        // trié par ordre croissant du nombre de cartes locomotives, sachant qu'il a initialement posé les cartes initialCards,
        // que les 3 cartes tirées du sommet de la pioche sont drawnCards,
        // et que ces dernières forcent le joueur à poser encore additionalCardsCount cartes;
        // lève IllegalArgumentException si le nombre de cartes additionnelles n'est pas compris entre 1 et 3 (inclus),
        // si l'ensemble des cartes initiales est vide ou contient plus de 2 types de cartes différents,
        // ou si l'ensemble des cartes tirées ne contient pas exactement 3 cartes,





    }


}
