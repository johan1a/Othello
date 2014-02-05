package game;

import java.util.LinkedList;

public class Board {
	public final static int EMPTY = 0;
	public final static int WHITE = 1;
	public final static int BLACK = 2;
	public static final int BOARD_SIZE = 8;
	private int[][] board;

	public Board() {
		board = new int[BOARD_SIZE][BOARD_SIZE];
	}

	public void newGame() {
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				board[i][j] = EMPTY;
			}
		}

		board[3][3] = WHITE;
		board[4][4] = WHITE;
		board[3][4] = BLACK;
		board[4][3] = BLACK;
	}

	public Board placeDisk(Coordinate c, int colour) {
		return placeDisk(c.getX(), c.getY(), colour);
	}

	public Board placeDisk(int x, int y, int colour) {
		Board newBoard = null;
		newBoard = this.clone();

		newBoard.setTile(x, y, colour);
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				int square = newBoard.getTile(x + i, y + j);
				int k = x + i;
				int l = y + j;
				while (square != Board.EMPTY && square != colour
						&& square != -1) {
					k += i;
					l += j;
					square = newBoard.getTile(k, l);
				}
				if (square == colour) {
					k -= i;
					l -= j;
					square = newBoard.getTile(k, l);
					while (square != Board.EMPTY && square != colour
							&& square != -1) {
						newBoard.setTile(k, l, colour);
						k -= i;
						l -= j;
						square = newBoard.getTile(k, l);
					}
				}
			}
		}
		return newBoard;
	}

	public boolean isLegalMove(int x, int y, int colour) {
		if (getTile(x, y) == Board.EMPTY) {
			for (int i = -1; i < 2; i++) {
				for (int j = -1; j < 2; j++) {
					// int square = getTile(x + i, y + j);
					int k = x + i;
					int l = y + j;
					int square = getTile(k, l);
					while (square != colour && square != Board.EMPTY
							&& square != -1) {
						k += i;
						l += j;
						square = getTile(k, l);
						if (square == colour) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public int evaluate(int colour) {
		int sum = 0;

		int otherColour;
		if (colour == WHITE) {
			otherColour = BLACK;
		} else {
			otherColour = WHITE;
		}

		int tile;
		int value = 0;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				tile = getTile(i, j);
				if (isCorner(i, j)) {
					value = 2;
				} else if (isBorder(i, j)) {
					value = 1;
				} else {
					value = 1;
				}
				if (tile == colour) {
					sum += value;
				} else if (tile != otherColour) {
					sum -= value;
				}
			}
		}
		return sum;
	}

	public LinkedList<Coordinate> getLegalMoves(int color) {
		LinkedList<Coordinate> legalMoves = new LinkedList<Coordinate>();

		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				if (isLegalMove(i, j, color)) {
					legalMoves.add(new Coordinate(i, j));
				}
			}
		}
		return legalMoves;
	}

	public Board clone() {
		Board b = new Board();
		b.setState(getState());
		return b;
	}

	public int[][] getState() {
		int[][] state = new int[BOARD_SIZE][BOARD_SIZE];
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				state[i][j] = board[i][j];
			}
		}
		return state;
	}

	private void setState(int[][] state) {
		board = state;
	}

	public boolean isGameOver() {
		return !canPlaceDisk(WHITE) && !canPlaceDisk(BLACK);
	}

	public boolean canPlaceDisk(int colour) {
		return getLegalMoves(colour).size() != 0;
	}

	public int calculateScore(int colour) {
		int sum = 0;

		int tile;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				tile = getTile(i, j);
				if (tile == colour) {
					sum++;
				}
			}
		}
		return sum;
	}

	private static boolean isBorder(int i, int j) {
		return i == 0 || i == 7 || j == 0 || j == 7;
	}

	private static boolean isCorner(int i, int j) {
		return i == 0 && (j == 0 || j == 7) || i == 7 && (j == 0 || j == 7);
	}

	private int getTile(int i, int j) {
		if (i < 0 || i >= BOARD_SIZE || j < 0 || j >= BOARD_SIZE) {
			return -1;
		}
		return board[i][j];
	}

	private void setTile(int i, int j, int colour) {
		if (!(i < 0 || i >= BOARD_SIZE || j < 0 || j >= BOARD_SIZE)) {
			board[i][j] = colour;
		}
	}
}
