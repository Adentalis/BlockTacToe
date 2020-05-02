package common;

public abstract class AbstractField<Figure extends AbstractFigure> {

	protected int fieldNumber;
	protected String name;
	protected Figure figure;
	
	public AbstractField(int fieldNumber, String name) {		
		this.fieldNumber = fieldNumber;
		this.name = name;
	}
	
	public Figure getFigure() {
		return figure;
	}

	public void setFigure(Figure figure) {
		this.figure = figure;
	}
	
	public int getFieldNumber() {
		return fieldNumber;
	}
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "AbstractField [fieldNumber=" + fieldNumber + ", name=" + name + "]";
	}
}
