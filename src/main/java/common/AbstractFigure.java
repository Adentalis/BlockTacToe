package common;

public abstract class AbstractFigure <Field extends AbstractField, Player extends AbstractPlayer, S> {
	
	protected S symbol;
	protected Player owner;
	protected Field field;
	
	public AbstractFigure(S symbol, Player owner, Field field) {
		this.symbol = symbol;
		this.owner = owner;
		this.field = field;
	}
	
	public S getSymbol() {
		return symbol;
	}
	public void setSymbol(S symbol) {
		this.symbol = symbol;
	}
	public Player getOwner() {
		return owner;
	}
	public void setOwner(Player owner) {
		this.owner = owner;
	}
	public Field getField() {
		return field;
	}
	public void setField(Field field) {
		this.field = field;
	}
	
}
