package tictactoe;

import java.util.Scanner;

import players.SimplePlayer;

public class SimpleCommandLineGame {

	public static void main(String[] args) throws CloneNotSupportedException {
		Scanner input = new Scanner(System.in);
		SimplePlayer player1 = new SimplePlayer("1");
		SimplePlayer player2 = new SimplePlayer("2");
		TicTacToeGame game = new TicTacToeGame(player1, player2);
		int turnNumber = 1;
		TicTacToeFigure figure1 = new TicTacToeFigure("X", player1);
		TicTacToeFigure figure2 = new TicTacToeFigure("O", player2);
		while (!game.isGameWon() && game.isGameWinable()) {
			SimplePlayer playerTurn = turnNumber % 2 == 0 ? player2 : player1;
			System.out.println(game.getBoard().printBoard());
			StringBuilder command = new StringBuilder();
			TicTacToeTurn tictacToeTurn;
			boolean valid;
			do {
				command.append("Turn number: " + turnNumber).append(", Player").append(playerTurn.getName())
						.append(" make a move!");
				System.out.println(command.toString());
				int field = input.nextInt();
				TicTacToeField fieldTurn = game.getBoard().getFieldByNumber(field);
				TicTacToeFigure figureTurn = turnNumber % 2 == 0 ? (TicTacToeFigure) figure2.clone()
						: (TicTacToeFigure) figure1.clone();
				figureTurn.setField(fieldTurn);
				tictacToeTurn = new TicTacToeTurn(turnNumber, playerTurn, figureTurn, fieldTurn,
						(TicTacToeBoard) game.getBoard().clone());
				valid = game.takeTurn(tictacToeTurn);
				if(!valid) {
					System.out.println("Invalid turn!");
				}
			} while (!valid);
			turnNumber++;
		}
		if(!game.isGameWon() && !game.isGameWinable()) {
			System.out.println(game.getBoard().printBoard());
			System.out.println("Game over. Result: draw.");
		}
		if(game.isGameWon()) {
			System.out.println(game.getBoard().printBoard());
			System.out.println("Game over. Result: Player"+game.getWinner().getName()+" won.");
		}
		input.close();
	}

}
