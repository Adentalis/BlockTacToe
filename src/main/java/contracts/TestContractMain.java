package contracts;

import static contractUtils.NameFactory.WALLET1;
import static contractUtils.NameFactory.WALLET1PW;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;
import org.web3j.utils.Convert;

import contractUtils.GasLogger;

public class TestContractMain {
	public static void main(String[] args) {
	
	
	boolean deployed = true;
	// addresse des Smart Contracts
	String address = "0x0fe21e77d2168c98d0840850b7ca89b0142d310a";
	//Verbindung zu einer ETH Node
	Web3j web3j = Web3j.build(new HttpService("https://ropsten.infura.io/uhnXLnEun9ncsmwv0iku"));

	try {
		//richte die Wallets ein
		System.out.println(("Connected to Ethereum client version: "
				+ web3j.web3ClientVersion().send().getWeb3ClientVersion()));
		
		Credentials credentials2 = WalletUtils.loadCredentials(WALLET1PW,
				WALLET1);
		System.out.println("Loaded credentials");
		
		
		//Damit "instanziere" ich den SC als Java Klasse 
		TestContract ticGame;
		if (!deployed) {
			System.out.println("Deploying contract");
			ticGame = TestContract.deploy(web3j, credentials2, BigInteger.valueOf(22000000000L), BigInteger.valueOf(10000000)).send();
			System.out.println("Deployed contract");
		} else {
			System.out.println("Loading contract");
			ticGame = TestContract.load(address, web3j, credentials2, BigInteger.valueOf(10000000000L), Contract.GAS_LIMIT);
			System.out.println("Loaded contract");
		}
		
		//send moneyz
		
		BigInteger toDeposit = Convert.toWei("0.1", Convert.Unit.ETHER).toBigInteger();
		GasLogger gaslogger = new GasLogger();
		TransactionReceipt tr = ticGame.deposit(toDeposit).send();
		String key = "deposit";
		gaslogger.addTransaction(key , tr.getGasUsed().intValue());
		System.out.println(gaslogger.getAllInformationForTransaction(key));
		
		//did moneyz get goten? 
		
		BigInteger balance = ticGame.balances(credentials2.getAddress()).send();
		BigDecimal balanceInGwei = Convert.fromWei(new BigDecimal(balance), Convert.Unit.WEI);
		System.out.println(credentials2.getAddress() + " has a balance of: "+ balanceInGwei+ " Gwei");
 
	} catch (Exception e) {
		e.printStackTrace();
	}																						// token here;
	
	
}}

