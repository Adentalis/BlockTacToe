package players;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;

public class PlayerWithWallet extends SimplePlayer {

	private Credentials credentials;

	public PlayerWithWallet(String name, Credentials credentials) {
		super(name);
		this.credentials = credentials;
	}

	public PlayerWithWallet(String name, String filePath, String password) {
		super(name);
		try {
			credentials = WalletUtils.loadCredentials(password, filePath);
		} catch (Exception e) {
			
		}
	}

	public Credentials getCredentials() {
		return credentials;
	}

	public String getAddress() {
		return credentials.getAddress();
	}
}
