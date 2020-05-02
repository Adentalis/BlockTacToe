package appV2;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import contractUtils.GasLogger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class GameSceneController implements ThreadFinishListener {
	private GasLogger logger;
	private UserInfo info;
	private BlockTacToeAppV2 app;
	private AlertBox alert;
	// Aktiver Spieler
	@FXML
	private Text gameText;

	// Die 9 Spielfelder
	@FXML
	private Button B1;
	@FXML
	private Button B2;
	@FXML
	private Button B3;
	@FXML
	private Button B4;
	@FXML
	private Button B5;
	@FXML
	private Button B6;
	@FXML
	private Button B7;
	@FXML
	private Button B8;
	@FXML
	private Button B9;
	
	@FXML
	protected Text opponentText;	
	@FXML
	protected Text zugText;
	@FXML
	protected Text symbolText;
	
	private static final Logger log = LoggerFactory.getLogger(GameSceneController.class);

	public GameSceneController(GasLogger logger, UserInfo info, BlockTacToeAppV2 app) {
		this.logger = logger;
		this.info = info;
		this.app = app;
	};

	public void initialize() {
		try {
			log.info("get starting player from contract");
			info.setStartingPlayer(info.getTicGame().getStartingPlayer(info.getGameID()).send());
			log.info("starting player initialized, setting turn count");
			info.setTurn(info.getTicGame().getTurn(info.getGameID()).send().intValue());
			log.info("get opponent address");
			String opponent = info.getTicGame().getOpponent().send();
			opponentText.setText(opponent);
		} catch (Exception e1) {
			log.error(e1.getMessage());
			e1.printStackTrace();
		}
		setGameText();
		if (!(info.getStartingPlayer().equals(info.getCred().getAddress()))) {
			symbolText.setText("Symbol: O");
			info.setPlayerintvalue(2);
			if (info.getTurn() % 2 == 1) {
				disableButtons();
			}
		} else {
			symbolText.setText("Symbol: X");
			info.setPlayerintvalue(1);
			if (info.getTurn() % 2 == 0) {
				disableButtons();
			}
		}
		
		refresh();
	}

	// Gibt je nach gamestate einen int zurück;-1: fehler beim auslesen des
	// gamestates 0: running; 1: tie; 2: player1win; 3: player2win
	public int getGameState() {
		int gamestate = -1;
		try {
			gamestate = info.getTicGame().getGameState(info.getGameID()).send().intValue();
			log.info("Gamestate: " + gamestate);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return gamestate;
	}

	public void checkWinCondition() {

		boolean isPlayer1 = info.getStartingPlayer().equals(info.getCred().getAddress());
		int gameState = getGameState();
		if (gameState == -1) {
			log.error("failed reading gamestate");
		} else if (gameState == 0) {
			return;
		} else if (gameState == 1) {
			log.info("Game over. Its a tie");
			new AlertBox("BlockTacToe", "Unentschieden!");
		} else if (gameState == 2) {
			if (isPlayer1) {
				log.info("Game over. you won");
				new AlertBox("BlockTacToe", "Du hast gewonnen!");
			} else {
				log.info("Game over. opponent won");
				new AlertBox("BlockTacToe", "Du hast verloren!");
			}
		} else if (gameState == 3) {
			if (isPlayer1) {
				log.info("Game over. opponent won");
				new AlertBox("BlockTacToe", "Du hast verloren!");
			} else {
				log.info("Game over. you won");
				new AlertBox("BlockTacToe", "Du hast gewonnen!");
			}
		}else if (gameState == 4) {
			if (isPlayer1) {
				log.info("Game over. opponent won, you timed out.");
				new AlertBox("BlockTacToe", "Deine Zeit ist abgelaufen, du hast verloren!");
			} else {
				log.info("Game over. you won, opponent timed out.");
				new AlertBox("BlockTacToe", "Die Zeit deines Gegners ist abgelaufen, du hast gewonnen!");
			}
		}else if (gameState == 5) {
			if (isPlayer1) {
				log.info("Game over. you won, opponent timed out.");
				new AlertBox("BlockTacToe", "Die Zeit deines Gegners ist abgelaufen, du hast gewonnen!");
			} else {
				log.info("Game over. opponent won, you timed out.");
				new AlertBox("BlockTacToe", "Deine Zeit ist abgelaufen, du hast verloren!");
			}
		}
		try {
			app.switchToHomeScene();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	// Deaktiviert alle Buttons (wenn Gegner am Zug ist)
	public void disableButtons() {
		B1.setDisable(true);
		B2.setDisable(true);
		B3.setDisable(true);
		B4.setDisable(true);
		B5.setDisable(true);
		B6.setDisable(true);
		B7.setDisable(true);
		B8.setDisable(true);
		B9.setDisable(true);
	}

	// Aktiviert alle Buttons, die noch nicht gesetzt sind (wenn eigener Zug)
	public void enableButtons() {
		if (B1.getText() == "")
			B1.setDisable(false);
		if (B2.getText() == "")
			B2.setDisable(false);
		if (B3.getText() == "")
			B3.setDisable(false);
		if (B4.getText() == "")
			B4.setDisable(false);
		if (B5.getText() == "")
			B5.setDisable(false);
		if (B6.getText() == "")
			B6.setDisable(false);
		if (B7.getText() == "")
			B7.setDisable(false);
		if (B8.getText() == "")
			B8.setDisable(false);
		if (B9.getText() == "")
			B9.setDisable(false);
	}

	// Setzt das entsprechende Symbol in das Feld
	public void setButtonText(int buttonid, int symbolNr) {
		String symbol = symbolNr == 1 ? "X" : "O";
		if (buttonid == 1) {
			B1.setText(symbol);
			B1.setDisable(true);
		} else if (buttonid == 2) {
			B2.setText(symbol);
			B2.setDisable(true);
		} else if (buttonid == 3) {
			B3.setText(symbol);
			B3.setDisable(true);
		} else if (buttonid == 4) {
			B4.setDisable(true);
			B4.setText(symbol);
		} else if (buttonid == 5) {
			B5.setText(symbol);
			B5.setDisable(true);
		} else if (buttonid == 6) {
			B6.setText(symbol);
			B6.setDisable(true);
		} else if (buttonid == 7) {
			B7.setText(symbol);
			B7.setDisable(true);
		} else if (buttonid == 8) {
			B8.setText(symbol);
			B8.setDisable(true);
		} else if (buttonid == 9) {
			B9.setText(symbol);
			B9.setDisable(true);
		}
	}

	// Anzeige welcher Spieler ist an der Reihe
	public void setGameText() {
		String text1;
		String text2;
		boolean starting = false;
		// Überprüft, ob man selber Spieler 1 ist und individualisiert die Anzeige
		// dementsprechend
		if (info.getStartingPlayer().equals(info.getCred().getAddress())) {
			text1 = "Du bist am Zug!";
			text2 = "Gegner ist am Zug!";
			starting = true;
		} else {
			text1 = "Gegner ist am Zug!";
			text2 = "Du bist am Zug!";
		}
		if (info.getTurn() % 2 == 1) {
			gameText.setText(text1);
			if (starting)
				enableButtons();
		} else {
			gameText.setText(text2);
			if (!starting)
				enableButtons();
		}
	}

	private void buttonAction(int number) {
		log.info("Button " + number + " pressed");
		disableButtons();
		BigInteger field = BigInteger.valueOf(number);
		try {
			alert = new AlertBox();
			TransactionReceiver rec = new TransactionReceiver(this, info.getTicGame(), logger, "send Turn  " +info.getTurn()+" for game: "+info.getGameID()) {

				@Override
				public TransactionReceipt function() throws Exception {
					return contract.doTurn(field).send();
				}
			};
			Thread t = new Thread(rec);
			t.start();
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	@FXML
	public void pressB1() {
		buttonAction(1);
	}

	@FXML
	public void pressB2() {
		buttonAction(2);
	}

	@FXML
	public void pressB3() {
		buttonAction(3);
	}

	@FXML
	public void pressB4() {
		buttonAction(4);
	}

	@FXML
	public void pressB5() {
		buttonAction(5);
	}

	@FXML
	public void pressB6() {
		buttonAction(6);
	}

	@FXML
	public void pressB7() {
		buttonAction(7);
	}

	@FXML
	public void pressB8() {
		buttonAction(8);
	}

	@FXML
	public void pressB9() {
		buttonAction(9);
	}

	@FXML
	public void refresh() {
		log.info("refreshing");
		try {
			@SuppressWarnings("unchecked")
			List<BigInteger> board = info.getTicGame().getBoard(info.getGameID()).send();
			log.info(board.toString());
			for (int i = 0; i < 9; i++) {
				if (board.get(i).intValue() != 0) {
					setButtonText(i + 1, board.get(i).intValue());
				}
			}
			info.setTurn(info.getTicGame().getTurn(info.getGameID()).send().intValue());
			checkWinCondition();
			setGameText();
			log.info("Zug: "+info.getTurn());
			zugText.setText("Zug: "+info.getTurn());

		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	@FXML
	private void quit() {
		Platform.exit();
	}

	
	@Override
	public void onSuccess() {
		if (alert != null)
			alert.close();
		try {
			info.setTurn(info.getTicGame().getTurn(info.getGameID()).send().intValue());
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		refresh();
		log.info("Button turn executed");
	}
}