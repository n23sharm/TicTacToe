package com.neha.tictactoe;

import java.util.Random;

import android.os.AsyncTask;

public class GameManager {
	private static final int MAX_BOARD_SCORE = 10;
	
	private Player mComputerPlayer;
	private Player mHumanPlayer;
	private GameBoard mGameBoard;
	private Player mCurrentPlayer;
	private GameListener mGameListener;
	
	private AsyncTask<Void, Void, MinMaxResult> mMinMaxTask;
	
	public GameManager(GameListener gameListener) {
		mGameBoard = new GameBoard();
		mGameListener = gameListener;
	}
	
	private void updateCurrentPlayer(Player currentPlayer) {
		mCurrentPlayer = currentPlayer;
		
		if (mGameListener != null) {
			mGameListener.onCurrentPlayerChanged(mCurrentPlayer);
		}
	}
	
	public void startGame(boolean isComputerFirst) {
		mGameBoard.clearBoard();
		
		mComputerPlayer = isComputerFirst ? Player.CROSS : Player.CIRCLE;
		mHumanPlayer = isComputerFirst ? Player.CIRCLE : Player.CROSS;

		updateCurrentPlayer(isComputerFirst ? mComputerPlayer : mHumanPlayer);
		if (isComputerFirst) {
			playComputerMove(true);
		}

		if (mGameListener != null) {
			mGameListener.onBoardUpdated(mGameBoard.getBoard());
		}
	}

	public Player getHumanPlayer() {
		return mHumanPlayer;
	}
	
	public Player getComputerPlayer() {
		return mComputerPlayer;
	}
	
	public void playHumanMove(int row, int column) throws NotYourTurnException, InvalidMoveException {
		if (mCurrentPlayer != mHumanPlayer) {
			throw new NotYourTurnException();
		}
		
		if (!mGameBoard.isMovePossible(row, column)) {
			throw new InvalidMoveException();
		}

		mGameBoard.playMove(new Move(row, column, mHumanPlayer));
		if (mGameListener != null) {
			mGameListener.onBoardUpdated(mGameBoard.getBoard());
		}
		
		if (mGameBoard.isGameOver()) {
			if (mGameListener != null) {
				Player winner = mGameBoard.getWinner(true);
				mGameListener.onGameOver(winner, mGameBoard.getWinningBoard());
			}
		} else {
			updateCurrentPlayer(mComputerPlayer);
			playComputerMove(false);
		}
	}
	
	public void playComputerMove(final boolean isFirstMoveOfGame) {
		if (mMinMaxTask == null || mMinMaxTask.getStatus() != AsyncTask.Status.RUNNING) {
			mMinMaxTask = new AsyncTask<Void, Void, MinMaxResult>() {
				@Override
				protected MinMaxResult doInBackground(Void... params) {
					// If we are playing the first move of the game, all moves on the board are
					// equally good. We will randomly pick a row and a column and play that and
					// save all the time calculating the minmax results.
					if (isFirstMoveOfGame) {
						Random rand = new Random();
						int row = rand.nextInt(3);
						int column = rand.nextInt(3);
						
						return new MinMaxResult(new Move(row, column, mComputerPlayer), 0);
					} else {
						GameBoard gameboard = new GameBoard(mGameBoard);
						return  bestMinMaxMove(gameboard, 0, mComputerPlayer);
					}
				}
				
				@Override
				protected void onPostExecute(MinMaxResult result) {
					Move move = result.move;
					mGameBoard.playMove(move);

					if (mGameListener != null) {
						mGameListener.onBoardUpdated(mGameBoard.getBoard());
					}
					
					if (mGameBoard.isGameOver()) {
						if (mGameListener != null) {
							Player winner = mGameBoard.getWinner(true);
							mGameListener.onGameOver(winner, mGameBoard.getWinningBoard());
						}
					} else {
						updateCurrentPlayer(mHumanPlayer);
					}
				}
			};
			mMinMaxTask.execute();
		}
	}
	
	private MinMaxResult bestMinMaxMove(GameBoard gameBoard, int depth, Player currentPlayer) {
		Player winner = gameBoard.getWinner();
		if (winner == mComputerPlayer) {
			return new MinMaxResult(null, MAX_BOARD_SCORE - depth);
		} else if (winner == mHumanPlayer) {
			return new MinMaxResult(null, depth - MAX_BOARD_SCORE);
		} else if (gameBoard.isBoardFull()) {
			return new MinMaxResult(null, 0);
		}
		
		depth++;
		MinMaxResult bestMove = null;
		boolean maximize = currentPlayer == mComputerPlayer ? true : false;
		
		for (int i = 0; i < GameBoard.SIZE; ++i) {
			for (int j = 0; j < GameBoard.SIZE; ++j) {
				if (gameBoard.isMovePossible(i, j)) {
					Move move = new Move(i, j, currentPlayer);
					gameBoard.playMove(move);
					
					// To avoid having to store all moves and perform a sort at each
					// iteration, we keep a running record of the maximum or minimum score
					// depending on what is required. If the player is the computer, we
					// maximize, otherwise we minimize. The recursive call to bestMinMaxMove
					// returns an object that contains a score and the associated move. We
					// replace the move object with our move as we bubble this up so that
					// at the highest level we have the first move in the chain that leads
					// to the best result.
					Player nextPlayer = currentPlayer == Player.CROSS ? Player.CIRCLE : Player.CROSS;
					MinMaxResult potentialBestMove = bestMinMaxMove(gameBoard, depth, nextPlayer);
					potentialBestMove.move = move;
					
					if (maximize) {
						if (bestMove == null || potentialBestMove.score > bestMove.score) {
							bestMove = potentialBestMove;
						}
					} else {
						if (bestMove == null || potentialBestMove.score < bestMove.score) {
							bestMove = potentialBestMove;
						}
					}
					
					// Undo the move so that the board returns back to its
					// previous state
					gameBoard.undoMove(move);
				}
			}
		}
		return bestMove;
	}
	
}
