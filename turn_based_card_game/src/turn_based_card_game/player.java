package turn_based_card_game;
import java.util.List;
import java.util.ArrayList;
public class player { //the main 2 players that draw cards.

	int mana = 0; // max mana goes up by 1 every tour and resets every tour beginning to max
	int health = 30; 
	card deck[] = new card[9]; // every player has 9 spots for cards
	String name = ""; // player's name

	player(String name){
		this.name = name;
	}

//players can get attacked by cards only if player dont have any creature card left
	public String toString() {
		return this.name;
	}
}
