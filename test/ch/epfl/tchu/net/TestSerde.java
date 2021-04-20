package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestSerde {
    @Test
    public void profTest(){
        Serde<Integer> intSerde = Serde.of(
                i -> Integer.toString(i),
                Integer::parseInt);
        assertEquals("2021", intSerde.serialize(2021));
        assertEquals(2021, intSerde.deserialize("2021"));


        SortedBag<Card> c = SortedBag.of(4,Card.BLUE);
        for (Card card: c) {
            System.out.println(card);
        }
    }

}
