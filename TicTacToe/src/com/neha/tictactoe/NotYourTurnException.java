package com.neha.tictactoe;

public class NotYourTurnException extends Exception {
	
	private static final long serialVersionUID = 1538600708779235770L;

	public NotYourTurnException() {
        super("It's not your turn!");
    }

}
