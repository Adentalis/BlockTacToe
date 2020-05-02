package contractUtils;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class GasLogger {

	// Gwei per Gas
	private static final int GasPrice = NameFactory.GASPRICE.divide(new BigInteger("1000000000")).intValue();
	// Dollar per Ether
	private static final double EtherPrice = 621f;
	// Gwei to Ether Faktor
	private static final int GweiPerEther = 1000000000;

	private Map<String, Integer> gasPaid;

	public GasLogger() {
		gasPaid = new HashMap<>();
	}

	public int getGasForTransactions(String key) {
		return gasPaid.get(key);
	}

	public int getGasForAllTransactions() {
		return gasPaid.entrySet().stream().mapToInt(e -> (Integer) e.getValue()).sum();
	}

	public double getDollarForAllTransactions() {
		return getGweiForAllTransactions() * EtherPrice / GweiPerEther;
	}

	public double getGweiForAllTransactions() {
		return getGasForAllTransactions() * GasPrice;
	}

	public double getDollarForTransaction(String key) {
		return getGweiForTransaction(key) * EtherPrice / GweiPerEther;
	}

	public double getGweiForTransaction(String key) {
		return gasPaid.get(key) * GasPrice;
	}

	public double getEtherForTransaction(String key) {
		return getGweiForTransaction(key) / GweiPerEther;
	}

	public double getEtherForAllTransactions() {
		return getGweiForAllTransactions() / GweiPerEther;
	}

	public void addTransaction(String key, Integer gasAmount) {
		if (!gasPaid.containsKey(key)) {
			gasPaid.put(key, gasAmount);
		} else {
			gasPaid.put(key, gasPaid.get(key) + gasAmount);
		}
	}

	public String getAllInformationForTransaction(String key) {
		return String.format("Transaction: %s, Gas Price: %d, Gwei Price: %.0f, Ether Price: %f, Dollar Price: %.3f", key,
				getGasForTransactions(key), getGweiForTransaction(key), getEtherForTransaction(key),
				getDollarForTransaction(key));
	}
	
	public String getAllInformation() {
		StringBuilder sb = new StringBuilder();
		for(String key : gasPaid.keySet()) {
			sb.append(getAllInformationForTransaction(key));
			sb.append("\n");
		}
		return sb.toString();
	}
}
