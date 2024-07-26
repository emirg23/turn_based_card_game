package turn_based_card_game;

public class card {
	
	int cost = 0;//card's price
	String description = "";//speciality of card's if it has
	int uniqueness = 0;// from 1 to 5, uniqueness group of the card
	String name = "";// name of the card

	
	card(int cost, String description, int uniqueness, String name){
		this.cost = cost;
		this.description = description;
		this.uniqueness = uniqueness;
		this.name = name;
	}

	
	public String toString() {
		if(this.description=="") 
			return "\nname: "+this.name+"\ncost: "+this.cost;
	
		else
			return "\nname: "+this.name+"\ncost: "+this.cost+"\nspeciality: " + this.description;
	
	}

}