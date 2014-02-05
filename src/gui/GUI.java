package gui;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class GUI extends JFrame {
	private static final int WIDTH = 44 * 9;
	private static final int HEIGHT = WIDTH;
	private ActionListener cmdListener;
	private GUIBoard othelloBoard;

	public GUI(ActionListener cmdListener) {
		this.cmdListener = cmdListener;
		init();
	}

	public void updateBoardState(int[][] state) {
		othelloBoard.updateBoardState(state);
	}

	private void init() {
		setTitle("Othello");
		setSize(WIDTH, HEIGHT);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		othelloBoard = setupBoard();
		add(othelloBoard);

		setResizable(false);
		setVisible(true);
	}

	private GUIBoard setupBoard() {
		GUIBoard othelloBoard = new GUIBoard();
		othelloBoard.setLayout(new GridLayout(9, 9));

		GUITile[][] tiles = new GUITile[8][8];
		JLabel borderTile;

		for (int j = 0; j < 8; j++) {
			borderTile = new JLabel();
			borderTile.setText("     " + String.valueOf(j + 1));
			othelloBoard.add(borderTile);

			for (int i = 0; i < 8; i++) {
				GUITile t = new GUITile(i, j);
				t.addActionListener(cmdListener);
				tiles[i][j] = t;
				othelloBoard.add(tiles[i][j]);
			}
		}

		borderTile = new JLabel();
		othelloBoard.add(borderTile);

		for (int j = 0; j < 8; j++) {
			borderTile = new JLabel();
			borderTile.setText(String.valueOf((char) (j + 'a')));
			othelloBoard.add(borderTile);
		}

		othelloBoard.setTiles(tiles);
		return othelloBoard;
	}
}
