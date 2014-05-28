package com.neha.tictactoe;

public class GameBoard {
	public static final int SIZE = 3;

	private Player[][] mBoard;
	
	public GameBoard() {
		this(null);
	}
	
	public GameBoard(GameBoard gameboard) {
		mBoard = new Player[SIZE][SIZE];
		for (int i = 0; i < mBoard.length; ++i) {
			for (int j = 0; j < mBoard[0].length; ++j) {
				mBoard[i][j] = gameboard != null ? gameboard.getBoard()[i][j] : Player.EMPTY;
			}
		}
	}
	
	public Player[][] getBoard() {
		return mBoard;
	}

	public void clearBoard() {
		for (int i = 0; i < mBoard.length; ++i) {
			for (int j = 0; j < mBoard[0].length; ++j) {
				mBoard[i][j] = Player.EMPTY;
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
		Player player = Player.EMPTY;
		
		// check rows
		for (int i = 0; i < SIZE; ++i) {
			player = mBoard[i][0];
			for (int j = 1; j < SIZE; ++j) {
				if (mBoard[i][j] != player) {
					player = Player.EMPTY;
					break;
				}
			}
			if (player != Player.EMPTY) {
				return player;
			}
		}
		
		// check columns
		for (int i = 0; i < SIZE; ++i) {
			player = mBoard[0][i];
			for (int j = 0; j < SIZE; ++j) {
				if (mBoard[j][i] != player) {
					player = Player.EMPTY;
					break;
				}
			}
			if (player != Player.EMPTY) {
				return player;
			}
		}
		
		// check left-right diagonal
		player = mBoard[0][0];
		for (int i = 0; i < SIZE; ++i) {
			if (mBoard[i][i] != player) {
				player = Player.EMPTY;
				break;
			}
		}
		if (player != Player.EMPTY) {
			return player;
		}
		
		// check right-left diagonal
		player = mBoard[SIZE - 1][0];
		for (int i = SIZE - 1, j = 0; i >= 0 && j < SIZE; --i, ++j) {
			if (mBoard[i][j] != player) {
				player = Player.EMPTY;
				break;
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
