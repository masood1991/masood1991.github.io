
/* ***********************************************************
COSC 1174 Lab Programming Fundamental 2- Fall 2021
Chapter 15-Poker2
Hw 8
Name: Billal Masood
Date: 11-10-2021
********************************************************** */
import java.io.FileInputStream;

import javafx.animation.PathTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class DisplayFiveCards extends Application {
	
	//declare a variable for money
	int money = 200;
	
	// Create a pane and sets its properties
	GridPane pane = new GridPane();
	boolean[] usedCards = new boolean[53];
	// Make an array for an ImageView
	ImageView[] Cards = new ImageView[5];
	boolean[] isHeld = new boolean[5];
	int[] cardNumber = new int[5];

	@Override // Override the start method in the Application class
	public void start(Stage primaryStage) throws Exception {

		// Set its alignment
		pane.setAlignment(Pos.CENTER);

		// Create a deal button and sets its properties
		Button Deal = new Button("DEAL");
		DealHandlerClass DealHandler = new DealHandlerClass();
		Deal.setOnAction(DealHandler);
		pane.add(Deal, 2, 2);

		// Create a draw button and set its properties
		Button Draw = new Button("DRAW");
		DrawHandlerClass DrawHandler = new DrawHandlerClass();
		Draw.setOnAction(DrawHandler);
		pane.add(Draw, 2, 3);

		// Create 5 hold buttons and sets its properties
		Button Hold1 = new Button("HOLD");
		Hold1 HoldHandler1 = new Hold1();
		Hold1.setOnAction(HoldHandler1);
		pane.add(Hold1, 0, 1);
		Button Hold2 = new Button("HOLD");
		Hold2 HoldHandler2 = new Hold2();
		Hold2.setOnAction(HoldHandler2);
		pane.add(Hold2, 1, 1);
		Button Hold3 = new Button("HOLD");
		Hold3 HoldHandler3 = new Hold3();
		Hold3.setOnAction(HoldHandler3);
		pane.add(Hold3, 2, 1);
		Button Hold4 = new Button("HOLD");
		Hold4 HoldHandler4 = new Hold4();
		Hold4.setOnAction(HoldHandler4);
		pane.add(Hold4, 3, 1);
		Button Hold5 = new Button("HOLD");
		Hold5 HoldHandler5 = new Hold5();
		Hold5.setOnAction(HoldHandler5);
		pane.add(Hold5, 4, 1);
		
		//Create the radio buttons for $1, $10, and $100
		RadioButton rbOneDollar = new RadioButton("$1");
		RadioButton rbTenDollar = new RadioButton("$10");
		RadioButton rbOneHundredDollar = new RadioButton("$100");
		
		//Create a text object and put player's money
		String moneyString = Integer.toString(money);
		Text textButton = new Text("Your money is: $" +  moneyString);
		pane.add(rbOneDollar, 0, 4);
		pane.add(rbTenDollar, 1, 4);
		pane.add(rbOneHundredDollar, 2, 4);
		pane.add(textButton, 2, 5);
		
		// choose 5 random distinct cards from the deck
		int count = 0;
		while (count < 5) {
			int card = 1 + (int) (Math.random() * 52);
			cardNumber[count] = card;
			if (!usedCards[card]) {
				usedCards[card] = true;
				// card png files start from 1, so increment card by one
				// for the correct file name.
				Cards[count] = (new ImageView(new Image(new FileInputStream("card/" + card + ".png"))));
				Entry(Cards[count]);
				pane.add(Cards[count], count, 0);
				count++;
			}
		}

		// Reset the used cards
		for (int i = 0; i < 52; i++) {
			usedCards[i] = false;
		}

		// Create a scene and place it in the stage
		Scene scene = new Scene(pane, 500, 500);
		primaryStage.setTitle("5 Random Cards");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	// Return the random and puts into array to keep track cards
	public ImageView getCard(int i) { // Get 1 card. The parameter is the index of the card to be gotten
		try {
			
			int card = 1 + (int) (Math.random() * 52);
			cardNumber[i] = card;
			Cards[i] = (new ImageView(new Image(new FileInputStream("card/" + card + ".png"))));

		} catch (Exception e) {
		}
		return Cards[i];
	}
	// Sets up the animation and handle the cards
	public void newHand(ImageView[] cards) {
		try {
			for (int i = 0; i < 5; i++) {
				if (isHeld[i]) {
					continue;
				}

				int column = i; // Fix scope error
				
				// Create the first half of the flip
				ScaleTransition st1 = new ScaleTransition(Duration.millis(500), cards[i]);
				st1.setFromX(1);
				st1.setToX(0);
				//Get the image view for the back of the card and image view for the new card
				ImageView cardBack = (new ImageView(new Image(new FileInputStream("card/b2fv.png"))));
				ImageView newCard = getCard(i);
				
				// Create the entry path transition
				PathTransition ptEntry = new PathTransition(Duration.millis(1000), new Line(36, -200, 36, 48),
						newCard);

				// Create the exit path transition
				PathTransition ptExit = new PathTransition(Duration.millis(1000), new Line(36, 48, 36, 400), cardBack);

				// Create second half of the flip and set its properties
				ScaleTransition st2 = new ScaleTransition(Duration.millis(500), cardBack);
				st2.setFromX(0);
				st2.setToX(1);
				
				// Play second half of the flip
				st1.setOnFinished(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent t) {
						pane.add(cardBack, column, 0);
						st2.play(); // Start Animation
					}
				});
				//Play exit animation
				st2.setOnFinished(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent t) {
						ptExit.play(); // Start Animation
					}
				});
				// Get new cards and play entry animation for new cards
				ptExit.setOnFinished(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent t) {
						Cards[column] = newCard;
						pane.add(newCard, column, 0);
						ptEntry.play(); // Start Animation
					}
				});

				st1.play(); // Start Animation
			}
		} catch (Exception e) {
		}
	}
	//Entry Animation when the program is started
	public void Entry(ImageView Card) throws Exception {
		// Create a path transition
		PathTransition pt = new PathTransition(Duration.millis(1000), new Line(36, -200, 36, 48), Card);
		pt.play(); // Start Animation
	}
	
	public boolean getWin() {
		boolean getWin = true;
		int lowerFlushBound;
		int upperFlushBound;
		
		// Check for a pair
		for(int i = 0; i < 5; i++) {
			for(int j = i + 1; j < 5; j++) {
				if((cardNumber[i] - cardNumber[j]) % 13 == 0)
					return true;
			}
		}
		
		// Find the suite of the first card
		if(cardNumber[0] <= 13) {
			lowerFlushBound = 1;
			upperFlushBound = 13;
		}
		else if (cardNumber[0] <= 26) {
			lowerFlushBound = 14;
			upperFlushBound = 26;
		}
		else if (cardNumber[0] <= 39) {
			lowerFlushBound = 27;
			upperFlushBound = 39;
		}
		else {
			lowerFlushBound = 40;
			upperFlushBound = 52;
		}
		
		// Check for flush
		for(int i = 0; i < 5; i++) {
			if(cardNumber[i] <= lowerFlushBound || cardNumber[i] >= upperFlushBound)
				getWin = false;
		}
		
		// Find value of each card, regardless of suit
		for(int i = 0; i < 5; i++) {
			cardNumber[i] %= 13;
			if(cardNumber[i] == 0)
				cardNumber[i] = 13;
		}
		// Sort cards by value
		for(int i = 0; i < 5; i++) {
			for(int j = i + 1; j < 5; j++) {
				if(cardNumber[j] > cardNumber[i]) {
					int temp = cardNumber[j];
					cardNumber[j] = cardNumber[i];
					cardNumber[i] = temp;
				}
			}
		}
		
		// Check for straight
		for(int i = 0; i < 5; i++) {
			if(cardNumber[i+1] != cardNumber[i] + 1 || (cardNumber[0] != 1 && cardNumber[4] != 13 && cardNumber[i+1] != i + 10))
				getWin = false;
		}
		
		return getWin;
	}

	// Create a DealHanderClass when the deal button is clicked
	class DealHandlerClass implements EventHandler<ActionEvent> {
		@Override // whenever the button is clicked, the cards are dealt
		public void handle(ActionEvent e) {
			
			for(int i = 0; i < isHeld.length; i++) {
				isHeld[i] = false;
			}
			
			newHand(Cards);// Get new hand and play animation
			
		}
	}

	// Create a DrawHandlerClass when the draw button is clicked
	class DrawHandlerClass implements EventHandler<ActionEvent> {
		@Override // whenever the button is clicked, the unheld cards are replace with new cards
		public void handle(ActionEvent e) {
			
			newHand(Cards);// Get new hand and play animation

		}
	}

	// Create a handler class for the first whole button
	class Hold1 implements EventHandler<ActionEvent> {
		@Override // whenever the button is clicked, the card is held and when the button is
					// clicked again, the card is upheld
		public void handle(ActionEvent e) {
			if (isHeld[0] == true) {
				isHeld[0] = false;
			} else {
				isHeld[0] = true;
			}
		}
	}

	// Create a handler class for the second whole button
	class Hold2 implements EventHandler<ActionEvent> {
		@Override // whenever the button is clicked, the card is held and when the button is
					// clicked again, the card is upheld
		public void handle(ActionEvent e) {
			if (isHeld[1] == true) {
				isHeld[1] = false;
			} else {
				isHeld[1] = true;
			}
		}
	}

	// Create a handler class for the third whole button
	class Hold3 implements EventHandler<ActionEvent> {
		@Override // whenever the button is clicked, the card is held and when the button is
					// clicked again, the card is upheld
		public void handle(ActionEvent e) {
			if (isHeld[2] == true) {
				isHeld[2] = false;
			} else {
				isHeld[2] = true;
			}
		}
	}

	// Create a handler class for the fourth whole button
	class Hold4 implements EventHandler<ActionEvent> {
		@Override // whenever the button is clicked, the card is held and when the button is
					// clicked again, the card is upheld
		public void handle(ActionEvent e) {
			if (isHeld[3] == true) {
				isHeld[3] = false;
			} else {
				isHeld[3] = true;
			}
		}
	}

	// Create a handler class for the fifth whole button
	class Hold5 implements EventHandler<ActionEvent> {
		@Override // whenever the button is clicked, the card is held and when the button is
					// clicked again, the card is upheld
		public void handle(ActionEvent e) {
			if (isHeld[4] == true) {
				isHeld[4] = false;
			} else {
				isHeld[4] = true;
			}
		}
	}

	// The main method is only needed for the IDE with limited JavaFX support. Not
	// needed for running from the command line.
	public static void main(String[] args) {
		Application.launch(args);

	}

}