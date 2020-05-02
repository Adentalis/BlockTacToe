package common;

import java.util.List;

public abstract class AbstractBoard <Field extends AbstractField>{

	protected List<Field> fields;
	
	public AbstractBoard() {
	}
	
	public AbstractBoard(List<Field> fields) {
		this.fields = fields;
	}
	abstract public Object printBoard();
	
	public List<Field> getFields() {
		return fields;
	}
	
	public Field getFieldByNumber(final int fieldNumber) {
		return fields.stream().filter(f -> f.getFieldNumber() == fieldNumber).findFirst().get();
	}
}
