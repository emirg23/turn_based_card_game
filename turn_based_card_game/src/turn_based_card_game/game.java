package turn_based_card_game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class game {
	
	static player player[] = new player[2]; // there is only 2 players
	
	public static Scanner input = new Scanner(System.in);
	
	static int tourNumber = 1;
	
	static Random random = new Random(); // for random rarity
	
	static List<Integer> usedRows = new ArrayList<Integer>(); // making sure the player does not use the same exact card more than one time
	
	//cards
	
	//cost 1 
	static spell miss = new spell(1,"empty",1,"miss");
	
	static creature scarecrow = new creature(1,"",2,"scarecrow",0,2);
	
	static creature minion = new creature(1,"",3,"minion",1,1);
	
	static creature cannibal = new creature(1,"",4,"cannibal",2,1);
	
	static creature bleeder = new creature(1,"every tour he lives, attack damage goes up by 1",5,"bleeder",1,2);
		
	//cost 2
	static spell stick = new spell(2,"1 damage to your own, 2 damage to your enemy card",1,"stick");
	
	static spell quarters = new spell(2,"turn one of your cards to soldier",2,"quarters");
	
	static spell carnival = new spell(2,"create 3 minions",3,"carnival");
	
	static spell fireball = new spell(2,"give 2 damage to a unit",4,"fire ball");
	
	static creature soldier = new creature(2,"",5,"soldier",3,3);
	
	//cost 3
	static creature sword = new creature(3,"",1,"sword",5,2);
	
	static creature crib = new creature(3,"every tour the crib stands, 2 minion spawns.",2,"crib",0,6);
	
	static creature wolf = new creature(3,"",3,"wolf",4,4);
	
	static creature chef = new creature(3,"every tour end chef stands, the player gains 3 health",4,"chef",1,4);
	
	static spell training = new spell(3,"every creature in your deck gains 3 attack damage",5,"training");
	
	//cost 4
	static spell armor = new spell(4,"makes one of your cards gain 10 health",1,"armor");
	
	static spell meteor = new spell(4,"makes any card you choose dead",2,"meteor");
	
	static spell fireballs = new spell(4,"summons 3 fireballs",3,"fireballs");
	
	static creature golem = new creature(4,"makes it's health 2x every tour. (if the health passes 50, the game ends)",4,"golem",2,5);
	
	static spell fullmoon = new spell(4,"summons 3 wolfes.",5,"fullmoon");
	
	//cost 5
	static creature knight = new creature(5,"",1,"knight",6,6);
	
	static creature asya = new creature(5,"every tour asya starts the tour with the same hp as tour number",2,"asya",6,4);
	
	static spell apocalypse = new spell(5,"destroy every card in other players hand including spells",3,"apocalypse");
	
	static creature ditto = new creature(5,"every tour ditto stands, ditto clones himself",4,"ditto",2,4);
	
	static creature emirg = new creature(5,"regens the player 5, kills every card the summoning player has and comes with 4 soldiers",5,"emirg",15,15);
	
	//deck of cards by costs ----> deck.get( cost*5  +  rarity )
	
	static List<Object> deck = new ArrayList<>(Arrays.asList(
			miss, scarecrow, minion, cannibal, bleeder,
            stick, quarters, carnival, fireball, soldier,
            sword, crib, wolf, chef, training,
            armor, meteor, fireballs, golem, fullmoon,
            knight, asya, apocalypse, ditto, emirg
    ));	
	
	//
	
	
	public static void main(String[] args) {
		
		
		create2Players();
		
		while(player[0].health > 0 && player[1].health > 0) { // the game itself
			
			bothPlayersManaEqualsToTourNumber(); //every tour player mana starts with being equal to tour number
		
			int a = (tourNumber-1)%2; // a is element for player[a] changing between 0 and 1 every tour in order starting with 0
			
			
				buyCard(player[0]);
				
				buyCard(player[1]);
				
				// attack phase
				
				System.out.println("this tour, "+player[a]+" is attacking first.");
				
				for(int i = 0; i<2; i++) {
					
					int k = (a+i)%2;// k is element for player[k] changing between 0 and 1 in this for() block
 				

					while(true) {
						
						System.out.println(deckToString(player[k].name,player[k].deck)+
						"select a card to use\ntype '9' for ending");
			
						int selectedCardRow = inputIntervalInt(0,9);
						
						if(selectedCardRow==9) { // passing the current players attack tour
							usedRows.clear();// cleaning usedRows so next player can use the slots that has been used
							break;
						}
						
						if(player[k].deck[selectedCardRow]==null) // if selected slot is null
							System.out.println("invalid choice\n");
						
						else if(usedRows.contains(selectedCardRow))  // trying to use the card that has been already used
							System.out.println("you already used this card");
						
						else if(player[k].deck[selectedCardRow].getClass()==spell.class) // SPELLS AREA 
							checkWhichSpell(player, k, player[k].deck, selectedCardRow);
						
						else { // CREATURE AREA
							
							usedRows.add(selectedCardRow); // used slot getting saved to list
							
							System.out.println("you selected " + player[k].deck[selectedCardRow].name + 
							"\nattack damage: "+ ( (creature) player[k].deck[selectedCardRow]).attackDamage);
							
							if(!doesThisDeckContainCreature(player[(k+1)%2].deck)) { // if the opponent deck is not attackable (does not contain any creatures)
								
								System.out.println("\nsince the player has no cards for defence, you are attacking the player!");
								
								player[(k+1)%2].health = player[(k+1)%2].health - ((creature)player[k].deck[selectedCardRow]).attackDamage;// taking away attack damage value of the card from player health
								
								System.out.println("\n"+ player[(k+1)%2] +"'s new health = "+ player[(k+1)%2].health);
								
									
									
							}
							
							else { // firstly attacking cards since opponent has cards 
								
								while(true) {
									
									System.out.println(deckToStringWithHP(player[(k+1)%2].name,player[(k+1)%2].deck)+ 
									"\nwhat card of your opponent do you wanna attack?");
							
									int targetCard = inputIntervalInt(0,8);
								
									if(player[(k+1)%2].deck[targetCard]==null || player[(k+1)%2].deck[targetCard].getClass()==spell.class) // null / spell
										System.out.println("invalid choice");
								
								
									else {	// creature
										attack((creature)player[k].deck[selectedCardRow],player[(k+1)%2].deck, targetCard);
										break;
									}
								
								}
							
							}
							
						}
					}
				}
			
				checkCreatureSpeciality(player);
				
				tourNumber++;
		
		}
		
		determineWinner(player);
		
	}
	
	
	//     METHODS
	
	
	public static void create2Players() {
		System.out.print("please input first player's name : ");
		player[0] = new player(input.nextLine());
		
		System.out.print("please input second player's name : ");
		player[1] = new player(input.nextLine());
	}
	
	
	public static void bothPlayersManaEqualsToTourNumber() {
		player[0].mana = tourNumber;
		player[1].mana = tourNumber;
	}
	
	
	public static void determineWinner(player player[]) {
		
		int winnerNumPlayer;
		
		// setting the winner (doesn't matter who got below 0 health points first, the lower one loses)
		if(player[0].health<player[1].health)
			winnerNumPlayer=1;
		
		else
			winnerNumPlayer=0;
		
		System.out.println("\n"+(player[winnerNumPlayer]).toString().toUpperCase() +" WON!!!!"); // winner announcement
		
	}
	
	
	public static void checkCreatureSpeciality(player player[]) {
		
		for(int i = 0; i<2; i++) { // i for player[0] and player[1] index
			
			
			for(int l = 0; l<9; l++) { // l for checking every spot in player deck
				
				if(!(player[i].deck[l]==null || player[i].deck[l].getClass()==spell.class))// it is a creature
					checkIfCreatureHasSpeciality(player, i, l);
				
				if(l==8)
					creatureDitto(player[i]);
				
				
			}
		
		
		}
	
	}
	

	public static void checkIfCreatureHasSpeciality(player player[],int playerIndex,int creatureIndex) {
		
		String x = player[playerIndex].deck[creatureIndex].name; // spell name == x
		
		if(x==bleeder.name) 
			creatureBleeder(player, playerIndex, creatureIndex);
		
		else if(x==crib.name)
			creatureCrib(player[playerIndex]);

		else if(x==chef.name)
			creatureChef(player[playerIndex]);

		else if(x==golem.name)
			creatureGolem(player, playerIndex, creatureIndex);

		else if(x==asya.name)
			creatureAsya(player[playerIndex], creatureIndex);

		
	}


	public static void drawSpecificSpell(spell spell, card deck[], int numberOfTimes) {
		
		for(int p = 0; p<numberOfTimes; p++) {
		
			for(int k = 0; k<9; k++) { // finding empty spot in deck and putting spell
			
				if(deck[k]==null) {
					deck[k] = spell;
					break;
				}
			}	
	
		}
	
	}

	
	public static void drawSpecificCreature(creature creature, card deck[], int numberOfTimes) {
	   
		for (int p = 0; p < numberOfTimes; p++) {
	       
			for (int k = 0; k < 9; k++) { // finding empty spot in deck and putting spell
	            
				if (deck[k] == null) {
	                deck[k] = copyCard(creature);
	                break;
	            }
	       
			}
		
		}
	
	}


	public static void pulledEmirg(player player) {
		
		player.health = player.health + 5;
		
		System.out.println("emirg healed the "+ player.name+" 5 points"
		+ "\nnew "+player.name+" health:"+player.health);
		
		System.out.println("emirg came with 4 soldiers of his and killed every creature besides them");
		
		for(int i = 0; i<9; i++) { // killing every creatures besides emirg and soldiers (if emirg/soldier was pulled recently, they are not gonna get killed)
				
				if(player.deck[i]==null); // protection from null exception
				
				else if(player.deck[i].getClass()==creature.class && !(player.deck[i].name==emirg.name || player.deck[i].name==soldier.name)) {
					player.deck[i]=null;
				}
				
			}
		
		drawSpecificCreature(emirg, player.deck, 1);
		
		drawSpecificCreature(soldier, player.deck, 4);
		
	}


	public static void creatureDitto(player player) {
		
		int dittoNumberPlayerHas = 0;  // we need to do this so ditto clones doesnt clone in the same tour leading to infinity
		
		for(int k = 0; k<9; k++) { // checking for dittos
			if(player.deck[k]!=null && player.deck[k].name==ditto.name)
				dittoNumberPlayerHas++;
		}
		
		if(dittoNumberPlayerHas>0) {
			
			int emptySpots = 0;
			
			for(int s = 0; s<9; s++) { // checking for how many empty spots are there
				
				if(player.deck[s]==null)
					emptySpots++;
				
			}
			
			if(emptySpots==0) 
				System.out.println("there is no empty spots in "+player.name+"'s deck to clone ditto");
			
			else if(emptySpots<dittoNumberPlayerHas) // example: 5 dittos, 3 empty spots, only 3 more clons of ditto
				System.out.println(player.name+"'s dittos cloned and now there are "+emptySpots+" more dittos");
		
			else // normal condition
				System.out.println(player.name+"'s dittos cloned and now there are "+dittoNumberPlayerHas+" more dittos");
			
			drawSpecificCreature(ditto, player.deck, dittoNumberPlayerHas);
		}
		dittoNumberPlayerHas = 0;
	}
	
	
	public static void creatureCrib(player player) {
		
		int emptySpots = 0;
		
		for(int s = 0; s<9; s++) { // checking for how many empty spots are there
			
			if(player.deck[s]==null)
				emptySpots++;
			
		}
		
		if(emptySpots==0);
		
		else if(emptySpots<2) // example: 2 crib spawns, 1 empty spots, only 1 more minion
			System.out.println("crib spawned "+emptySpots+" minions for "+player.name+"!");
	
		else // normal condition
			System.out.println("crib spawned 2 minions for "+player.name+"!");
		
		drawSpecificCreature(minion, player.deck, 2); // pulling 2 minions anyways if there is no spot, no problem
	

	}


	public static void creatureChef(player player) {
		
			player.health = player.health + 3; // chef heals the player 3 points every tour
			System.out.println(player.name+"'s chef cooked and healed the player 3 points.\nnew player health:"+player.health);	
		
	}


	public static void creatureAsya(player player, int creatureIndex) {

		((creature) player.deck[creatureIndex]).health =  tourNumber+1;// asya health point equals to playing tour number every tour it stands
		
		System.out.println(player.name+"'s asya health points equals to tour number."
		+ "\nnew asya health point:" + ((creature) player.deck[creatureIndex]).health);
		
	}


	public static void creatureGolem(player player[], int playerIndex, int creatureIndex) {
		
		int k = (playerIndex+1)%2; // opponent player index 
		
		((creature) player[playerIndex].deck[creatureIndex]).health =  ((creature) player[playerIndex].deck[creatureIndex]).health*2;// golems health point multiplies by 2 every tour it stands
		
		System.out.println(player[playerIndex].name+"'s golem got harder 2 times!"
		+ "\nnew golem health point:" + ((creature) player[playerIndex].deck[creatureIndex]).health);
		
		if(((creature) player[playerIndex].deck[creatureIndex]).health>50){ // golem reached over 50 health points
			
			System.out.println("golem met the condition of over 50 HPs and smashed "+player[k].name);
			player[k].health = 0;
		}
	}

	
	public static void creatureBleeder(player player[],int playerIndex, int creatureIndex) {
		
		((creature) player[playerIndex].deck[creatureIndex]).attackDamage++; // bleeders attack damage goes up by one every tour it stands
		
		System.out.println(player[playerIndex].name+"'s bleeder gained 1 attack damage."
		+ "\nnew bleeder attack damage:" + ((creature) player[playerIndex].deck[creatureIndex]).attackDamage);
		
	}

	
	public static void noCreaturesForSpell(spell spell,card deck[],int spellIndex, boolean itself) { // the cancel and refund of the used spell when you do not meet the condition for the spell
		
		if(itself) // the text for players own deck condition
		System.out.println("since you have no creatures, you cannot use "+spell.name);
		
		else // the text for opponent players deck condition
		System.out.println("since the opponent has no creatures, you cannot use "+spell.name);
		
		deck[spellIndex]=spell;
	
	}
	
	
	public static void spellAttackAbility(int damage, card[] defenderDeck, int defenderIndex, String spellName) { // attack ability for spells
	
		System.out.println("\n"+ spellName +"'s damage: "+damage+"\n"+
		defenderDeck[defenderIndex].name+"'s health: "+ ((creature)defenderDeck[defenderIndex]).health + "\n" + spellName + " has been used and");
		
		if(damage >= ((creature)defenderDeck[defenderIndex]).health){//enough damage to kill
			System.out.println(defenderDeck[defenderIndex].name +" has been killed.");
			defenderDeck[defenderIndex] = null;
		}
		
		else {//not enough damage to kill
			((creature)defenderDeck[defenderIndex]).health = ((creature)defenderDeck[defenderIndex]).health-damage;
			System.out.println("new health of "+ defenderDeck[defenderIndex].name +" is " + ((creature)defenderDeck[defenderIndex]).health +".");
		}
	}
	
	
	public static void checkWhichSpell(player player[],int playerIndex,card deck[],int spellIndex) { // comparing every spell to selected spell till it's found for its' method
		
		String x = deck[spellIndex].name; // spell name == x
		deck[spellIndex]=null; // we used the spell
		
		if(x==stick.name)
			spellStick(player, playerIndex, spellIndex);
		
		else if(x==quarters.name) 
			spellQuarters(player[playerIndex], deck, spellIndex);
		
		else if(x==carnival.name)
			spellCarnival(deck);
		
		else if(x==fireball.name)
			spellFireball(player,playerIndex,deck);
		
		else if(x==training.name)
			spellTraining(player[playerIndex]);
		
		else if(x==armor.name)
			spellArmor(player[playerIndex],spellIndex);
		
		else if(x==meteor.name)
			spellMeteor(player,playerIndex,spellIndex);
		
		else if(x==fireballs.name)
			spellFireballs(deck);
		
		else if(x==fullmoon.name)
			spellFullmoon(deck);
		
		else if(x==apocalypse.name)
			spellApocalypse(player,playerIndex);
		
	}
	
	
	public static void spellTraining(player player) {
		
		for(int i = 0; i<9;i++) { // searching deck for cards to make them gain 3 attack damages
			
			if(player.deck[i]!=null && player.deck[i].getClass()==creature.class){ // found creature
				
				((creature) player.deck[i]).attackDamage = ((creature) player.deck[i]).attackDamage + 3;
				
				System.out.println(player.name+"'s "+player.deck[i].name+" gained 3 attack damage\nnew attack damage:"+((creature)player.deck[i]).attackDamage);
			}
		
		}
	}
	
	
	public static void spellStick(player player[],int playerIndex,int spellIndex) {
		
		int k = (playerIndex+1)%2; // the opponent player index
		
		if(doesThisDeckContainCreature(player[playerIndex].deck)) { // the card stick requires friendly creature
			
			System.out.println(deckToStringWithHP(player[playerIndex].name,player[playerIndex].deck)+
			"select one of your creatures to give 1 damage");
			
			while(true) {
			
				int selectedStickTarget = inputIntervalInt(0,8);
				
				if(player[playerIndex].deck[selectedStickTarget]==null || player[playerIndex].deck[selectedStickTarget].getClass()==spell.class) // null/spell got selected
					System.out.println("invalid choice");		
				
				else {
					spellAttackAbility(1,player[playerIndex].deck,selectedStickTarget,stick.name);
					break;
				}
			
			}
			
			if(doesThisDeckContainCreature(player[k].deck)) { // checking if enemy got any creatures
				
				System.out.println(deckToStringWithHP(player[k].name, player[k].deck)+
				"select one of your opponents cards to make it lose 2 health points");
				
				while(true) {
					
					int selectedStickTarget = inputIntervalInt(0,8);
			
					if(player[k].deck[selectedStickTarget]==null || player[k].deck[selectedStickTarget].getClass()==spell.class) // null/spell got selected
						System.out.println("invalid choice");
			
					else { // creature
						spellAttackAbility(2,player[k].deck,selectedStickTarget,stick.name); // fireball has 2 damage
						break;	
					}
					
				}
				
			}
			else { // no creatures in opponent deck, so attacking the player 
				
				System.out.println("since the opponent has no creatures, you are attacking the player");
				
				player[k].health = player[k].health-2; // stick has 2 damage, attacking the player
				
				System.out.println("new "+player[k].name+" health:"+ player[k].health);
			}
			
		}
		else // no creatures in deck
			noCreaturesForSpell(stick,player[playerIndex].deck,spellIndex,true);
		
	}
	
	
	public static void spellQuarters(player player, card deck[], int spellIndex) {
		
		if(doesThisDeckContainCreature(player.deck)) {  // the card quarters transforms creatures into soldier so we need creatures
		
			System.out.println(deckToString(player.name,deck)+"select one of your cards to turn it to soldier");
		
			while(true) {
			
				int selectedQuarterTarget = inputIntervalInt(0,8);
			
				if(deck[selectedQuarterTarget]==null || deck[selectedQuarterTarget].getClass()==spell.class) // spell/null
					System.out.println("invalid choice");
			
				else { // creature
					System.out.println(deck[selectedQuarterTarget].name+" transformed into soldier!");
					deck[selectedQuarterTarget] = soldier;
					break;
				}
			}
		
		}
		else  // no creatures in deck
			noCreaturesForSpell(quarters,player.deck,spellIndex,true);
		
	}
	
	
	public static void spellCarnival(card deck[]) {
		
		drawSpecificCreature(minion, deck, 3); // 3 minions

	}
	
	
	public static void spellFireball(player player[], int playerIndex, card deck[]) {
		
		int k = (playerIndex+1)%2; // the opponent player index
		
		if(doesThisDeckContainCreature(player[k].deck)) { // checking opponent deck to see if it has creatures
		
		System.out.println(deckToStringWithHP(player[k].name, player[k].deck)+
		"select one of your opponents cards to make it lose 2 health points");
		
			while(true) {
			
				int selectedFireballTarget = inputIntervalInt(0,8);
		
				if(player[k].deck[selectedFireballTarget]==null || player[k].deck[selectedFireballTarget].getClass()==spell.class) // null/spell got selected
					System.out.println("invalid choice");
		
				else { // creature
					spellAttackAbility(2,player[k].deck,selectedFireballTarget,fireball.name); // fireball has 2 damage
					break;	
				}
				
			}
		
		}
		else { // no creatures in opponent deck, so attacking the player 
			System.out.println("since the opponent has no creatures, you are attacking the player");
			
			player[k].health = player[k].health-2; // fireball has 2 damage, attacking the player
			
			System.out.println("new "+player[k].name+" health:"+ player[k].health);
		}
	
	}
	
	
	public static void spellArmor(player player, int spellIndex) {
		
		if(doesThisDeckContainCreature(player.deck)) { //  the card armor gives 10 HPs to creatures, so we need creatures
			
		System.out.println(deckToStringWithHP(player.name,player.deck)+
		"select one of your cards to make it gain 10 health points");
		
		
			while(true) {
			
				int selectedQuarterTarget = inputIntervalInt(0,8);
			
				if(player.deck[selectedQuarterTarget]==null || player.deck[selectedQuarterTarget].getClass()==spell.class) // null/spell got selected
					System.out.println("invalid choice");
				
				else { // creature
					
					((creature)player.deck[selectedQuarterTarget]).health = ((creature)player.deck[selectedQuarterTarget]).health+10; // +10 health for the card
					
					System.out.println(player.deck[selectedQuarterTarget].name+" gained 10 more health points!"
					+ "\nnew "+player.deck[selectedQuarterTarget].name+" health:"+((creature)player.deck[selectedQuarterTarget]).health);
					
					break;
				}
				
			}
			
		}
		else  // no creatures in deck
			noCreaturesForSpell(armor,player.deck,spellIndex,true);	
		
			
	}
	
	
	public static void spellMeteor(player player[], int playerIndex,int spellIndex) {
		
		int k = (playerIndex+1)%2; // the opponent player index
		
		if(doesThisDeckContainCreature(player[k].deck)) { // checking opponent deck to see if it has creatures
			
			System.out.println(deckToString(player[k].name,player[k].deck)+"select one of your opponents cards to destroy");
			
			while(true) {
				
				int selectedMeteorTarget = inputIntervalInt(0,8);
			
				if(player[k].deck[selectedMeteorTarget]==null || player[k].deck[selectedMeteorTarget].getClass()==spell.class) {
					System.out.println("invalid choice");
				}
				else {
					System.out.println(player[k].deck[selectedMeteorTarget].name+" got destroyed.");
					player[k].deck[selectedMeteorTarget]=null;
					break;
				}
			}
		}
		else  // no creatures in opponent deck
			noCreaturesForSpell(meteor,player[playerIndex].deck,spellIndex,false);
		
	}
	
	
	public static void spellFireballs(card deck[]) {
		
		drawSpecificSpell(fireball, deck, 3); // 3 fireballs
		
	}
	
	
	public static void spellFullmoon(card deck[]) { 
		
		drawSpecificCreature(wolf, deck, 3); // 3 wolves
		
	}
	
	
	public static void spellApocalypse(player player[], int playerIndex) {
		
		int k = (playerIndex+1)%2; // the opponent player index

		for(int i = 0; i<9; i++) // making null every opponent card
			player[k].deck[i]=null;
		
		System.out.println("apocalypse destroyed every card of "+player[k].name);
		
	}
	
	
	public static void attack(creature attacker, card[] defenderDeck, int defenderIndex) {//creatures attacking each other
		
		System.out.println("\n"+attacker.name+"'s attack damage: "+attacker.attackDamage+"\n"+defenderDeck[defenderIndex].name+"'s health: "+ ((creature)defenderDeck[defenderIndex]).health 
				+ "\n" + attacker.name+ " attacked "+ defenderDeck[defenderIndex].name+" and");
		
		if(attacker.attackDamage >= ((creature)defenderDeck[defenderIndex]).health){//enough damage to kill
			System.out.println("\n"+defenderDeck[defenderIndex].name +" has been killed.");
			defenderDeck[defenderIndex] = null;
		}
		
		else {//not enough damage to kill
			((creature)defenderDeck[defenderIndex]).health = ((creature)defenderDeck[defenderIndex]).health-attacker.attackDamage;
			System.out.println("\n"+defenderDeck[defenderIndex].name +"'s new health is " + ((creature)defenderDeck[defenderIndex]).health +".");
		}
		
	}
	
	
	public static creature copyCard(creature sample) {//method to use drawed cards as a cloned one so the card's itself is not touched and drawable again
	
		String sampleName = sample.name;
		String sampleDescription = sample.description;
		int sampleUniqueness = sample.uniqueness;
		int sampleCost = sample.cost;
		int sampleAttackDamage = sample.attackDamage;
		int sampleHealth = sample.health;
		return new creature(sampleCost,sampleDescription,sampleUniqueness,sampleName,sampleAttackDamage,sampleHealth);
		
	}
	
	
	public static spell copyCard(spell sample) {//method to use drawed cards as a cloned one so the card's itself is not touched and drawable again
	
		String sampleName = sample.name;
		String sampleDescription = sample.description;
		int sampleUniqueness = sample.uniqueness;
		int sampleCost = sample.cost;
		return new spell(sampleCost,sampleDescription,sampleUniqueness,sampleName);
		
	}
	
	
	public static void drawCard(player playerX, int cost) {// drawing card random of 5 rarity in one of 5 costs
			
		playerX.mana = playerX.mana-cost; //charging the player
			
		int rarity = random.nextInt(5); //random rarity of 5
			
		
		
		for(int i = 0; i<9; i++) { // checking for empty spot in players deck to put card

			if(playerX.deck[i] == null) { //empty spot

				System.out.println(playerX+"'s new mana is " + playerX.mana + "\n\nthe card drawen is " + ((card)deck.get((cost-1)*5+rarity)).name+ "\n");

				if((cost==1&&rarity==0)) // if card 'miss' gets drawen, it do not get into deck
					return;
				
				if((cost==5&&rarity==4)) { // if card 'emirg' gets drawen, it's method gets applied
					pulledEmirg(playerX);
					return;
				}
				
				if(deck.get((cost-1)*5+rarity).getClass().equals(spell.class))// copyCard() method requires specific class so got to make sure if its class spell or class creature						playerX.deck[i] = copyCard((spell)deck.get((cost-1)*5+rarity));
					playerX.deck[i] = copyCard((spell)deck.get((cost-1)*5+rarity));
				
				else
					playerX.deck[i] = copyCard((creature)deck.get((cost-1)*5+rarity));

				break;
				
			}
			else if(i==8) { // no empty spots has been found till the end of for() block
				
				System.out.println("there is no empty spots in your deck so you get your mana back and cannot draw card");
				playerX.mana = playerX.mana+cost;
				
			}
			
		}
	}
	
	
	public static void buyCard(player player) { // card buying process
		
		while(true) {
			
			System.out.println("\n\n\n\n\n\n\n"+ player + " has " +player.mana +" mana, what cost of cards do you wanna draw?\ntype '0' for ending");
		
		
			int choiceOfCost = inputIntervalInt(0,5);

			if (choiceOfCost==0) // 0 is get back choice, others is mana choice 
				break;
		
			System.out.println("\n0)get back\n1)buy\n2)reveal cards by order of rarity");
		
			int choiceOfAction = inputIntervalInt(0,2);
		
			if(choiceOfAction == 0); // get back
		
			else if(choiceOfAction == 1) { // buy
			
				if(choiceOfCost <= player.mana) // mana is enough
					drawCard(player, choiceOfCost);
				
				else  // not enough mana
					System.out.println("\nnot enough mana\n");
			
			}
		
			else if(choiceOfAction == 2) { // reveal card one by one
			
				for(int i = 0; i<5;i++) 
					System.out.println("\n"+(i+1)+"-"+deck.get((choiceOfCost-1)*5+i));
			
			}
		
		}
		
	}
	
	
	public static String deckToString(String name, card deck[]) { // displaying deck as rows
		
		StringBuffer asd = new StringBuffer();
		
		asd.append("\n" + name + "'s deck\n");
		
		for(int i = 0;i<9;i++) {
			
			if(deck[i]==null) // null
			asd.append(i+"."+deck[i]+"\n");
			
			else // cards
			asd.append(i+"."+deck[i].name+" - "+ deck[i].getClass().getSimpleName() +"\n");
		}
		
		return String.valueOf(asd);
		
	}
	
	
	public static String deckToStringWithHP(String name, card deck[]) { // displaying deck as rows with health points at the side of creature ones
		
		StringBuffer asd = new StringBuffer();
		
		asd.append("\n" + name + "'s deck\n");
		
		for(int i = 0;i<9;i++) {
			
			if(deck[i]==null) // null 
			asd.append(i+"."+deck[i]+"\n");
			
			else if(deck[i].getClass()==spell.class) // spell cards (no hp)
			asd.append(i+"."+deck[i].name+" - "+ deck[i].getClass().getSimpleName() +"\n");
			
			else // creature cards with hp
			asd.append(i+"."+deck[i].name+" - "+ deck[i].getClass().getSimpleName() +" | HP:"+ ((creature)deck[i]).health +"\n");
		}
		
		return String.valueOf(asd);
		
	}
	
	
	public static boolean doesThisDeckContainCreature(card deck[]) { // checking if a deck only contains spells/nulls (there is nothing to attack)
		
		for(int i = 0; i<9; i++){
			
			if(deck[i] == null || deck[i].getClass().equals(spell.class));
			
			else
			return true;
			
		}
		return false; // if we get out of the for() block without returning, that means we got no card except nulls/spells so it's not an attackable deck
	
	}
	
	
	public static int inputInt() { // for getting proper integer input (avoiding exception throw for strings exc.)
		
		while(true) {
		
			Scanner asd = new Scanner(System.in);
		
			if(asd.hasNextInt()) {
			
				int a = asd.nextInt();
			
				return a;
		
			}
			else
			System.out.println("invalid value");
		}
	}
	
	
	public static int inputIntervalInt(int min, int max) { // for getting proper integer input in a interval (avoiding exception throw for strings exc.)
		
		while(true) {
			
			Scanner asd = new Scanner(System.in);
			
			if(asd.hasNextInt() ) {
				
				int a = asd.nextInt();
			
				if(min <= a && max >= a) 
				return a;
			
				else
				System.out.println("invalid value");
			}
			
			else
			System.out.println("invalid value\n");
			
		}
	}
}
