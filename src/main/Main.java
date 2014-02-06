package main;

import game.Board;
import controller.Controller;

public class Main {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Board board = new Board();
		Controller controller = new Controller(board);
	}
}