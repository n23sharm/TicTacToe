package com.neha.tictactoe;

public class GameBoard {
	public static final int SIZE = 3;

	private Player[][] mBoard;
	private boolean[][] mWinnerBoard;
	
	public GameBoard() {
		this(null);
	}
	
	public GameBoard(GameBoard gameboard) {
		mBoard = new Player[SIZE][SIZE];
		mWinnerBoard = new boolean[SIZE][SIZE];
		for (int i = 0; i < mBoard.length; ++i) {
			for (int j = 0; j < mBoard[0].length; ++j) {
				mBoard[i][j] = gameboard != null ? gameboard.getBoard()[i][j] : Player.EMPTY;
				mWinnerBoard[i][j] = false;
			}
		}
	}
	
	public Player[][] getBoard() {
		return mBoard;
	}
	
	public boolean[][] getWinningBoard() {
		return mWinnerBoard;
	}

	public void clearBoard() {
		for (int i = 0; i < mBoard.length; ++i) {
			for (int j = 0; j < mBoard[0].length; ++j) {
				mBoard[i][j] = Player.EMPTY;
				mWinnerBoard[i][j] = false;
			}
		}
	}
	
	public boolean isBoardFull() {
		for (int i = 0; i < mBoard.length; ++i) {
			for (int j = 0; j < mBoard[0].length; ++j) {
				if (mBoard[i][j] == Player.EMPTY) {
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean isGameOver() {
		return getWinner() != Player.EMPTY || isBoardFull();
	}
	
	public Player getWinner() {
		return getWinner(false);
	}
	
	public Player getWinner(boolean populateWinnerBoard) {
		Player player = Player.EMPTY;
		
		// check rows
		for (int row = 0; row < SIZE; ++row) {
			player = mBoard[row][0];
			for (int column = 0; column < SIZE; ++column) {
				if (mBoard[row][column] != player) {
					player = Player.EMPTY;
					break;
				}
			}

			if (player != Player.EMPTY) {
				if (populateWinnerBoard) {
					for (int column = 0; column < SIZE; ++column) {
						mWinnerBoard[row][column] = true;
					}
				}
				
				return player;
			}
		}
		
		// check columns
		for (int column = 0; column < SIZE; ++column) {
			player = mBoard[0][column];
			for (int row = 0; row < SIZE; ++row) {
				if (mBoard[row][column] != player) {
					player = Player.EMPTY;
					break;
				}
			}

			if (player != Player.EMPTY) {
				if (populateWinnerBoard) {
					for (int row = 0; row < SIZE; ++row) {
						mWinnerBoard[row][column] = true;
					}
				}
				
				return player;
			}
		}
		
		// check left-right diagonal
		player = mBoard[0][0];
		for (int row = 0, column = 0; row < SIZE && column < SIZE; ++row, ++column) {
			if (mBoard[row][column] != player) {
				player = Player.EMPTY;
				break;
			}
		}
		if (player != Player.EMPTY) {
			if (populateWinnerBoard) {
				for (int row = 0, column = 0; row < SIZE && column < SIZE; ++row, ++column) {
					mWinnerBoard[row][column] = true;
				}
			}
			
			return player;
		}
		
		// check right-left diagonal
		player = mBoard[SIZE - 1][0];
		for (int row = SIZE - 1, column = 0; row >= 0 && column < SIZE; --row, ++column) {
			if (mBoard[row][column] != player) {
				player = Player.EMPTY;
				break;
			}
		}
			
		if (player != Player.EMPTY && populateWinnerBoard) {
			for (int row = SIZE - 1, column = 0; row >= 0 && column < SIZE; --row, ++column) {
				mWinnerBoard[row][column] = true;
			}
		}
		
		return player;
	}
	
	public boolean isMovePossible(int row, int column) {
		return mBoard[row][column] == Player.EMPTY;
	}
	
	public void playMove(Move move) {
		mBoard[move.row][move.column] = move.player;
	}
	
	public void undoMove(Move move) {
		mBoard[move.row][move.column] = Player.EMPTY;
	}
	
}
