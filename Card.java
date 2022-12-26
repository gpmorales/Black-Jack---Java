import java.util.Random;
import javax.swing.JOptionPane;
import java.util.*;

public class Card {

    // Data attributes 
    private int cardNum; // Ace, Jack, Queen, and King are stored as 1, 11, 12, and 13, respectively, but are displayed as A, J, Q, K
    private String suit;
    private boolean AceHigh; //Ace can be c
  
    //Overloaded constructors to add specific cards to the deck
    public Card(int num, String s){
      cardNum = num;
      suit = s;
      AceHigh = false;
    }

    public Card(int num, String s, boolean AceHigh){
      cardNum = num;
      suit = s;
      this.AceHigh = AceHigh;
    }


    public static void buildDeck(ArrayDeque<Card> deck) {
    // Given an empty ArrayDeque (Double-Ended Queue), construct a standard deck of playing cards
      for(int n = 1; n < 14; n++){ //13 cards in each suit
        for(int s = 1; s < 5; s++){ //4 suits total
          if(s == 1){
            deck.addFirst(new Card(n, "hearts"));
          }
          if(s == 2){
            deck.addFirst(new Card(n, "diamonds"));
          }
          if(s == 3){
            deck.addFirst(new Card(n, "clubs"));
          }
          if(s == 4){
            deck.addFirst(new Card(n, "spades"));
          }
        }
      }
    }


    public static void initialDeal(ArrayDeque<Card> deck, ArrayList<Card> playerHand, ArrayList<Card> dealerHand){
    // Deal two cards from the deck into each of the player's hand and dealer's hand
      shuffle(deck);

      playerHand.clear(); //clear the container of the 'player'
      dealerHand.clear(); //clear the container of the 'dealer'

      for(int i = 1; i <= 4; i++){
        if(i%2 != 0) //executes when i = 1 and i = 3
          dealOne(deck, playerHand, false); //deal player first, then one more time after we deal the dealer

        else //executes when i = 2 and i = 4
          dealOne(deck, dealerHand, true);
      }
    }


    // Deal a single card from the deck to the hand
    public static void dealOne(ArrayDeque<Card> deck, ArrayList<Card> hand, boolean isDealer){
      if(!isDealer){
        Card top = deck.removeFirst(); //remove single card from top of Deck

        if(top.cardNum == 1){ //Give player the option to hit low ace or high ace (1 or 11 points)
          boolean AceHigh = false;

          Object[] options = {"Low Ace", "High Ace"};

          int playerChoice = JOptionPane.showOptionDialog(null,"You have been dealt an Ace" + "\nWhat do you want to do?", "Ace!",
                       JOptionPane.YES_NO_OPTION,
                       JOptionPane.QUESTION_MESSAGE,
                       null,
                       options,
                       options[0]);

          if(playerChoice != JOptionPane.YES_OPTION)
            AceHigh = true;

          hand.add(new Card(top.cardNum, top.suit, AceHigh));
          deck.addLast(new Card(top.cardNum, top.suit, AceHigh)); //insert this card into the back of the deck

        }

        else{ //else card is not an ace, we deal normally
          hand.add(new Card(top.cardNum, top.suit));
          deck.addLast(new Card(top.cardNum, top.suit)); //insert this card into the back of the deck
        }

      }

      else{ //we deal dealer normally
        Card top = deck.removeFirst(); //remove single card from top of Deck
        hand.add(new Card(top.cardNum, top.suit));
        deck.addLast(new Card(top.cardNum, top.suit)); //insert this card into the back of the deck
      }

    }


    public static boolean checkBust(ArrayList<Card> hand){
    // This should return whether a given hand's value exceeds 21
      if(sum(hand) > 21){
        	return true;
    	}
      return false;
    }


    public static boolean dealerTurn(ArrayDeque<Card> deck, ArrayList<Card> hand){
    // This conducts the dealer's turn and
    // Returns true if the dealer busts; false otherwise
      while(sum(hand) < 17){
          dealOne(deck, hand, true);
      }

      if(sum(hand) > 16){
        if(sum(hand) > 21){
          return true;
        }
        else{
          return false;
        }
      }

      else{
        return false;
      }

    }


    public static int whoWins(ArrayList<Card> playerHand, ArrayList<Card> dealerHand){
    // This should return 1 if the player wins and 2 if the dealer wins
		if(sum(playerHand) > sum(dealerHand)){
        	return 1;
      	}
      	else{
        	return 2;
      	}
    }


    // Return a string describing the card which has index 1 in the hand
    public static String displayCard(ArrayList<Card> hand){
      return returnCard(hand.get(1));
    }


    // Return a string listing the cards in the hand
    public static String displayHand(ArrayList<Card> hand){
      String r = returnCard(hand.get(0));
      for(int i = 1; i < hand.size(); i++){
        r = r + ", " + returnCard(hand.get(i));
      }
      return r;
    }


