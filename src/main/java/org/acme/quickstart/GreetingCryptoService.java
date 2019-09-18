package org.acme.quickstart;

import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GreetingCryptoService {

		
	private static final int keyLength = 256;
	private static final int iterationCount = 10000000;

	public byte[] encryptData(String key, byte[] data)
			throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
			InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException {

		SecureRandom secureRandom = new SecureRandom();

		// iv/ nounce
		byte[] iv = new byte[12];
		secureRandom.nextBytes(iv);

		// Derivando a senha
		SecretKey secretKey = generateSecretKey(key, iv);

		// Configura modo
		Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
		GCMParameterSpec parameterSpec = new GCMParameterSpec(128, iv);
		
		// Inicializa cifrador em modo cifrador
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);
		
		// Cifrando o arquivo
		byte[] encryptedData = cipher.doFinal(data);

		ByteBuffer byteBuffer = ByteBuffer.allocate(4 + iv.length + encryptedData.length);
		byteBuffer.putInt(iv.length);
		byteBuffer.put(iv);
		byteBuffer.put(encryptedData);
		return byteBuffer.array();
	}

	public byte[] decryptData(String key, byte[] encryptedData)
			throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
			InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException {

		//Empacota o arquivo criptografado para facilitar a leitura 
		ByteBuffer byteBuffer = ByteBuffer.wrap(encryptedData);

		int noonceSize = byteBuffer.getInt();

		// Check no tamanho do nounce 
		if (noonceSize < 12 || noonceSize >= 16) {
			throw new IllegalArgumentException(
					"Tamanho do nounce incorreto. Tenha certeza que o arquivo que esta recebendo foi codificado com modo AES.");
		}
		byte[] iv = new byte[noonceSize];
		byteBuffer.get(iv);

		// Derivando a senha
		SecretKey secretKey = generateSecretKey(key, iv);

		byte[] cipherBytes = new byte[byteBuffer.remaining()];
		byteBuffer.get(cipherBytes);
		
		// Configura modo 
		Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
		GCMParameterSpec parameterSpec = new GCMParameterSpec(128, iv);

		// Inicializa cifrador em modo decifrar
		cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);

		// Decifrando o arquivo
		return cipher.doFinal(cipherBytes);

	}

	/**
	 * Método deriva uma chave a partir de uma senha digitada pelo usuário
	 *  */
	public static SecretKey generateSecretKey(String password, byte[] salt)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		//Configura o derivador
		//Com a senha e o salt 
		//Com Nº de iterações (iterationCount)  do algoritmo de hash e tamanho da chave (keyLength)
		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterationCount, keyLength); 
		
		//Constroi a instância da factory com o modo 'PBKDF2WithHmacSHA1'
		SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		byte[] key = secretKeyFactory.generateSecret(spec).getEncoded();
		return new SecretKeySpec(key, "AES");
	}
}
