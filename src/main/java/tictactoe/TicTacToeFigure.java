package tictactoe;

import common.AbstractFigure;
import players.SimplePlayer;

public class TicTacToeFigure  extends AbstractFigure<TicTacToeField, SimplePlayer, String> implements Cloneable{

	public TicTacToeFigure(String symbol, SimplePlayer owner) {
		super(symbol, owner, null);
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new TicTacToeFigure(symbol , owner);		
	}
}
