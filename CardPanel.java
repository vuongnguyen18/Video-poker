/** 
 * @author Diane Mueller
 * CardPanel displays cards horizontally for a VideoPoker game.
 * The number of cards is set in the constructor.
 */

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.*;

public class CardPanel extends JPanel {

	public static final int X_OFFSET = 20;
	public static final int Y_OFFSET = 10;

	public static final int CARD_WIDTH = 79;
	public static final int CARD_HEIGHT = 123;

	private Image cardImage;
	private PokerCard[] cards;
	private boolean[] faceUp;

	/**
	 * Constructs a CardPanel which displays cards in a row horizontally
	 * @param cards -- an array of poker cards
	 * @param faceUp -- an array of booleans indicating whether each card
	 *                  will be displayed face up when true or the back
	 *                  of the card will be displayed when false.
	 */
	public CardPanel(PokerCard[] cards, boolean[] faceUp) {
		if (cards.length != faceUp.length) {
			throw new IllegalArgumentException("Arrays must be the same length");
		}
		// loads the file of card images
		try {
			cardImage = ImageIO.read(new File("cards.png"));
		} catch (Exception e) {
			cardImage = null;
		}
		this.cards = cards;
		this.faceUp = faceUp;
	}
	
	/**
	 * Updates the CardPanel to agree with the current hand of card
	 * as stored in the cards array and the state of each card, face up 
	 * or face down.
	 */
	public void update() {
		repaint();
	}

	/**
	 * draws the horizontal row of cards in the panel
	 * 
	 * @param g -- the graphics pen for the panel
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		setBackground(Color.GREEN);
		for (int count = 0; count < cards.length; count++) {
			if (!faceUp[count]) {
				drawFaceDownCard(g, X_OFFSET + count * (CARD_WIDTH + X_OFFSET), Y_OFFSET);
			} else {
				drawCard(g, cards[count], X_OFFSET + count * (CARD_WIDTH + X_OFFSET), Y_OFFSET);
			}
		}
	}
	
	/**
	 * Determines if a point is within one of the displayed cards
	 * @param x- the x coordinate of the point
	 * @param y- the y coordinate of the point
	 * @return the index of the card containing (x, y) or -1 if no
	 * card contains (x, y)
	 */
	public int indexOfCard(int x, int y) {
		for (int i = 0; i < cards.length; i++) { 
			int upperLeftX = CardPanel.X_OFFSET + i * (CardPanel.CARD_WIDTH + CardPanel.X_OFFSET);
			int upperLeftY = CardPanel.Y_OFFSET;
			if (x > upperLeftX && x < upperLeftX + CardPanel.CARD_WIDTH
					&& y > upperLeftY && y < upperLeftY + CardPanel.CARD_HEIGHT) {
				return i;
			} 
		}
		return -1;

	}

	/**
	 * Draws a specified PokerCard in the Graphics context g, with its upper-left
	 * corner at the point (cardX,cardY). The card is CARD_WIDTH pixels wide and
	 * CARD_HEIGHT pixels tall.
	 * 
	 * @param g     -- the Graphics pen connected to the panel
	 * @param card  -- the card from the deck to be draw
	 * @param cardx -- x-coord of upper left corner where card will be drawn in the
	 *              panel
	 * @param cardy -- y-coord of upper left corner where card will be drawn in the
	 *              panel
	 * 
	 */
	private void drawCard(Graphics g, PokerCard card, int cardX, int cardY) {
		int imageX; // x-coord of upper left corner of the card inside cardsImage
		int imageY; // y-coord of upper left corner of the card inside cardsImage
		if (card.getValue() == PokerCard.ACE) {
			imageX = 0;
		} else {
			imageX = (card.getValue() - 1) * CARD_WIDTH;
		}
		int suit = card.getSuit();
		if (suit == PokerCard.CLUBS) {
			imageY = 0;
		} else if (suit == PokerCard.DIAMONDS) {
			imageY = CARD_HEIGHT;
		} else if (suit == PokerCard.HEARTS) {
			imageY = 2 * CARD_HEIGHT;
		} else {
			imageY = 3 * CARD_HEIGHT;
		}

		g.drawImage(cardImage, cardX, cardY, cardX + CARD_WIDTH, cardY + CARD_HEIGHT, imageX, imageY,
				imageX + CARD_WIDTH, imageY + CARD_HEIGHT, this);
	}

	/**
	 * Draws a face-down card in the Graphics context g with its upper left corner
	 * at the point (cardX,cardY).
	 * 
	 * @param g     -- the Graphics pen connected to the panel
	 * @param cardx -- x-coord of upper left corner where card will be drawn in the
	 *              panel
	 * @param cardy -- y-coord of upper left corner where card will be drawn in the
	 *              panel
	 * 
	 */
	private void drawFaceDownCard(Graphics g, int cardX, int cardY) {
		int imageX; // x-coord of upper left corner of the card inside cardsImage
		int imageY; // y-coord of upper left corner of the card inside cardsImage
		imageY = 4 * CARD_HEIGHT; // coords for a face-down card.
		imageX = 2 * CARD_WIDTH;
		g.drawImage(cardImage, cardX, cardY, cardX + CARD_WIDTH, cardY + CARD_HEIGHT, imageX, imageY,
				imageX + CARD_WIDTH, imageY + CARD_HEIGHT, this);
	}
}
