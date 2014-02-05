package controller;

import game.Board;
import gui.GUI;
import gui.GUITile;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ai.Agent;

public class Controller {
	private Agent agent;
	Board board;
	private GUI gui;
	private int playerColour = Board.BLACK;
	private int aiColour = Board.WHITE;

	public Controller(Board board) {
		this.board = board;
		gui = new GUI(new CommandListener());

		board.newGame();
		agent = new Agent(aiColour);
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

				if (board.isGameOver()) {
					printScore();
				} else {
					/*
					 * If it is impossible to place disks after the AI:s round,
					 * the AI can place again. Same goes for the player.
					 */
					do {
						if (board.canPlaceDisk(aiColour)) {
							board = agent.placeDisk(board);
							updateGUIState();
						}

						if(board.isGameOver()){
							printScore();
							break;
						}
						
					} while (!board.canPlaceDisk(playerColour));
				}
			} else {
				System.out.println("Invalid move!");
			}
		}

		private void printScore() {
			int white = board.calculateScore(Board.WHITE);
			int black = board.calculateScore(Board.BLACK);

			System.out.println("Score: ");
			System.out.println("White: " + white);
			System.out.println("Black: " + black);
			if (white > black) {
				System.out.println("White wins!");
			} else {
				System.out.println("Black wins!");
			}
		}
	}

	private void updateGUIState() {
		gui.updateBoardState(board.getState());
	}
}
