package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GUI extends JFrame {
	private static final long serialVersionUID = -5494583478142571839L;
	private static final int WIDTH = 44 * 9;
	private static final int HEIGHT = WIDTH + 50;
	private ActionListener cmdListener, timerListener;
	private GUIBoard othelloBoard;
	private JTextField timerTextField;
	private JLabel timerLabel, recursionLabel, infoLabel;
	private String recursionDepth = "";
	private String timeLimit = "1";

	public GUI(ActionListener cmdListener, ActionListener timerListener) {
		this.cmdListener = cmdListener;
		this.timerListener = timerListener;
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

		setLayout(new BorderLayout());
		othelloBoard = setupBoard();
		add(othelloBoard, BorderLayout.CENTER);

		JPanel timerPanel = initTimerPanel();

		add(timerPanel, BorderLayout.SOUTH);
		setResizable(false);
		setVisible(true);
	}

	private JPanel initTimerPanel() {
		JPanel panel = new JPanel(new BorderLayout());

		JPanel timerInputPanel = new JPanel(new BorderLayout());
		timerTextField = new JTextField(5);
		timerInputPanel.add(timerTextField, BorderLayout.CENTER);
		JButton button = new JButton("Set new limit");
		button.addActionListener(timerListener);
		timerInputPanel.add(button, BorderLayout.EAST);

		timerLabel = new JLabel("    AI time limit: " + timeLimit
				+ " second(s)");
		recursionLabel = new JLabel("    AI recursion depth: " + recursionDepth);

		infoLabel = new JLabel("    ");

		JPanel northPanel = new JPanel(new BorderLayout());
		northPanel.add(recursionLabel, BorderLayout.WEST);
		northPanel.add(infoLabel, BorderLayout.EAST);

		panel.add(northPanel, BorderLayout.NORTH);
		panel.add(timerInputPanel, BorderLayout.EAST);
		panel.add(timerLabel, BorderLayout.WEST);
		return panel;
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

	public String getTimerInput() {
		return timerTextField.getText();
	}

	public void updateLimitText(String input) {
		timeLimit = input;
		refreshLimitText();
		timerTextField.setText("");
	}

	public void refreshLimitText() {
		timerLabel.setText("    AI time limit: " + timeLimit + " second(s)");
	}

	public void printTimedOut() {
		infoLabel.setText("    AI timed out! Decreasing depth...");
	}

	public void setAIRecursionDepth(int depth) {
		recursionDepth = String.valueOf(depth);
		recursionLabel.setText("    AI recursion depth: " + recursionDepth);
		infoLabel.setText("    Increasing recursion depth...");
	}

	public void clearInfoText() {
		infoLabel.setText("");

	}

	public void printInvalidMode() {
		infoLabel.setText("Invalid move!");
	}

	public void printScore(int white, int black) {
		if (white > black) {
			infoLabel.setText("White wins! Score: Black: " + black + " White: " + white
					+ "     ");
		} else {
			infoLabel.setText("Black wins! Score: Black: " + black + " White: " + white
					+ "     ");
		}

	}
}
