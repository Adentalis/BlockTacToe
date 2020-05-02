package appV2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import contractUtils.GasLogger;
import contracts.BlockTacToe;
import javafx.application.Platform;

public abstract class TransactionReceiver implements Runnable{

	protected BlockTacToe contract;
	protected GasLogger logger;
	protected String key;
	protected ThreadFinishListener listener;
	protected static final Logger log = LoggerFactory.getLogger(TransactionReceiver.class);
	
	public  TransactionReceiver(ThreadFinishListener listener, BlockTacToe contract, GasLogger logger, String key) {	
		this.contract = contract;
		this.logger = logger;
		this.key = key;
		this.listener = listener;
	}
	
	public abstract TransactionReceipt function() throws Exception;	
	
	@Override
	public void run() {
		log.info("started transaction");
		TransactionReceipt tr;
		try {
			tr = function();
			logger.addTransaction(key, tr.getGasUsed().intValue());
			log.info("finished transaction");
			Platform.runLater(listener);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		
	}
}