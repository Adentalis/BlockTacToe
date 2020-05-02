package tictactoe;

import common.AbstractField;

public class TicTacToeField extends AbstractField<TicTacToeFigure>{

	public TicTacToeField(int fieldNumber, String name) {
		super(fieldNumber, name);
	}
	
	protected Object printField() {
		return figure == null ? fieldNumber : figure.getSymbol();
	}
}
