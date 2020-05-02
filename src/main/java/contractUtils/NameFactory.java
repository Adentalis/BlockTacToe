package contractUtils;

import java.math.BigInteger;

public class NameFactory {
	
	final public static String INFURATOKEN = "https://ropsten.infura.io/uhnXLnEun9ncsmwv0iku";
	final public static String WALLETPATH = "../blocktactoe/src/main/resources/wallets/";
	final public static String CONTRACTPATH = "../blocktactoe/src/main/resources/contracts/";
	final public static String WALLET1 = WALLETPATH + "wallet1.json";
	final public static String WALLET2 = WALLETPATH + "wallet2.json";
	final public static String WALLET1PW_ENCRYPTED = "Kbpy0Vihvglp7VO3yimLzg==";
	final public static String WALLET2PW_ECNRYPTED = "Ff5mwGj3SRsFt56dCsXHVw==";
	final public static String WALLET1PW = Encrypter.decrypt(WALLET1PW_ENCRYPTED);
	final public static String WALLET2PW = Encrypter.decrypt(WALLET2PW_ECNRYPTED);
	final public static String CONTRACTADDRESS = "0x0985f92e57ae2499b98c290a89d8768232c47fcd";
	final public static BigInteger GASPRICE = new BigInteger("15000000000");
	final public static BigInteger GASLIMIT = new BigInteger("4300000");
	final public static long MILLISPERMINUTE = 1L * 1000* 60;
	
	final public static String WALLET3KEYENCRYPTED  = "6cyNZeGurLjyNnbYLvTfOD236Cfz0M75fhk9tNUmtb2iwEmsdK/5xB5dpKrDGebGhvV9F9N9TGBTVV2rnZSU7vnZfT5M1ZiZAmVtee+BMjA=";
	final public static String WALLET3KEY = Encrypter.decrypt(WALLET3KEYENCRYPTED);
	final public static String WALLET4KEYENCRYPTED  = "iefFCoqesoHq5Lc4fNEDMVvA9vJzNyvBCl+KUmCsov4Ba/LRLzo/JksVTd3cS02cWQXJizjATMLDf7oiIDn4YPnZfT5M1ZiZAmVtee+BMjA=";
	final public static String WALLET4KEY = Encrypter.decrypt(WALLET4KEYENCRYPTED);
	final public static String WALLET5KEYENCRYPTED  = "ppxrzR42W2uwL4MA4CXDCReavMrxbQp4xPHrv5h0wpOZyVtv8mjQSWT8uUjqZJRVk/VkmSGU+v3jGgi+QR41pvnZfT5M1ZiZAmVtee+BMjA=";
	final public static String WALLET5KEY = Encrypter.decrypt(WALLET5KEYENCRYPTED);
}
