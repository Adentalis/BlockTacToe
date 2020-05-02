package tictactoe;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import common.AbstractGame;
import common.IWincodition;
import players.SimplePlayer;

public class TicTacToeGame extends AbstractGame<TicTacToeBoard, TicTacToeTurn, SimplePlayer>
		implements IWincodition<SimplePlayer> {

	private List<TicTacToeField> winningRow;

	public TicTacToeGame() {
		super(new TicTacToeBoard(), new SimplePlayer("Player One"), new SimplePlayer("Player Two"));
		turns = new ArrayList<>();
	}

	public TicTacToeGame(SimplePlayer... players) {
		super(new TicTacToeBoard(), players);
		turns = new ArrayList<>();
	}
	
	public TicTacToeGame(List<TicTacToeTurn> turns, SimplePlayer[] players) {
		super(turns.get(turns.size()).getBoardStateAfter(),players);
		this.turns = turns;
	}

	@Override
	public boolean isGameWon() {
		return checkHorizontally() || checkVertically() || checkDiagonal();
	}

	private boolean checkHorizontally() {
		for (int i = 1; i < 10; i++) {
			if (checkThreeFields(i, i+1, i+2)) {
				return true;
			}
			i= i + 2;
		}
		return false;
	}

	private boolean checkVertically() {
		for (int i = 1; i < 3; i++) {
			if (checkThreeFields(i, i + 3, i + 6)) {
				return true;
			}
		}
		return false;
	}

	private boolean checkDiagonal() {
		if (checkThreeFields(1, 5, 9) || checkThreeFields(3, 5, 7)) {
			return true;
		}
		return false;
	}

	private boolean checkThreeFields(int a, int b, int c) {
		ArrayList<TicTacToeField> checkFields = new ArrayList<>();
		checkFields.add(board.getFieldByNumber(a));
		checkFields.add(board.getFieldByNumber(b));
		checkFields.add(board.getFieldByNumber(c));
		if (checkFields.get(0).getFigure() != null //
				&& checkFields.stream().map(f -> f.getFigure() == null ? null : f.getFigure().getOwner()).distinct()
						.count() == 1) {
			winningRow = checkFields;
			return true;
		}
		return false;
	}

	@Override
	public SimplePlayer getWinner() {
		if (!isGameWon()) {
			return null;
		}
		// long numberOfPlayerOneFigures = board.getFields().stream().filter(new
		// NumberOfFigurePredicate(players.get(0)))
		// .count();
		// long numberOfPlayerTwoFigures = board.getFields().stream().filter(new
		// NumberOfFigurePredicate(players.get(1)))
		// .count();
		// return numberOfPlayerOneFigures > numberOfPlayerTwoFigures ? players.get(0) :
		// players.get(1);
		return winningRow.get(0).getFigure().getOwner();
	}

	@SuppressWarnings("unused")
	private class NumberOfFigurePredicate implements Predicate<TicTacToeField> {

		private SimplePlayer player;

		public NumberOfFigurePredicate(SimplePlayer player) {
			this.player = player;
		}

		@Override
		public boolean test(TicTacToeField t) {
			return (t.getFigure().getOwner().equals(player));
		}
	}

	// checks if all fields are filled
	public boolean isGameWinable() {
		long numberOfFigures = board.getFields().stream().filter(f -> f.getFigure() != null).count();
		return numberOfFigures < 9;
	}

	@Override
	protected boolean takeTurn(TicTacToeTurn turn) {
		if(turn.isValidTurn()) {
			this.turns.add(turn);
			this.board = turn.getBoardStateAfter();
			return true;
		}
		return false;
	}

}
