package contractUtils;

import static contractUtils.NameFactory.CONTRACTPATH;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.codegen.SolidityFunctionWrapper;
import org.web3j.utils.Files;

public class ContractGenerator {

	private static final String ABI = ".abi";
	private static final String BIN = ".bin";
	private static final String PACKAGENAME = "contracts";
	private static final String CONTRACTPACKAGEPATH = "../blocktactoe/src/main/java";
	private static final Logger log = LoggerFactory.getLogger(ContractGenerator.class);
	private String contractName;

	public ContractGenerator(String contractName) {
		this.contractName = contractName;
	}

	private boolean generateContract() {
		SolidityFunctionWrapper wrapper = new SolidityFunctionWrapper(true);
		try {
			String binFilePath = CONTRACTPATH + contractName + BIN;
			String abiFilePath = CONTRACTPATH + contractName + ABI;
			log.info("binFile: "+binFilePath+", abiFile: "+abiFilePath);
			
			byte[] bytes = Files.readBytes(new File(binFilePath));
	        String binFile = new String(bytes);



	        bytes = Files.readBytes(new File(abiFilePath));
	        String abiFile = new String(bytes);
			
			wrapper.generateJavaFiles(contractName, binFile,
					abiFile, //
					CONTRACTPACKAGEPATH, PACKAGENAME);
			return true;
		} catch (Exception e) {
			log.error("",e);
			return false;
		}
	}

	public static void main(String[] args) {
		/*
		 * put name of contract here without .sol extension,
		 * like "String contractName = "cat";",
		 * abi- and bin-file need to be in "/blocktactoe/src/main/resources/contracts".
		 * You need to refresh project (F5) after generation, so you can see the contract in eclipse 
		 */

		String contractName = "BlockTacToe";

		
		log.info("Started creating contract: " + contractName);
		if (new ContractGenerator(contractName).generateContract()) {
			log.info("created contract at " + CONTRACTPACKAGEPATH + "/"+PACKAGENAME+"/" + contractName);
		} else {
			log.info("couldn't create contract");
		}
	}

}
