package contractUtils;

import java.security.Key;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Encrypter {
	private static final String ALGORITHM = "AES";
	private static final byte[] KEYVALUE = "ADBSJHJS12547896".getBytes();
	

	public static void main(String[] args) {
		String s = encrypt("600b778de51c22398bc3254ecb91baee2c94d9ed316a329bf90a7372c0148e7e");
		System.out.println(s);
		System.out.println(decrypt(s));
	}

	public static String encrypt(String valueToEnc) {
		try {
			Key key = new SecretKeySpec(KEYVALUE, ALGORITHM);
			Cipher c = Cipher.getInstance(ALGORITHM);
			c.init(Cipher.ENCRYPT_MODE, key);
			byte[] encValue = c.doFinal(valueToEnc.getBytes());
			byte[] encryptedByteValue = Base64.getEncoder().encode(encValue);
			String encryptedValue = new String(encryptedByteValue);
			return encryptedValue;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public static String decrypt(String encryptedValue) {
		try {
			Key key = new SecretKeySpec(KEYVALUE, ALGORITHM);
			Cipher c = Cipher.getInstance(ALGORITHM);
			c.init(Cipher.DECRYPT_MODE, key);
			byte[] decodedValue = Base64.getDecoder().decode(encryptedValue.getBytes());
			byte[] decryptedVal = c.doFinal(decodedValue);
			return new String(decryptedVal);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
