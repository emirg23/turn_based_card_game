package turn_based_card_game;

public class spell extends card{ // spells are unattackable cards

	spell(int cost,String description,int uniqueness, String name){
		super(cost, description, uniqueness, name);
	}
	
}
