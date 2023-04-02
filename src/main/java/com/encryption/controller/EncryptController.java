package com.encryption.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.encryption.config.AESEncryptionDecryption;
import com.jayway.jsonpath.JsonPath;


@RestController
public class EncryptController {
	
	String secret = "BandarAlhamwan";

	@SuppressWarnings("unchecked")
	@PostMapping(value =  "encrypt", produces=MediaType.APPLICATION_JSON_VALUE)
	public Object encrypt(@RequestBody Object object) {
		
		Map<String, String> request = (Map<String, String>) object;
		if (!request.containsKey("encryptedFields")) {
			Map<String, String> Error = new HashMap<String, String>();
			Error.put("Error", "Please include encryptedFields in request");
			return Error;
		}
		
		List<String> encriptedFieled = new ArrayList<String>();
		Object encryptedFields = request.get("encryptedFields");
		encriptedFieled = (List<String>) encryptedFields;
		request.remove("encryptedFields");
		object = request;

		String json = AESEncryptionDecryption.convertObjectToJson(object);
		
		for(String path : encriptedFieled) {
			
			String value = JsonPath.parse(json).read(path);
			json = JsonPath.parse(json).set(path, AESEncryptionDecryption.encrypt(value, secret)).jsonString();
		}

		return json;
	}
	
	
	@SuppressWarnings("unchecked")
	@PostMapping(value =  "decrypt", produces=MediaType.APPLICATION_JSON_VALUE)
	public Object decrypt(@RequestBody Object object) {
		
		Map<String, String> request = (Map<String, String>) object;
		if (!request.containsKey("decryptedFields")) {
			Map<String, String> Error = new HashMap<String, String>();
			Error.put("Error", "Please include encryptedFields in request");
			return Error;
		}
		
		List<String> encriptedFieled = new ArrayList<String>();
		Object encryptedFields = request.get("decryptedFields");
		encriptedFieled = (List<String>) encryptedFields;
		request.remove("decryptedFields");
		object = request;

		String json = AESEncryptionDecryption.convertObjectToJson(object);
		
		for(String path : encriptedFieled) {
			
			String value = JsonPath.parse(json).read(path);
			
			json = JsonPath.parse(json).set(path, AESEncryptionDecryption.decrypt(value, secret)).jsonString();
		}

		return json;
	}
}
