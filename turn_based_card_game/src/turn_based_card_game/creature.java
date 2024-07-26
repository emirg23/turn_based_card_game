package turn_based_card_game;

public class creature extends card{ // only creatures can attack and receive damage

	int attackDamage = 0; //only creatures have attack damage
	int health = 0; //only creatures have health

	creature(int cost, String description, int uniqueness, String name,int attackDamage, int health){
		super(cost, description, uniqueness, name);
		this.attackDamage = attackDamage;
		this.health = health;
	}
	public String toString() {
		return super.toString() +"\nattack damage: "+ this.attackDamage+"\nhealth: " + this.health;
	}
}