    // Method to randomly shuffle deck 
    public static void shuffle(ArrayDeque<Card> deck){
      //First convert our Deque into an array of Card objects using the toArray() method
      Card[] tempDeck = deck.toArray(new Card[0]);

      for(int i = 0; i < tempDeck.length; i++){ //shuffle array contents
        int randPos = (int)(Math.random() * 51);
        Card temp = tempDeck[i];
        tempDeck[i] = tempDeck[randPos];
        tempDeck[randPos] = temp;
      }

      deck.clear(); //clear contents of Deque 

      //copy shuffled array contents into our Deque in order (FIFO)
      for(Card card: tempDeck){
        deck.offerLast(card);
      }

    }

    // method to find sum of a hand
    public static int sum(ArrayList<Card> hand){
      int sum = 0;
      
      for(int i = 0; i < hand.size(); i++){
        if(hand.get(i).cardNum == 1 && hand.get(i).AceHigh == true){
          sum = sum + 11;
        }
        else if(hand.get(i).cardNum < 11){
          sum = sum + hand.get(i).cardNum;
        }
        else if(hand.get(i).cardNum > 10){
          sum = sum + 10;
        }
      }

      return sum;
    }

    // method to return a card as a string
    public static String returnCard(Card c){
      String n = Integer.toString(c.cardNum);
      String s = c.suit;

      if(c.cardNum == 1 && c.AceHigh == true){
        return "High Ace of " + s;
      }

      if(c.cardNum == 1 && c.AceHigh == false){
        return "Ace of " + s;
      }

      if(c.cardNum == 11){
        return "Jack of " + s;
      }
      if(c.cardNum == 12){
        return "Queen of " + s;
      }
      if (c.cardNum == 13){
        return "King of " + s;
      }
      else{
        return n + " of " + s;
      }
    }

    public static boolean containsAce(ArrayList<Card> playerHand){
      for(Card card : playerHand){
        if(card.cardNum == 1) return true;
      }
      return false;
    }


