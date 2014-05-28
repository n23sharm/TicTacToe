package com.neha.tictactoe;

public interface GameListener {
	public void onBoardUpdated(Player[][] board);
	public void onGameOver(Player winner);
	public void onCurrentPlayerChanged(Player currentPlayer);
}
