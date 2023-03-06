package com.mmc.nts.utility;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HasherUtils {
	public static byte[] digest(byte[] input, String algorithm) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException(e);
		}
		byte[] result = md.digest(input);
		return result;
	}
	public static String bytesToHex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			sb.append(String.format("%02x", b));
		}
		return sb.toString();
	}
	public static String getHash(String pass) {
		byte[] pText = pass.getBytes();
		return bytesToHex(HasherUtils.digest(pText, "SHA-256"));
	}
}
