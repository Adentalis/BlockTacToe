package tictactoe;

import java.util.ArrayList;
import java.util.List;

import common.AbstractBoard;

public class TicTacToeBoard extends AbstractBoard<TicTacToeField> implements Cloneable {

	public TicTacToeBoard() {
		fields = new ArrayList<TicTacToeField>();
		for (int i = 0; i < 9; i++) {
			String fieldName = (i / 3 + 1) + "/" + (i % 3 + 1);
			fields.add(new TicTacToeField(i + 1, fieldName));
		}
	}

	private TicTacToeBoard(List<TicTacToeField> fields) {
		this.fields = new ArrayList<TicTacToeField>();
		this.fields.addAll(fields);
	}

	@Override
	public List<TicTacToeField> getFields() {
		return super.getFields();
	}

	@Override
	public Object printBoard() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < fields.size(); i++) {
			sb.append(fields.get(i).printField());
			if (i == 2 || i == 5) {
				sb.append("\n-----\n");
			} else if (i != fields.size() - 1) {
				sb.append("|");
			}
		}
		return sb.toString();
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return new TicTacToeBoard(fields);
	}

}
