package controller;

import game.Board;
import gui.GUI;
import gui.GUITile;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ai.Agent;

public class Controller {
	Agent agent;
	Board board;
	GUI gui;
	private int playerColour = Board.BLACK;
	private int aiColour = Board.WHITE;
	private int roundsWithoutTimeOut = 0;

	public Controller(Board board) {
		this.board = board;
		gui = new GUI(new CommandListener(), new TimerListener());

		board.newGame();
		int timeLimit = 1;
		int maxDepth = 7;
		agent = new Agent(aiColour, timeLimit, maxDepth);
		gui.setAIRecursionDepth(maxDepth);
		gui.clearInfoText();
		updateGUIState();
	}

	class CommandListener implements ActionListener {
		@SuppressWarnings("synthetic-access")
		@Override
		public void actionPerformed(ActionEvent e) {
			GUITile tile = (GUITile) e.getSource();
			int x = tile.getOthelloX();
			int y = tile.getOthelloY();

			boolean legal = board.isLegalMove(x, y, playerColour);
			if (legal) {
				board = board.placeDisk(x, y, playerColour);
				updateGUIState();
				gui.clearInfoText();
				if (board.isGameOver()) {
					printScore();
				} else {
					/*
					 * If it is impossible to place disks after the AI:s round,
					 * the AI can place again. Same goes for the player.
					 */
					do {
						doAIRound();
						if (board.isGameOver()) {
							printScore();
							break;
						}
					} while (!board.canPlaceDisk(playerColour));
				}
			} else {
				gui.printInvalidMode();
			}
		}

		private void printScore() {
			int white = board.calculateScore(Board.WHITE);
			int black = board.calculateScore(Board.BLACK);
			// int evalScore = board.evaluate(Board.WHITE);

			System.out.println("Score: ");
			System.out.println("White: " + white);
			System.out.println("Black: " + black);
			// System.out.println("Eval: " + evalScore);
			if (white > black) {
				System.out.println("White wins!");
			} else {
				System.out.println("Black wins!");
			}

		}
	}

	public void doAIRound() {

		if (board.canPlaceDisk(aiColour)) {
			board = agent.placeDisk(board);
			if (agent.timedOut()) {
				gui.setAIRecursionDepth(agent.decreaseDepth());
				gui.printTimedOut();
				roundsWithoutTimeOut = 0;
			} else {
				gui.refreshLimitText();
				roundsWithoutTimeOut++;
				if (roundsWithoutTimeOut > 5) {
					gui.setAIRecursionDepth(agent.increaseDepth());

					roundsWithoutTimeOut = 0;
				}
			}
			updateGUIState();
		}
	}

	class TimerListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			String input = gui.getTimerInput();
			try {
				double newTimeLimit = Double.parseDouble(input);
				agent.setTimeLimit(newTimeLimit);
				gui.updateLimitText(input);

			} catch (NumberFormatException exception) {
				exception.printStackTrace();
			}
			updateGUIState();
		}
	}

	void updateGUIState() {
		gui.updateBoardState(board.getState());
	}
}
