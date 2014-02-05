package ai;

import game.Board;
import game.Coordinate;

import java.util.LinkedList;

public class Agent {
	private final int aiColour;
	private Coordinate bestMove;
	private final int MAX_DEPTH = 5;

	public Agent(int color) {
		aiColour = color;
	}

	public Board placeDisk(Board board) {
		return board.placeDisk(calculateBestMove(board), aiColour);
	}

	private Coordinate calculateBestMove(Board board) {
		alfaBeta(board, 0, MAX_DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE,
				aiColour);
		return bestMove;
	}

	private int alfaBeta(Board board, int depth, int maxDepth, int alpha,
			int beta, int currentPlayer) {
		if (!board.canPlaceDisk(currentPlayer) || depth == maxDepth) {
			return board.evaluate(currentPlayer);
		}

		LinkedList<Coordinate> legalMoves = board.getLegalMoves(currentPlayer);
		if (currentPlayer == aiColour) {
			for (Coordinate move : legalMoves) {
				Board newBoard = board.placeDisk(move, currentPlayer);
				alpha = Math.max(
						alpha,
						alfaBeta(newBoard, depth + 1, maxDepth, alpha, beta,
								changePlayer(currentPlayer)));

				if (beta <= alpha) {
					break;
				}
				bestMove = move;
			}
			return alpha;
		}

		for (Coordinate move : legalMoves) {
			Board newBoard = board.placeDisk(move, currentPlayer);
			beta = Math.min(
					beta,
					alfaBeta(newBoard, depth + 1, maxDepth, alpha, beta,
							changePlayer(currentPlayer)));
			if (beta <= alpha) {
					break;
			}
		}
		return beta;
	}

	private static int changePlayer(int player) {
		if (player == Board.WHITE) {
			return Board.BLACK;
		}
		return Board.WHITE;
	}

}
