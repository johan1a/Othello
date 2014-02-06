package ai;

import game.Board;
import game.Coordinate;

import java.util.LinkedList;

import util.Timer;

public class Agent {
	private final int aiColour;
	private Coordinate bestMove;
	private final int MAX_DEPTH = 19;
	int testAlpha;
	private final Coordinate nullMove = new Coordinate(-1, -1);
	private Timer timer;
	private double timeLimit = 1;

	public Agent(int color, int timeLimit) {
		aiColour = color;
		this.timeLimit = timeLimit;
		timer = new Timer();
	}

	public Board placeDisk(Board board) {
		return board.placeDisk(calculateBestMove(board), aiColour);
	}

	private Coordinate calculateBestMove(Board board) {
		bestMove = nullMove;
		timer.startTimer(timeLimit);
	
		alfaBeta(board, 0, MAX_DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE,
				aiColour);
		if (timer.timeOut()) {
			System.out.println("AI Time out after " + timer.getSeconds()
					+ " seconds!");
		}
		return bestMove;
	}

	private int alfaBeta(Board board, int depth, int maxDepth, int alpha,
			int beta, int currentPlayer) {
		if (!board.canPlaceDisk(currentPlayer) || depth == maxDepth) {
			return board.evaluate(aiColour);
		}

		LinkedList<Coordinate> legalMoves = board.getLegalMoves(currentPlayer);

		if (bestMove.equals(nullMove)) {
			bestMove = legalMoves.peek();
		}

		if (currentPlayer == aiColour) {
			for (Coordinate move : legalMoves) {
				Board newBoard = board.placeDisk(move, currentPlayer);

				if (timer.timeOut()) {
					if (currentPlayer == aiColour) {
						return alpha;
					}
					return beta;
				}

				int score = alfaBeta(newBoard, depth + 1, maxDepth, alpha,
						beta, changePlayer(currentPlayer));
				if (score > alpha) {
					alpha = score;
					if (depth == 0) {
						bestMove = move;
						testAlpha = alpha;
					}
				}
				if (beta <= alpha) {
					break;
				}
			}
			return alpha;
		}

		for (Coordinate move : legalMoves) {
			Board newBoard = board.placeDisk(move, currentPlayer);

			if (timer.timeOut()) {
				if (currentPlayer == aiColour) {
					return alpha;
				}
				return beta;
			}

			int score = alfaBeta(newBoard, depth + 1, maxDepth, alpha, beta,
					changePlayer(currentPlayer));
			if (score < beta) {
				beta = score;
			}
			if (beta <= alpha) {
				break;
			}
		}
		return beta;
	}

	@SuppressWarnings("unused")
	private int miniMax(Board board, int depth, int maxDepth, int currentPlayer) {
		if (!board.canPlaceDisk(currentPlayer) || depth == maxDepth) {
			return board.evaluate(aiColour);
		}

		int bestScore;
		if (currentPlayer == aiColour) {
			bestScore = Integer.MIN_VALUE;
		} else {
			bestScore = Integer.MAX_VALUE;
		}

		LinkedList<Coordinate> legalMoves = board.getLegalMoves(currentPlayer);

		if (bestMove.equals(nullMove)) {
			bestMove = legalMoves.peek();
		}

		for (Coordinate move : legalMoves) {
			Board newBoard = board.placeDisk(move, currentPlayer);

			int score = miniMax(newBoard, depth + 1, maxDepth,
					changePlayer(currentPlayer));
			if (currentPlayer == aiColour) {
				if (score > bestScore) {
					bestScore = score;
					if (depth == 0) {
						bestMove = move;
						testAlpha = bestScore;
					}
				}
			} else if (score < bestScore) {
				bestScore = score;
			}
		}

		// return bestScore, bestMove
		return bestScore;
	}

	private static int changePlayer(int player) {
		if (player == Board.WHITE) {
			return Board.BLACK;
		}
		return Board.WHITE;
	}

	public void setTimeLimit(double limit) {
		timeLimit = limit;
	}

}
