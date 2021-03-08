package ch.epfl.tchu.game;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ch.epfl.tchu.game.src.ch.epfl.tchu.gui.Info;

public class InfoTest {
	
	Info chris = new Info("Chris");
	
	@Test
	public void testCardName() {
		assertEquals("blanche", Info.cardName(Card.WHITE, 1));
	}
	
	@Test
	public void TestWillPlayFirst() {
		assertEquals("Chris jouera en premier.\n\n",
				     chris.willPlayFirst());
	}
	
	@Test 
	public void TestKeptTickets() {
		assertEquals("Chris a gardé 3 billets.\n",
				     chris.keptTickets(3));
	}
	
	@Test 
	public void Testdraw() {
		List<String> names = new ArrayList<>();
		names.add("James");
		names.add("Chris");
		assertEquals("\nJames et Chris sont ex æqo avec 45 points !\n",
				    Info.draw(names, 45));
	}
	
	@Test
	public void TestCanPlay() {
		assertEquals("\nC'est à Chris de jouer.\n",
				     chris.canPlay());
	}
	
	@Test
	public void TestDrewTickets() {
		assertEquals("Chris a tiré 4 billets...\n",
				      chris.drewTickets(4));
	}
	
	@Test
	public void TestDrewBlindCard() {
		assertEquals("",);
	}
	
	

}
