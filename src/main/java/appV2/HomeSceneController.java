package appV2;

import java.io.IOException;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;

import contractUtils.GasLogger;
import contractUtils.NameFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.text.Text;

public class HomeSceneController implements ThreadFinishListener {

	private GasLogger logger;
	private UserInfo info;
	private BlockTacToeAppV2 app;
	private static final Logger log = LoggerFactory.getLogger(HomeSceneController.class);
	private int state = 0;
	private AlertBox al;

	public HomeSceneController(GasLogger logger, UserInfo info, BlockTacToeAppV2 app) {
		this.logger = logger;
		this.info = info;
		this.app = app;
	};

	// Text mit Balance im Contract
	@FXML
	protected Text EtherBalance;
	// Text mit Balance in der Wallet
	@FXML
	protected Text EtherBalance2;
	// Button, um Spielanfrage zu schicken
	@FXML
	protected Button sendButton;
	@FXML
	protected Text RequestText;

	@FXML
	private void initialize() {
		log.info("initilazing, checking balances and requests");
		checkRequest(null);
	}

	// Methode, um Geld von der eigenen Wallet in den Smart Contract zu überweisen
	@FXML
	public void depositToContract() {
		// Dialog
		TextInputDialog depositether = new TextInputDialog("in Ether");
		depositether.setTitle("Ether an den Smart Contract überweisen");
		depositether.setHeaderText("Wie viel Ether wollen Sie in den Smart Contract einzahlen?");
		depositether.setContentText("Betrag:");

		Optional<String> result = depositether.showAndWait();
		result.ifPresent(amount -> {
			// "Antwort" des Dialogs als BigInteger
			BigInteger weiValue;
			try {
				 weiValue = Convert.toWei(amount, Convert.Unit.ETHER).toBigInteger();
			}catch (NumberFormatException nfe){
				new AlertBox("BlockTacToe", "Ungultiges Zahlenformat. Bitte im Format x.xxx... eingeben.");
				return;
			}
			if (((getWalletBalance() * 1000000000000000000f) - weiValue.doubleValue()) < 1000000000000000f) {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("BlockTacToe");
				alert.setHeaderText("Ihr Walletbalance ist sehr niedrig!");
				alert.setContentText(
						"Wenn Sie trotzdem den Betrag überweisen kann es sein, dass Sie zu wenig Ether für Transaktionen übrig haben. Wollen Sie trotzdem überweisen?");

				Optional<ButtonType> r = alert.showAndWait();
				if (r.get() == ButtonType.OK) {
					al = new AlertBox();
					try {
						log.info("started deposit with low balance");
						TransactionReceiver rec = new TransactionReceiver(this, info.getTicGame(), logger, "deposit") {

							@Override
							public TransactionReceipt function() throws Exception {
								return contract.deposit(weiValue).send();
							}
						};
						Thread t = new Thread(rec);
						t.start();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else {
				try {
					al = new AlertBox();
					log.info("started deposit");
					TransactionReceiver rec = new TransactionReceiver(this, info.getTicGame(), logger, "deposit") {

						@Override
						public TransactionReceipt function() throws Exception {
							return contract.deposit(weiValue).send();
						}
					};
					Thread t = new Thread(rec);
					t.start();
				} catch (Exception e) {
					log.error(e.getMessage());
				}
			}
		});

	}

	// Aktualisiert die Anzeige "Kontostand im Smart Contract"
	public void updateContractBalanceDisplay(double balance) {
		// Formatiert die Ausgabe in eine Dezimalzahl mit 15 Nachkommastellen
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(15);
		String ethbalance = df.format(balance);
		EtherBalance.setText(ethbalance + " Ether");
	}

	// Methode, um den aktuellen Kontostand (von der eigenen Wallet aus) im Smart
	// Contract auszugeben
	public double getContractBalance() {
		try {
			// gibt den aktuellen Kontostand auf dem Smart Contract aus (in wei)
			BigInteger balance = info.getTicGame().balances(info.getCred().getAddress()).send();
			// Umwandlung von Wei in Ether
			info.setContractBalance(balance);
			log.info(balance.toString());
			return Convert.fromWei(balance.toString(), Convert.Unit.ETHER).doubleValue();
		} catch (Exception e) {
			log.error(e.getMessage());
			// Rückgabewert, falls ticGame.balances fehlschlägt
			return -1;
		}

	}

	// Aktualisiert die Anzeige "Kontostand auf dem Smart Contract"
	public void updateWalletBalanceDisplay(double balance) {
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(15);
		String ethbalance = df.format(balance);
		EtherBalance2.setText(ethbalance + " Ether");
	}

	// Methode, um den aktuellen Kontostand auf dem eigenen Smart Contract
	// auszugeben
	public double getWalletBalance() {
		// Verbindet sich mit web3j
		Web3j web3 = Web3j.build(new HttpService(NameFactory.INFURATOKEN));
		try {
			// liest mit ethGetBalance den Kontostand aus
			BigInteger balance = web3.ethGetBalance(info.getCred().getAddress(), DefaultBlockParameterName.LATEST)
					.send().getBalance();
			// umrechnung in Ether
			info.setWalletBalance(balance);
			return Convert.fromWei(balance.toString(), Convert.Unit.ETHER).doubleValue();
		} catch (Exception e) {
			log.error(e.getMessage());
			return -1;
		}
	}

	public void sendRequest(ActionEvent event) {
		// less than 0.1 Ether in Contract
		if (info.getContractBalance().longValue() < 100000000) {
			new AlertBox("BlockTacToe", "Niedriger Kontostand, bitte einzahlen.");
			log.error("low balance");
			return;
		}
		if (state == 0 || state == 2) {
			try {
				al = new AlertBox();
				log.info("send game request");
				TransactionReceiver rec = new TransactionReceiver(this, info.getTicGame(), logger, "send request") {

					@Override
					public TransactionReceipt function() throws Exception {
						return contract.randomOpponent().send();
					}
				};
				Thread t = new Thread(rec);
				t.start();

			} catch (Exception e) {
				log.error(e.getMessage());
			}
		} else {
			new AlertBox("BlockTacToe", "Anfrage bereits gesendet oder Spiel läuft schon");
		}
		checkRequest(event);
	}

	@FXML
	public void accept(ActionEvent e) throws IOException {
		if (state == 3) {
			state = 0;
			app.switchToGameScene();
		} else {
			new AlertBox("BlockTacToe", "Match noch nicht gefunden");
		}
	}

	@FXML
	public void retract(ActionEvent e) {
		checkRequest(e);
		if (state == 1) {
			try {
				al = new AlertBox();
				log.info("send retract");
				TransactionReceiver rec = new TransactionReceiver(this, info.getTicGame(), logger, "retract request") {

					@Override
					public TransactionReceipt function() throws Exception {
						return contract.retractRequest().send();
					}
				};
				Thread t = new Thread(rec);
				t.start();
			} catch (Exception e1) {
				log.error(e1.getMessage());
			}
		} else {
			String alert = state == 3 ? "Spiel bereits gestartet" : "Kein aktives Spiel";
			new AlertBox("BlockTacToe", alert);
		}
		checkRequest(e);
	}

	@FXML
	public void checkRequest(ActionEvent e) {
		try {
			updateContractBalanceDisplay(getContractBalance());
			updateWalletBalanceDisplay(getWalletBalance());
			boolean active = info.getTicGame().hasActiveRequest(info.getCred().getAddress()).send();
			if (active) {
				RequestText.setText("Offene Anfrage, warte auf Gegner.");
				state = 1;
			} else {
				BigInteger id = info.getTicGame().playerGameId(info.getCred().getAddress()).send();
				if (id.intValue() != 0) {
					state = 3;
					RequestText.setText("Match gefunden.");
					info.setGameID(id);
				} else {
					state = 2;
					RequestText.setText("Keine offene Anfrage.");
				}
			}
		} catch (Exception e1) {
			log.error(e1.getMessage());
		}
	}

	@FXML
	public void withdraw(ActionEvent e) {
		try {
			al = new AlertBox();
			log.info("withdraw money");
			TransactionReceiver rec = new TransactionReceiver(this, info.getTicGame(), logger, "withdraw") {

				@Override
				public TransactionReceipt function() throws Exception {
					return contract.withdraw().send();
				}
			};
			Thread t = new Thread(rec);
			t.start();
		} catch (Exception e1) {
			log.error(e1.getMessage());
		}
		log.info("updating display");
		updateContractBalanceDisplay(getContractBalance());
		updateWalletBalanceDisplay(getWalletBalance());
	}

	@FXML
	public void logout() throws Exception {
		app.switchToWelcomeScene();
	}
	
	@FXML
	public void logLogger() {
		log.info(logger.getAllInformation());
	}
	
	@Override
	public void onSuccess() {
		if (al != null)
			al.close();
		updateContractBalanceDisplay(getContractBalance());
		updateWalletBalanceDisplay(getWalletBalance());
	}
}