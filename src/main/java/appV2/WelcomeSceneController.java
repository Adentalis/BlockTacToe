package appV2;

import static contractUtils.NameFactory.CONTRACTADDRESS;
import static contractUtils.NameFactory.GASLIMIT;
import static contractUtils.NameFactory.GASPRICE;
import static contractUtils.NameFactory.INFURATOKEN;
import static contractUtils.NameFactory.WALLET1;
import static contractUtils.NameFactory.WALLET1PW;
import static contractUtils.NameFactory.WALLET2;
import static contractUtils.NameFactory.WALLET2PW;
import static contractUtils.NameFactory.WALLET3KEY;
import static contractUtils.NameFactory.WALLET4KEY;
import static contractUtils.NameFactory.WALLET5KEY;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import contractUtils.GasLogger;
import contracts.BlockTacToe;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class WelcomeSceneController {
	
	@SuppressWarnings("unused")
	private GasLogger logger;
	private UserInfo info;
	private BlockTacToeAppV2 app;
	private static final Logger log = LoggerFactory.getLogger(WelcomeSceneController.class);
	//predefined Wallets or private Key
		private ObservableList<String> walletoptions = FXCollections.observableArrayList("Demo-Wallet 1", "Demo-Wallet 2","Demo-Wallet 3" ,"Demo-Wallet 4" ,"Demo-Wallet 5" , "mit Privatekey");
		
		@FXML protected TextField privkeyfield;
		@FXML protected ChoiceBox<String> walletchoice;
		@FXML protected Text privkey;
	
	public WelcomeSceneController (GasLogger logger, UserInfo info, BlockTacToeAppV2 app) {
		this.logger = logger;
		this.info = info;
		this.app = app;
	};
	
	@FXML
	private void initialize() {
		walletchoice.setItems(walletoptions);
		//only show private Key Fields when private key is selected
		walletchoice.getSelectionModel().selectedItemProperty().addListener( (v, oldValue, newValue) -> 
		{
			if(newValue == "mit Privatekey") {
				privkey.setVisible(true);
				privkeyfield.setVisible(true);
			}
			else {
				privkey.setVisible(false);
				privkeyfield.setVisible(false);
			}
		});
		log.info("Initialized WelcomeSceneController");
	}
	
	@FXML
	public void login (ActionEvent event) throws IOException {
		
		if(walletchoice.getSelectionModel().getSelectedItem() == "Demo-Wallet 1") {
			info.setCred(WALLET1PW, WALLET1);			
		}
		else if(walletchoice.getSelectionModel().getSelectedItem() == "Demo-Wallet 2") {
			info.setCred(WALLET2PW, WALLET2);			
		}
		else if(walletchoice.getSelectionModel().getSelectedItem() == "Demo-Wallet 3") {
			info.setCred(WALLET3KEY);			
		}
		else if(walletchoice.getSelectionModel().getSelectedItem() == "Demo-Wallet 4") {
			info.setCred(WALLET4KEY);			
		}
		else if(walletchoice.getSelectionModel().getSelectedItem() == "Demo-Wallet 5") {
			info.setCred(WALLET5KEY);			
		}
		else if (walletchoice.getSelectionModel().getSelectedItem() == "mit Privatekey") {
			if(WalletUtils.isValidPrivateKey(privkeyfield.getText())) {
				info.setCred(privkeyfield.getText());
			}else {
				log.error("Invalid private Key entered!");
				new AlertBox("BlockTacToe","Private Key ungültig");
				return;
			}
		}
		log.info("Using credentials "+info.getCred().toString()+" to load contract.");
		//Läd den Smart Contract mit der Wallet, die ausgewählt wurde --> für deposit etc nötig
		
		info.setTicGame(BlockTacToe.load(CONTRACTADDRESS, Web3j.build(new HttpService(INFURATOKEN)), info.getCred(), GASPRICE, GASLIMIT));
		log.info("Loaded contract.");
		app.switchToHomeScene();
	}
	
	@FXML
	private void quit() {
		Platform.exit();
	}
}