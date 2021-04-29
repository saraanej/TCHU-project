package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Random;


public final class TestServer {
    public static void main(String[] args) throws IOException {
        System.out.println("Starting server!");
        try (ServerSocket serverSocket = new ServerSocket(5108);
             Socket socket = serverSocket.accept()) {
            Player playerProxy = new RemotePlayerProxy(socket);
            var playerNames = Map.of(PlayerId.PLAYER_1, "Ada",
                    PlayerId.PLAYER_2, "Charles");
            playerProxy.initPlayers(PlayerId.PLAYER_1, playerNames);
            System.out.println("sentInit");
            playerProxy.updateState(GameState.initial(SortedBag.of(ChMap.tickets()),new Random()),
                    PlayerState.initial(SortedBag.of(4,Card.BLUE)));
            System.out.println("sentUpdate");

            //initial ticket choice
           List<Ticket> tick = List.of(ChMap.tickets().get(0),
                    ChMap.tickets().get(1), ChMap.tickets().get(2), ChMap.tickets().get(3),ChMap.tickets().get(4));
            SortedBag<Ticket> tickets = SortedBag.of(tick);

            playerProxy.setInitialTicketChoice(tickets);
            SortedBag<Ticket> chosen = playerProxy.chooseInitialTickets();
            System.out.println(chosen);
            System.out.println(tickets.contains(chosen));
        }
        System.out.println("Server done!");
    }
}