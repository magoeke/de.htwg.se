package de.htwg.fivewins.view.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import de.htwg.fivewins.FiveWins;
import de.htwg.fivewins.controller.FiveWinsController;
import de.htwg.fivewins.controller.IFiveWinsController;
import de.htwg.fivewins.model.ai.AIAdapter;
import de.htwg.fivewins.model.ai.StrongAI;
import de.htwg.fivewins.model.ai.VerySillyAI;
import de.htwg.fivewins.model.field.Field;
import de.htwg.fivewins.view.tui.TextUI;
import de.htwg.util.observer.IObserver;

/*
 * @author Max
 */
public class GameFrame extends JFrame {

	/*
	 * To switch betwee GamePanel and MainMenuPanel is a cardlayout used.
	 */

	private static final long serialVersionUID = 1L;
	private static final String GAMEPANEL = "GamePanel";
	private static final String MAINMENUPANEL = "MainMenuPanel";
	private static final int BOTTOMBORDER = 0;
	private static final int TOPBORDER = 20;
	private static final int HEIGHT = 640;
	private static final int WIDTH = 400;

	private IFiveWinsController controller = null;
	private GamePanel gamePanel;
	private JPanel mainPanel;

	/*
	 * initialize GameFrame with a controller
	 */
	public GameFrame(IFiveWinsController controller) {
		this.controller = controller;
		this.setTitle("FiveWins");
		this.setLocationRelativeTo(null);
		this.setSize(HEIGHT, WIDTH);
		this.setJMenuBar(new GameMenuBar(this));

		MainMenuPanel mainMenuPanel = new MainMenuPanel(this);
		gamePanel = new GamePanel(1, this, controller);

		mainPanel = new JPanel();
		mainPanel.setLayout(new CardLayout());
		mainPanel.add(mainMenuPanel, MAINMENUPANEL);
		mainPanel.add(gamePanel, GAMEPANEL);

		this.add(mainPanel, BorderLayout.CENTER);

		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/*
	 * start a new Player vs. player game
	 */
	public void startGamePlayer(int fieldsize) {
		gamePanel.setPlayer("X");
		if (fieldsize > BOTTOMBORDER && fieldsize <= TOPBORDER) {
			resizeGameField(fieldsize);
			controller.resizeGameField(fieldsize);
			CardLayout c1 = (CardLayout) (mainPanel.getLayout());
			c1.show(mainPanel, GAMEPANEL);
		}
	}

	/*
	 * start a new npc vs. player game
	 */
	public void startGameNPC(int fieldsize, String levelOfDifficulty,
			String sign) {
		gamePanel.setPlayer("X");
		Field field = new Field(fieldsize);
		controller.createAI();
		// if(levelOfDifficulty.equals("silly")) {
//		controller = new FiveWinsController(field, new VerySillyAI(sign, field));
		// } else {
		// controller = new FiveWinsController(field, new StrongAI(sign,
		// field));
		// }
		if (fieldsize > BOTTOMBORDER && fieldsize <= TOPBORDER) {
			resizeGameField(fieldsize);
			controller.resizeGameField(fieldsize);
			CardLayout c1 = (CardLayout) (mainPanel.getLayout());
			c1.show(mainPanel, GAMEPANEL);
		}
		// gamePanel.firstAction();
	}

	/*
	 * Change the gamefieldsize. Because first initialize is with default values
	 */
	public void resizeGameField(int fieldsize) {
		controller.removeObserver(gamePanel);
		gamePanel = new GamePanel(fieldsize, this, controller);
		mainPanel.add(gamePanel, GAMEPANEL);
	}

	/*
	 * leave the game and go back to the main menu
	 */
	public void backToMainMenu() {
		CardLayout c1 = (CardLayout) (mainPanel.getLayout());
		c1.show(mainPanel, MAINMENUPANEL);
	}

	/*
	 * @return the actual player sign
	 */
	public String getPlayerSign() {
		return controller.getPlayerSign();
	}

	// /*
	// * makes the npc turn
	// */
	// public boolean nowSecondPlayer() {
	// AIAdapter aia = controller.getSecondPlayer();
	// boolean returnValue = false;
	// if(aia != null && aia.getWhichPlayer().equals(getPlayerSign())) {
	// returnValue = true;
	// }
	// return returnValue;
	// }

	// /*
	// * @return the ai object
	// */
	// public AIAdapter getSecondPlayer() {
	// return controller.getSecondPlayer();
	// }

	/*
	 * handle the action button pressed
	 */
	public void handleAction(String command) {
		controller.handleInputOrQuit(command);
		gamePanel.setTurn(controller.getTurn());
		gamePanel.setPlayer(controller.getPlayerSign());

		if (controller.getWinner()) {
			// output win message and go back to main menu
			JOptionPane.showMessageDialog(null,
					"Player " + controller.getWinnerSign() + " has won!",
					"Game Over", JOptionPane.INFORMATION_MESSAGE);

			gamePanel.allButtonsEnabled();
		}
		if (controller.getDraw()) {
			// output draw message and go back to main menu
			JOptionPane.showMessageDialog(null, "It's a draw!", "Game Over",
					JOptionPane.INFORMATION_MESSAGE);

			gamePanel.allButtonsEnabled();
		}
	}

	/*
	 * reset the game
	 */
	public void reset() {
		gamePanel.reset();
		controller.reset();
	}

	/*
	 * @return if game is won or not
	 */
	public boolean isWon() {
		return controller.getWinner();
	}

	public String[][] getGamefield() {
		return controller.getField();
	}

}