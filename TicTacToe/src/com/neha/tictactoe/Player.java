package com.neha.tictactoe;

public enum Player {
	CROSS, CIRCLE, EMPTY;
	
	@Override
	public String toString() {
		switch(this) {
			case CROSS :
				return "X";
			case CIRCLE :
				return "O";
			default : 
				return "";
		}
	}
}
