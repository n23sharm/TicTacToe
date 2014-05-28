package com.neha.tictactoe;

public class Move {
	public int row;
	public int column;
	public Player player;
	
	public Move(int row, int column, Player player) {
		this.row = row;
		this.column = column;
		this.player = player;
	}
}
