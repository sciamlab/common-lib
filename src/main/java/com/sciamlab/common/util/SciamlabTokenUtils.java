package com.sciamlab.common.util;

import java.security.Key;

import org.jose4j.jwe.ContentEncryptionAlgorithmIdentifiers;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.jwe.KeyManagementAlgorithmIdentifiers;
import org.jose4j.lang.JoseException;

public class SciamlabTokenUtils {

	public static String createJsonWebToken(String payload, Key key) throws JoseException{
		JsonWebEncryption jwe = new JsonWebEncryption();
		jwe.setPayload(payload);
		jwe.setAlgorithmHeaderValue(KeyManagementAlgorithmIdentifiers.A128KW);
		jwe.setEncryptionMethodHeaderParameter(ContentEncryptionAlgorithmIdentifiers.AES_128_CBC_HMAC_SHA_256);
		jwe.setKey(key);
		return jwe.getCompactSerialization();
	}
	
	public static String decodeJsonWebToken(String serializedJwe, Key key) throws JoseException{
		JsonWebEncryption jwe = new JsonWebEncryption();
		jwe.setKey(key);
		jwe.setCompactSerialization(serializedJwe);
		return jwe.getPayload();
	}
}