    //Main Method
    public static void main(String[] args) {

		int playerChoice, winner;
		ArrayDeque<Card> deck = new ArrayDeque<Card>();
		
		buildDeck(deck);
		
		playerChoice = JOptionPane.showConfirmDialog(null, "Ready to Play Blackjack?", "Blackjack", JOptionPane.OK_CANCEL_OPTION);

		if((playerChoice == JOptionPane.CLOSED_OPTION) || (playerChoice == JOptionPane.CANCEL_OPTION))
		    System.exit(0);

		
		Object[] options = {"Hit","Stand"};
		boolean isBusted, dealerBusted;
		boolean isPlayerTurn;
		ArrayList<Card> playerHand = new ArrayList<>();
		ArrayList<Card> dealerHand = new ArrayList<>();
	
		do{ // Game loop
		    initialDeal(deck, playerHand, dealerHand);

		    isPlayerTurn=true;
		    isBusted=false;
		    dealerBusted=false;
		    
		    while(isPlayerTurn){
          
          // Shows the hand and prompts player to hit or stand
          playerChoice = JOptionPane.showOptionDialog(null,"Dealer shows " + displayCard(dealerHand) + "\nYour hand is: " + displayHand(playerHand) + "\nWhat do you want to do?","Hit or Stand",
                       JOptionPane.YES_NO_OPTION,
                       JOptionPane.QUESTION_MESSAGE,
                       null,
                       options,
                       options[0]);

          if(playerChoice == JOptionPane.CLOSED_OPTION)
          {
              System.exit(0);
          }
          
          else if(playerChoice == JOptionPane.YES_OPTION) //If player hits
          {
              dealOne(deck, playerHand, false);
              isBusted = checkBust(playerHand);
              if(isBusted){
              // First Case: Player Busts but has a High (not soft) Ace 
                
                if(containsAce(playerHand)){
                  int Ace = -1; // index of the Ace;

                  for(int i = 0; i < playerHand.size(); i++){
                    if(playerHand.get(i).cardNum == 1 && playerHand.get(i).AceHigh == true ) Ace = i;
                  }
                  
                  // If we busted, there is an Ace in our deck, and its not a low Ace, then we can give player the option to make the Ace Low!
                  if(Ace != -1){
                    Object[] optionsB = {"Keep High Ace", "Make Ace Low"}; //Yes or No

                    int AceChoice = JOptionPane.showOptionDialog(null,"You have have Busted but have a High Ace!" + "\nWhat do you want to do?", "Busted but with an Ace!",
                                 JOptionPane.YES_NO_OPTION,
                                 JOptionPane.QUESTION_MESSAGE,
                                 null,
                                 optionsB,
                                 optionsB[0]);


                    if(AceChoice == JOptionPane.NO_OPTION){
                        Card oldAce = playerHand.get(Ace);
                        playerHand.set(Ace, new Card(oldAce.cardNum, oldAce.suit, false));
                        isBusted = checkBust(playerHand); //check if we busted if we make the ace low to continue playing
                    }

                  }
                }
                

                if(checkBust(playerHand)){ //Otherwise player has busted
                  String playersHand = "";
                  for(int i = 0; i < playerHand.size(); i++){
                    Card item = playerHand.get(i);
                    String hand = returnCard(item);
                    if(i != playerHand.size() - 1)
                      playersHand += hand + " , ";
                    else
                      playersHand += hand;
                  }

                  playerChoice = JOptionPane.showConfirmDialog(null,"Player has busted!" + "\nYour hand : " + playersHand, "You lose!", JOptionPane.OK_CANCEL_OPTION);

                  if((playerChoice == JOptionPane.CLOSED_OPTION) || (playerChoice == JOptionPane.CANCEL_OPTION)) //If user closes window, exit from program
                  {
                      System.exit(0);
                  }

                }
              
                isPlayerTurn=false;

              } //End of isBusted case

          }//End of player hits 
            
          // Otherwise player stood, so it is now the Dealers turn 
          else isPlayerTurn=false;

		    }

        //Now we check whether the users current hand exceeds 21
		    if(!isBusted){ // if player hasn't busted
          dealerBusted = dealerTurn(deck, dealerHand); //...then check if Dealer busted

          if(dealerBusted){ // In the case were the dealer Busts, congratulate player and give them option to play new game
              playerChoice = JOptionPane.showConfirmDialog(null, "The dealer's hand: " +displayHand(dealerHand) + "\n \nYour hand: " + displayHand(playerHand) + "\nThe dealer busted." + "\nCongratulations!", "You Win!!!", JOptionPane.OK_CANCEL_OPTION);

            if((playerChoice == JOptionPane.CLOSED_OPTION) || (playerChoice == JOptionPane.CANCEL_OPTION))
              System.exit(0);
          }
			
			
          else{ //The Dealer did not bust.  The winner must be determined

              if(containsAce(playerHand)){
                int Ace = -1; // index of the Ace;

                for(int i = 0; i < playerHand.size(); i++){
                  if(playerHand.get(i).cardNum == 1 && playerHand.get(i).AceHigh == false) Ace = i;
                }
                
                // If we busted, there is an Ace in our deck, and its not a low Ace, then we can give player the option to make the Ace Low!
                if(Ace != -1){
                  Object[] optionsC = {"Keep Soft Ace", "Make Ace High"}; //Yes or No

                  int aceChoice = JOptionPane.showOptionDialog(null,"You have have an Ace!" + "\nWhat do you want to do?", "Ace!",
                               JOptionPane.YES_NO_OPTION,
                               JOptionPane.QUESTION_MESSAGE,
                               null,
                               optionsC,
                               optionsC[0]);


                  if(aceChoice == JOptionPane.NO_OPTION){
                      Card oldAce = playerHand.get(Ace);
                      playerHand.set(Ace, new Card(oldAce.cardNum, oldAce.suit, true));
                      
                      if(checkBust(playerHand)){ //check if we busted if we make the ace high 
                        int Choice = JOptionPane.showConfirmDialog(null,"Player has busted!\n" , "You lose!", JOptionPane.OK_CANCEL_OPTION);
                        if((Choice == JOptionPane.CLOSED_OPTION) || (Choice == JOptionPane.CANCEL_OPTION)) //If user closes window, exit from program
                            System.exit(0);
                      }
                  }

                }//end of condition that checks if we have an ace

              }

              winner = whoWins(playerHand, dealerHand);

              if(winner == 1){ //Player Wins
              playerChoice = JOptionPane.showConfirmDialog(null, "The dealer's hand: " +displayHand(dealerHand) + "\n \nYour hand: " + displayHand(playerHand) + "\nCongratulations!", "You Win!!!", JOptionPane.OK_CANCEL_OPTION);

              if((playerChoice == JOptionPane.CLOSED_OPTION) || (playerChoice == JOptionPane.CANCEL_OPTION))
                  System.exit(0);
              }

              else{ //Player Loses
              playerChoice = JOptionPane.showConfirmDialog(null, "The dealer's hand: " +displayHand(dealerHand) + "\n \nYour hand: " + displayHand(playerHand) + "\nBetter luck next time!", "You lose!!!", JOptionPane.OK_CANCEL_OPTION); 
            
              if((playerChoice == JOptionPane.CLOSED_OPTION) || (playerChoice == JOptionPane.CANCEL_OPTION))
                  System.exit(0);
              }
          }
		    }

		}while(true); //End of do-while loop

    } //End of Main Method

} // End of Class
