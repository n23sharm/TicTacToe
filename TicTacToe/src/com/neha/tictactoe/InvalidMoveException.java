package com.neha.tictactoe;

public class InvalidMoveException extends Exception {

	private static final long serialVersionUID = 8012282404073104696L;

	public InvalidMoveException() {
        super("Invalid Move!");
    }
}
