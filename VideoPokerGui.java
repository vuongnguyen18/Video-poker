
/**
 * @author: Vuong (Jay) Nguyen
 * @assignment CSC212-ProgramGUI
 * @version (put the data here): 5/2/2019
 * 
 * Description: In video poker, a player is dealt a hand of five playing cards. The user selects 0-5 of the cards to discard. The selected 
 * cards are replaced by new cards from the same deck to give the final poker hand. The user placed a bet on each hand. 
 * Different kinds of hands give different payouts on the bet. To win anything, the user must have a pair of Jacks or
 * better. 
 * 
 * Citations of Assistance (who and what):
 * J.H (tutor) : help me how tp use cardPanel.
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

public class VideoPokerGui extends MouseAdapter implements ActionListener {

	// main method
	public static void main(String[] args) {
		VideoPokerGui gui = new VideoPokerGui();
	}

	private static final int FRAME_HEIGHT = 300;
	private static final int FRAME_WIDTH = 540;
	private static final int START_AMOUNT = 100;
	private static final int DEFAULT_BET = 10;

	public static final int NUM_CARDS = 5;
	public static final int[] reWard = { 0, 1, 2, 3, 4, 6, 9, 25, 50, 800 };
	public static final String[] onHand = { "NOTHING", "EVEN", "Two pair", "Triple", "Straight", "Flush", "Full House",
			"Four-of-a-Kind", "Straight Flush", "Royal Fulsh" };

	// data fields
	private PokerCard[] cards;
	private PokerDeck pokerDeck;
	private boolean[] faceUp;
	private JTextField enterAmount;
	private JButton dealButton;
	private JButton drawButton;
	private JButton quitButton;
	private JLabel lineOne;
	private JLabel lineTwo;
	private CardPanel display;
	private JLabel theBet;
	private int amount;

	/**
	 * Several constants are given in the start file including the size of the
	 * frame. The frame should close on program termination and contains the
	 * following components: JLabels for the first two lines. A CardPanel to display
	 * the cards A JLabel and JTextfield for the bet Three JButtons for DEAL, DRAW
	 * and QUIT.
	 * 
	 */
	public VideoPokerGui() {
		this.cards = new PokerCard[NUM_CARDS];
		this.faceUp = new boolean[NUM_CARDS];
		this.pokerDeck = new PokerDeck();
		this.amount = START_AMOUNT;

		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		display = new CardPanel(cards, faceUp);
		display.addMouseListener(this);

		frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		frame.setTitle("Video Poker--Jacks or Better");
		frame.add(display2(), BorderLayout.NORTH);
		frame.add(display1(), BorderLayout.SOUTH);
		frame.add(display, BorderLayout.CENTER);
		frame.setVisible(true);
	}

	/**
	 * The method which have all the code for the display2 panel.
	 * 
	 * @return: the panel for display2.
	 */
	public JPanel display2() {
		JPanel display2 = new JPanel(new GridLayout(2, 1));
		lineOne = new JLabel("You have $" + START_AMOUNT);
		lineTwo = new JLabel("Let's Play. Click DEAL to begin.");
		lineOne.setFont(new Font("Serif", Font.BOLD, 20));
		lineTwo.setFont(new Font("Serif", Font.BOLD, 20));
		display2.add(lineOne);
		display2.add(lineTwo);
		display2.setBackground(Color.green);
		return display2;
	}

	/**
	 * The method which have all the code for the display1 panel.
	 * 
	 * @return: the panel for display1.
	 */
	public JPanel display1() {
		JPanel display1 = new JPanel(new FlowLayout());
		JLabel theBet = new JLabel("Your bet: ");

		enterAmount = new JTextField(5);
		enterAmount.setText("" + DEFAULT_BET);

		dealButton = new JButton("DEAL");
		drawButton = new JButton("DRAW");
		quitButton = new JButton("QUIT");

		dealButton.addActionListener(this);

		drawButton.addActionListener(this);

		quitButton.addActionListener(this);

		drawButton.setEnabled(false);
		display1.add(theBet);
		display1.add(enterAmount);
		display1.add(dealButton);
		display1.add(drawButton);
		display1.add(quitButton);
		return display1;
	}

	/**
	 * The method is used to check whether the bet is valid or not.
	 * 
	 * @return: true if the bet is valid and false if the bet is invalid and it also
	 *          show what kind of error you make.
	 */
	public boolean checkBet() {
		boolean goodBet = true;
		try {
			String number = enterAmount.getText();
			int numb = Integer.parseInt(number);
			if (numb < 0) {
				goodBet = false;
				lineTwo.setText("The bet amount must be greater than zero!");
			} else if (numb > amount) {
				goodBet = false;
				lineTwo.setText("You don't have that much money");
			} else {
				amount -= numb;
				lineOne.setText("You have $" + (amount));
				lineTwo.setText("Click cards to discard and hit DRAW");
				goodBet = true;
			}
		} catch (NumberFormatException error) {
			lineTwo.setText("The bet input does not contain a legal integer!");
		}
		return goodBet;
	}

	/**
	 * The method is used to show the card when the player press the deal button,
	 * the cards will be deal and when they click on the card then hit the draw
	 * button, the card which is being clicked will be replace with a new card.
	 */
	public void pressDeal() {
		pokerDeck.shuffle();
		for (int i = 0; i < NUM_CARDS; i++) {
			cards[i] = pokerDeck.dealCard();
			faceUp[i] = true;
		}
		display.update();
	}

	/**
	 * The method is used to flip the card when the player click on the card.
	 */
	@Override
	public void mouseClicked(MouseEvent event) {
		int index = display.indexOfCard(event.getX(), event.getY());
		if (index != -1) {
			faceUp[index] = !faceUp[index];
			display.update();
		}
	}

	/**
	 * This method is used after the player flip the card down then they hit the
	 * draw button and the card flip down will be replaced with different card.
	 */
	public void pressDraw() {
		for (int i = 0; i < NUM_CARDS; i++) {
			if (faceUp[i] == false) {
				cards[i] = pokerDeck.dealCard();
			}
			faceUp[i] = true;
		}
		display.update();
	}

	/**
	 * This show how much money you make after every turn and it also show what kind
	 * of hand you get.
	 */
	public void payoutComment() {
		int index = getPokerRank();
		amount = amount + reward();
		if (index == 0) {
			lineOne.setText("You have $" + amount);
			lineTwo.setText("No hand. You lose your bet.");
		} else if (index == 1) {
			lineOne.setText("You have $" + amount);
			lineTwo.setText("Pair, Jacks or better: You win even money.");
		} else {

			lineOne.setText("You have $" + amount);
			lineTwo.setText(onHand[index] + ": " + "Pays " + index + "-to-1. You win $" + reward());
		}

	}

	/**
	 * This method calculate the amount of money you make after every turn.
	 * 
	 * @return: the amount of money you make after every turn.
	 */
	public int reward() {
		String number = enterAmount.getText();
		int numb = Integer.parseInt(number);
		int amountReward = 0;
		for (int i = 0; i < reWard.length; i++) {
			if (getPokerRank() == i) {
				amountReward = numb * reWard[i];
			}
		}
		return amountReward;
	}

	/**
	 * This is show the display when you click one of the three buttons.
	 */
	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		if (source == dealButton) {
			if (checkBet()) {
				pressDeal();
				dealButton.setEnabled(false);
				drawButton.setEnabled(true);
			}
		} else if (source == drawButton) {
			pressDraw();
			payoutComment();
			drawButton.setEnabled(false);
			dealButton.setEnabled(true);
			if (amount == 0) {
				JOptionPane.showMessageDialog(null, "You're out of money!");
				dealButton.setEnabled(false);
				enterAmount.setEnabled(false);
			}
		} else {
			System.exit(0);
		}
	}

	/**
	 * DON'T CHANGE THIS METHOD
	 * 
	 * Evaluates a hand of five poker cards. The return value is the hand's "rank",
	 * which is one of the following constants: PokerRank.NOTHING, PokerRank.PAIR,
	 * PokerRank.TWO_PAIR, PokerRank.TRIPLE, PokerRank.STRAIGHT, PokerRank.FLUSH,
	 * PokerRank.FULL_HOUSE, PokerRank.FOUR_OF_A_KIND, PokerRank.STRAIGHT_FLUSH, or
	 * PokerRank.ROYAL_FLUSH. Note that PokerRank.PAIR is only returned for a pair
	 * of Jacks or better. PokerRank.NOTHING is returned for a hand that is less
	 * than a pair of Jacks, which gets no payout in this game.
	 * 
	 * @return the rank of the hand
	 */
	private int getPokerRank() {
		PokerRank ranker = new PokerRank();
		for (int i = 0; i < NUM_CARDS; i++) {
			ranker.add(cards[i]);
		}
		int rank = ranker.getHandType();
		if (rank == PokerRank.PAIR) {
			// if it's not at least a pair of Jacks, return NOTHING rather than PAIR
			PokerCard card = ranker.getCards().get(0);
			if (card.getValue() < PokerCard.JACK) {
				rank = PokerRank.NOTHING;
			}
		}
		return rank;
	}

}
