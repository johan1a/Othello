package gui;

import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GUIBoard extends JPanel {

	private GUITile[][] othelloBoard;
	private final String imgPath = "images/";
	private final String[] imageNames = { "empty", "white", "black" };
	ArrayList<ImageIcon> icons;

	public GUIBoard() {
		initIcons();
	}

	private void initIcons() {
		icons = new ArrayList<ImageIcon>();
		for (int i = 0; i < 3; i++) {
			icons.add(new ImageIcon(imgPath + imageNames[i] + ".png"));
		}
	}

	public void updateBoardState(int[][] state) {
		for (int j = 8 - 1; j >= 0; j--) {
			for (int i = 0; i < 8; i++) {
				othelloBoard[i][7 - j].setIcon(icons.get(state[i][j]));
			}
		}
	}

	public void setTiles(GUITile[][] tiles) {
		this.othelloBoard = tiles;
	}
}
