package ai;

import game.Board;
import game.Coordinate;

import java.util.LinkedList;

import util.Timer;

public class Agent {
	private final Coordinate nullMove = new Coordinate(-1, -1);
	private Timer timer;
	private final int aiColour;
	private Coordinate bestMove;
	private int maxDepth = 1;
	private double timeLimit = 1;
	private boolean timedOut = false;

	public Agent(int color, int timeLimit, int maxDepth) {
		aiColour = color;
		this.timeLimit = timeLimit;
		this.maxDepth = maxDepth;
		timer = new Timer();
	}

	public Board placeDisk(Board board) {
		return board.placeDisk(calculateBestMove(board), aiColour);
	}

	private Coordinate calculateBestMove(Board board) {
		bestMove = nullMove;
		timer.startTimer(timeLimit);
		alfaBeta(board, 0, maxDepth, Integer.MIN_VALUE, Integer.MAX_VALUE,
				aiColour);
		timedOut = timer.timedOut();
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

		//If maximizing player:
		if (currentPlayer == aiColour) {
			for (Coordinate move : legalMoves) {
				Board newBoard = board.placeDisk(move, currentPlayer);

				if (timer.timedOut()) {
					return alpha;
				}

				int score = alfaBeta(newBoard, depth + 1, maxDepth, alpha,
						beta, changePlayer(currentPlayer));
				if (score > alpha) {
					alpha = score;
					if (depth == 0) {
						bestMove = move;
					}
				}
				if (beta <= alpha) {
					break;
				}
			}
			return alpha;
		}

		//If minimizing player:
		for (Coordinate move : legalMoves) {
			Board newBoard = board.placeDisk(move, currentPlayer);

			if (timer.timedOut()) {
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

	public boolean timedOut() {
		return timedOut;
	}

	public int decreaseDepth() {
		maxDepth--;
		return maxDepth;
	}

	public int increaseDepth() {
		maxDepth++;
		return maxDepth;
	}
}
