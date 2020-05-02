package tictactoe;

import common.AbstractTurn;
import players.SimplePlayer;

public class TicTacToeTurn extends AbstractTurn<TicTacToeBoard, SimplePlayer, TicTacToeFigure, TicTacToeField, TicTacToeField> {

	public TicTacToeTurn(int turnNumber, SimplePlayer player, TicTacToeFigure figure, TicTacToeField field, TicTacToeBoard board) {
		super(turnNumber, player, figure, field, field, board);
	}

	@Override
	protected boolean isValidTurn() {
		 boolean valid = board.getFieldByNumber(endField.getFieldNumber()).getFigure() == null;
		 return valid;
	}

	@Override
	protected TicTacToeBoard getBoardStateAfter() {
		TicTacToeBoard boardAfter;
		try {
			boardAfter = ((TicTacToeBoard) (board.clone()));
			boardAfter.getFieldByNumber(endField.getFieldNumber()).setFigure(figure);
			return board;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
		
	}

}
