package ch.epfl.tchu.game;


import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.*;

/**
 * The PlayerState class from the ch.epfl.tchu.game package, public, final, and immutable.
 * Modelizes the player's State.
 * Represents the complete state of a player.
 * It inherits from PublicPlayerState.
 * It offers a unique public constructor and a static construction method.
 *
 * @author Yasmin Ben Rahhal (329912)
 * @author Sara Anejjar (329905)
 */

public final class PlayerState extends PublicPlayerState {

    /**
     * Constructs the initial state of a player to whom the given initial cards were dealt,
     * the player does not yet have any tickets, and has not taken any route.
     *
     * @param initialCards (SortedBag<Card>) the initial distributed cards
     * @return the initial Player's state
     * @throws IllegalArgumentException if the number of initial cards is not equal to 4
     */
    public static PlayerState initial(SortedBag<Card> initialCards) {
        Preconditions.checkArgument(initialCards.size() == 4);
        return new PlayerState(SortedBag.of(), initialCards, List.of());
    }


    private final SortedBag<Ticket> tickets;
    private final SortedBag<Card> cards;

    /**
     * Public Constructor of the state of a player owning the given tickets, cards and routes.
     *
     * @param tickets (SortedBag<Ticket>) the tickets owned by the player
     * @param cards   (SortedBag<Card>) the cards owned by the player
     * @param routes  (List<Route>) the roads owned by the player
     */
    public PlayerState(SortedBag<Ticket> tickets, SortedBag<Card> cards, List<Route> routes) {
        super(tickets.size(), cards.size(), routes);
        this.tickets = tickets;
        this.cards = cards;
    }


    /**
     * Returns the tickets in the player's hand.
     *
     * @return (SortedBag<Ticket>) The tickets owned by the player
     */
    public SortedBag<Ticket> tickets() {
        return tickets;
    }

    /**
     * Returns the cards in the player's hand
     *
     * @return (SortedBag<Card>) The cards owned by the player
     */
    public SortedBag<Card> cards() {
        return cards;
    }

    /**
     * Returns the list of all sets of ordered cards the player could use to take possession of the given route.
     *
     * @param route (Route) The road to claim
     * @return (List <SortedBag<Card>>) All the possible sets of cards to use to claim the road route
     * @throws IllegalArgumentException If the player doesn't have enough wagons to claim the road route
     */
    public List<SortedBag<Card>> possibleClaimCards(Route route) {
        Preconditions.checkArgument(carCount() >= route.length());
        List<SortedBag<Card>> possiblesClaims = route.possibleClaimCards();
        List<SortedBag<Card>> possiblePlayer = new ArrayList<>();
        for (SortedBag<Card> pos : possiblesClaims) {
            boolean contains = true;
            List<Card> playerCards = cards.toList();
            for (Card c : pos) {
                if (!playerCards.contains(c))
                    contains = false;
                playerCards.remove(c);
            }
            if (contains)
                possiblePlayer.add(pos);
        }
        return possiblePlayer;
    }

    /**
     * Returns the list of all the sets of cards that the player could use to claim a tunnel,
     * sorted in ascending order of the number of locomotive cards,knowing that the initialCards
     * and the 3 drawnCards from the deck force him to draw additionalCardsCount cards.
     *
     * @param additionalCardsCount (int) the additional Cards for the player to add
     * @param initialCards         (SortedBag<Card>) the cards used by the player to claim a route
     * @param drawnCards           (SortedBag<Card>) the cards drawn by the player after claiming a route
     * @return (List <SortedBag<Card>>) The list of Sorted additional cards the player can use to claim the route
     * @throws IllegalArgumentException if additionalCardsCount is not between 1 and 3 (included)
     *                                  if initialCards is empty
     *                                  if initialCards contains more than two different cards
     *                                  if drawnCards doesn't contain exactly 3 cards
     */
    public List<SortedBag<Card>> possibleAdditionalCards(int additionalCardsCount, SortedBag<Card> initialCards, SortedBag<Card> drawnCards) {
        Preconditions.checkArgument(additionalCardsCount >= 1 && additionalCardsCount <= 3);
        Preconditions.checkArgument(!initialCards.isEmpty());
        Set<Card> initial = initialCards.toSet();
        Preconditions.checkArgument(initial.size() <= 2);
        Preconditions.checkArgument(drawnCards.size() == 3);

        SortedBag<Card> cardsWithoutInitials = cards.difference(initialCards);
        SortedBag.Builder<Card> canUse = new SortedBag.Builder<>();
        for (Card c : cardsWithoutInitials) {
            if (c.equals(Card.LOCOMOTIVE) || initialCards.contains(c))
                canUse.add(c);
        }
        List<SortedBag<Card>> all = new ArrayList<>();
        if (canUse.size() >= 0 && additionalCardsCount <= canUse.size()) {
            Set<SortedBag<Card>> allSubSets = canUse.build().subsetsOfSize(additionalCardsCount);
            all.addAll(allSubSets);
            all.sort(Comparator.comparingInt(cs -> cs.countOf(Card.LOCOMOTIVE)));
        }
        return all;
    }

