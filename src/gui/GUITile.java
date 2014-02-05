package gui;

import javax.swing.JButton;

/* Needs to have strange method names because of problems with swing */
@SuppressWarnings("serial")
public class GUITile extends JButton {
	private int x, y;

	public GUITile(final int x, final int y) {
		this.x = x;
		this.y = 7 - y;
	}

	public int getOthelloX() {
		return x;
	}

	public int getOthelloY() {
		return y;
	}
}
