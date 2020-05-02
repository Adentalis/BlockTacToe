package common;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractGame <Board extends AbstractBoard, Turn extends AbstractTurn, Player extends AbstractPlayer>{
	
	protected Board board;
	protected List<Turn> turns;
	protected List<Player> players;
	

	public AbstractGame(Board board, Player...players) {
		this.board = board;
		this.players = Arrays.asList(players);
	}
	
	public AbstractGame(Board board, List<Turn> turns, List<Player> players) {
		super();
		this.board = board;
		this.turns = turns;
		this.players = players;
	}

	
	public Board getBoard() {
		return board;
	}

	public List<Turn> getTurns() {
		return turns;
	}

	public List<Player> getPlayers() {
		return players;
	}
	
	abstract protected boolean takeTurn(Turn turn);

}
