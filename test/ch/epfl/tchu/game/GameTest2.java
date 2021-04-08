package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.gui.Info;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GameTest2 {

    @Test
    public void playTest1(){
        TestPlayer player1 = new TestPlayer(2, ChMap.routes());
        TestPlayer player2 = new TestPlayer(4, ChMap.routes());
        Map<PlayerId, Player> players = new EnumMap<>(PlayerId.class);
        players.put(PlayerId.PLAYER_1, player1);
        players.put(PlayerId.PLAYER_2, player2);

        Map<PlayerId, String> playerNames = new EnumMap<>(PlayerId.class);
        playerNames.put(PlayerId.PLAYER_1, "Ari");
        playerNames.put(PlayerId.PLAYER_2, "Fanny");

        List<Ticket> tickets = ChMap.tickets();
        SortedBag<Ticket> ticketSortedBag = SortedBag.of(tickets);

        Game.play(players, playerNames, ticketSortedBag, new Random());



        int totalCards = player1.ownState.cards().size() + player2.ownState.cards().size() + player1.gameState.cardState().totalSize();
        System.out.println("Total number of cards : "+totalCards);
        System.out.println("player1 cards: "+player1.ownState.cards().size());
        System.out.println("player2 cards: "+player2.ownState.cards().size());
        System.out.println(player1.gameState.cardState().totalSize());
        System.out.println(player2.gameState.cardState().totalSize());
        if (totalCards != 110){
            throw new Error("There's not the right number of cards !");
        }
    }
    


    private static final class TestPlayer implements Player {
        private static final int TURN_LIMIT = 1000;

        private final Random rng;
        // Toutes les routes de la carte
        private final List<Route> allRoutes;

        //counters
        private int receiveInfoCounter;
        private int turnCounter;
        private int updateStateCounter;
        private int initPlayerCounter;

        private PlayerState ownState;
        private PublicGameState gameState;

        // Lorsque nextTurn retourne CLAIM_ROUTE
        private Route routeToClaim;
        private SortedBag<Card> initialClaimCards;

        private PlayerId ownId;
        private String playerName;

        private String infoToReceive;

        private SortedBag<Ticket> distributedTickets;



        public TestPlayer(long randomSeed, List<Route> allRoutes) {
            this.rng = new Random(randomSeed);
            this.allRoutes = List.copyOf(allRoutes);
            this.turnCounter = 0;
            this.receiveInfoCounter = 0;
            this.updateStateCounter = 0;
            this.initPlayerCounter = 0;
        }

        @Override
        public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
            initPlayerCounter += 1;
            if (initPlayerCounter> 1){
                throw new Error("the player was initialized twice...");
            }
            this.ownId = ownId;
            this.playerName = playerNames.get(ownId);
        }

        @Override
        public void receiveInfo(String info) {
            receiveInfoCounter += 1;
            if (turnCounter > receiveInfoCounter)
                throw new Error("The number of infos communicated is smaller than the number of turns!");

            this.infoToReceive = info;
            System.out.println(info);

        }

        @Override
        public void updateState(PublicGameState newState, PlayerState ownState) {
            updateStateCounter += 1;
            this.gameState = newState;
            this.ownState = ownState;
            System.out.println("Number of times the game has been updated: " + updateStateCounter);

            //int totalCars = ownState.carCount();
            //System.out.println("Total number of cars for 1 player: " + totalCars);

            int totalCards = ownState.cards().size() + gameState.cardState().totalSize(); //1) 4 the cards he has in his hand, 2) 102
            System.out.println("Total number of cards in the hand of "+ playerName +": " + ownState.cards().size() + " " + ownState.cards());

            System.out.println("Total number of cards for the game (without the cards in the hands) : " + gameState.cardState().totalSize()+"\n");
        }

        @Override
        public void setInitialTicketChoice(SortedBag<Ticket> tickets) {
            this.distributedTickets = tickets;

        }

        @Override
        public SortedBag<Ticket> chooseInitialTickets() {
            return SortedBag.of(Deck.of(distributedTickets, rng).topCards(3));
        }

        @Override
        public TurnKind nextTurn() {
            //total cars 40 each so 80
            //int totalCars = ownState.carCount();
            //System.out.println("total number of cars for " + playerName+ ": " + totalCars);

            //total cards for the game does not include the cards of the players hands
            int totalCards = gameState.cardState().totalSize();
            System.out.println("Total number of cards for this new turn : "+totalCards+", discards: "+gameState.cardState().discardsSize()+", deck: "+gameState.cardState().deckSize()+", faceupcards: "+gameState.cardState().faceUpCards().size());

            turnCounter += 1;
            System.out.println("----------------------------------NEW TURN ("+turnCounter+")--------------------------------------------\n");
            if (turnCounter > TURN_LIMIT)
                throw new Error("Trop de tours joués !");

            // Détermine les routes dont ce joueur peut s'emparer
            List<Route> tempRoutes = new ArrayList<>();
            for (Route r : allRoutes){
                if(ownState.canClaimRoute(r)){
                    tempRoutes.add(r);
                }
            }
            tempRoutes.removeAll(gameState.claimedRoutes());
            List<Route> claimableRoutes = tempRoutes;

            int randNum = rng.nextInt(10);

            if (claimableRoutes.isEmpty()) {
                return TurnKind.DRAW_CARDS;
            } else if (randNum == 5){
                return TurnKind.DRAW_TICKETS;
            } else {
                int routeIndex = rng.nextInt(claimableRoutes.size());
                Route route = claimableRoutes.get(routeIndex);
                List<SortedBag<Card>> cards = ownState.possibleClaimCards(route);

                routeToClaim = route;
                System.out.println("The route to claim is: " + routeToClaim.id());
                initialClaimCards = cards.get(0);
                System.out.println("The initial claim cards are: " + initialClaimCards+"\n");

                return TurnKind.CLAIM_ROUTE;
            }


        }


        @Override
        public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {
            return SortedBag.of(Deck.of(options, rng).topCards(3));
        }

        @Override
        public int drawSlot() {
            Random rand = new Random();
            int randomNum = rand.nextInt(5)-1;
            if (randomNum == -1){
                System.out.println("The player has decided to draw a card from the deck");
            } else {
                System.out.println("The player has decided to draw a card from the face up cards");
            }
            return randomNum;
        }

        @Override
        public Route claimedRoute() {
            return routeToClaim;
        }

        @Override
        public SortedBag<Card> initialClaimCards() {
            List<SortedBag<Card>> cards = ownState.possibleClaimCards(routeToClaim);
            return cards.get(0);
        }

        @Override
        public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) {
            if (options.isEmpty()){
                System.out.println("There is no need for additional cards to seize the tunnel");
            }
            return options.get(0);
        }
    }


}


