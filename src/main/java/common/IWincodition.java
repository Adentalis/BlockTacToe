package common;

public interface IWincodition <Player extends AbstractPlayer>{

	boolean isGameWon();
	Player  getWinner();
	
}
