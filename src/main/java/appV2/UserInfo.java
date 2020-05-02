package appV2;

import java.math.BigInteger;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;

import contracts.BlockTacToe;

/**
 * Manage informations regarding to the user across all scenes
 */
public class UserInfo {


	// player's wallet
	private Credentials cred;	
	//the contract
	private BlockTacToe ticGame;
	// current game ID
	private BigInteger gameID;
	//Spieler 1
	private  String startingPlayer; 	
	//Integervalue des Spielers, 1 (Spieler 1), 2 (Spieler 2); nötig für ausführen von Zügen
	private int playerintvalue;	
	// current turn
	private int turn;
	private BigInteger walletBalance;
	private BigInteger contractBalance;


	public int getPlayerintvalue() {
		return playerintvalue;
	}
	
	public void setPlayerintvalue(int value) {
		this.playerintvalue = value;
	}	
	
	public BigInteger getWalletBalance() {
		return walletBalance;
	}

	public void setWalletBalance(BigInteger walletBalance) {
		this.walletBalance = walletBalance;
	}

	public BigInteger getContractBalance() {
		return contractBalance;
	}

	public void setContractBalance(BigInteger contractBalance) {
		this.contractBalance = contractBalance;
	}

	public BlockTacToe getTicGame() {
		return ticGame;
	}

	public void setTicGame(BlockTacToe ticGame) {
		this.ticGame = ticGame;
	}

	public void setTurn(int turn) {
		this.turn = turn;
	}

	public int getTurn() {
		return turn;
	}

	public void setGameID(BigInteger id) {
		gameID = id;
	}

	public BigInteger getGameID() {
		return gameID;
	}

	public Credentials getCred() {
		return cred;
	}

	public void setCred(String pw, String path) {
		try {
			cred = WalletUtils.loadCredentials(pw, path);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setCred(String privkey) {
		cred = Credentials.create(privkey);
	}

	public void setStartingPlayer(String adress) {
		startingPlayer = adress;
	}

	public String getStartingPlayer() {
		return startingPlayer;
	}

}