    /**
     * Returns an identical state to this, except that the player also has the given tickets added to his hand
     *
     * @param newTickets (SortedBag<Ticket) The tickets to add to this player
     * @return (PlayerState) Same as this PlayerState with the newTickets added to it
     */
    public PlayerState withAddedTickets(SortedBag<Ticket> newTickets) {
        SortedBag.Builder<Ticket> addedTickets = new SortedBag.Builder<>();
        addedTickets.add(tickets);
        addedTickets.add(newTickets);
        return new PlayerState(addedTickets.build(), cards, routes());
    }

    /**
     * Returns an identical state to this, except that the player also has the given card added to his hand.
     *
     * @param card (Card) the card to add to the list of cards owned by the player
     * @return (PlayerState) Same as this PlayerState with a card added to its list of cards
     */
    public PlayerState withAddedCard(Card card) {
        SortedBag.Builder<Card> newCards = new SortedBag.Builder<>();
        newCards.add(cards);
        newCards.add(card);
        return new PlayerState(tickets, newCards.build(), routes());
    }

    /**
     * Returns an identical state to this, except that the player also has the given cards added to his hand.
     *
     * @param additionalCards (SortedBag<Card>) the additional cards to add
     *                        to the list of cards owned by the player
     * @return (PlayerState) Same as this PlayerState with the additional cards added to its list of cards
     */
    public PlayerState withAddedCards(SortedBag<Card> additionalCards) {
        SortedBag.Builder<Card> newCards = new SortedBag.Builder<>();
        newCards.add(cards);
        newCards.add(additionalCards);
        return new PlayerState(tickets, newCards.build(), routes());
    }

    /**
     * Returns an identical state to the receiver,
     * except that the player has claimed the given route using the given cards claimCards
     *
     * @param route      (Route) the road to add to this PlayerState's roads
     * @param claimCards (The cards used by this player to claim the road)
     * @return (PlayerState) Same as this PlayerState with the additional route added to its routes
     * and with the claimCards removed from its list of cards
     */
    public PlayerState withClaimedRoute(Route route, SortedBag<Card> claimCards) {
        List<Route> newRoutes = new ArrayList<>(routes());
        newRoutes.add(route);
        return new PlayerState(tickets, cards.difference(claimCards), newRoutes);
    }

    /**
     * Returns true iff the player can claim the given route,
     * i.e. if he has enough cars left and if he has the necessary claimCards in hand.
     *
     * @param route (Route) The road to check if it can be claimed or not
     * @return true if route can be claimed
     */
    public boolean canClaimRoute(Route route) {
        return carCount() >= route.length() && !possibleClaimCards(route).isEmpty();
    }

    /**
     * Returns the number of points — possibly negative — obtained by the player thanks to his tickets.
     *
     * @return (int) The points earned by the player with its tickets
     */
    public int ticketPoints() {
        List<Integer> iDs = new ArrayList<>();
        for (Route r : routes()) {
            iDs.add(r.station1().id());
            iDs.add(r.station2().id());
        }
        StationPartition.Builder builder =
                new StationPartition.Builder(iDs.isEmpty() ? 0 : Collections.max(iDs) + 1);
        for (Route r : routes())
            builder.connect(r.station1(), r.station2());

        StationPartition connectivity = builder.build();

        int points = 0;
        for (Ticket t : tickets)
            points += t.points(connectivity);

        return points;
    }

    /**
     * Returns all of the points obtained by the player at the end of the game,
     * namely the sum of the points earned with the claimPoints and ticketPoints.
     *
     * @return (int) All the points earned by the player at the end of the game
     */
    public int finalPoints() {
        return ticketPoints() + claimPoints();
    }
}
