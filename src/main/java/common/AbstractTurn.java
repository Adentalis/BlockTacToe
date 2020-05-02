package common;

public abstract class AbstractTurn <Board extends AbstractBoard, Player extends AbstractPlayer, Figure extends AbstractFigure, StartField extends AbstractField, EndField extends AbstractField>{

	protected int turnNumber;
	
	protected Player player;
	protected Figure figure;
	protected StartField startField;
	protected EndField endField;
	protected Board board;
	
	public AbstractTurn(int turnNumber, Player player, Figure figure, StartField startField, EndField endField,
			Board board) {
		super();
		this.turnNumber = turnNumber;
		this.player = player;
		this.figure = figure;
		this.startField = startField;
		this.endField = endField;
		this.board = board;
	}

	public int getTurnNumber() {
		return turnNumber;
	}

	public Player getPlayer() {
		return player;
	}

	public Figure getFigure() {
		return figure;
	}

	public StartField getStartField() {
		return startField;
	}

	public EndField getEndField() {
		return endField;
	}

	public Board getBoard() {
		return board;
	}
	
	abstract protected boolean isValidTurn();
	abstract protected Board getBoardStateAfter();
	
}
