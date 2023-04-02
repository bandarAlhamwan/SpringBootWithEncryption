package com.encryption.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.encryption.config.AESEncryptionDecryption;

@RestController
public class AESEncryptionDecryptionController {
	String secret = "bandar";

	@SuppressWarnings("unchecked")
	@GetMapping(value = "encrypt1")
	public Object getObject2(@RequestBody Object encriptRequest) throws Exception {
		Map<String, String> request = (Map<String, String>) encriptRequest;

		if (!request.containsKey("encryptedFields")) {
			Map<String, String> Error = new HashMap<String, String>();
			Error.put("Error", "Please include encryptedFields in request");
			return Error;
		}

		// add Encription fieled to list
		List<String> encriptedFieled = new ArrayList<String>();
		Object encryptedFields = request.get("encryptedFields");
		encriptedFieled = (List<String>) encryptedFields;
		// String[] strArray = null;
		
		/*
		 * for (int i = 0; i < strArray.length; i++) { String string2 = strArray[i];
		 * string2 = string2.replace("[", ""); string2 = string2.replace("]", "");
		 * string2 = string2.replaceAll("\\s", ""); encriptedFieled.add(string2); }
		 */

		for (String fieldName : encriptedFieled) {
			int count = fieldName.length() - fieldName.replaceAll("\\_", "").length();

			if (count == 0) {
				Object object1 = request.get(fieldName);
				request.put(fieldName, AESEncryptionDecryption.encrypt(object1.toString(), secret));
			}

			if (count > 0) {
				String[] split = fieldName.split("_");

				Map<String, String> request2 = new HashMap<String, String>();
				
				Object finename;

				for (int i = 0; i < split.length -1; i++) {

					if (i == 0) {
						finename = request.get(split[i]);
						request2 = (Map<String, String>) finename;
						System.out.println(request2);
					}
					if (i > 0) {
						System.out.println(split[i].contains("[]"));
						finename = request2.get(split[i]);
						if (finename instanceof Collection<?>){
							
							List<Object> list1 = (List<Object>) finename;
							
							for(Object n: list1) {
								System.out.println(n);
							}
							
						}else {
							request2 = (Map<String, String>) finename;
						}
	
					}
					
				}
				
				
				Object string = request2.get(split[split.length - 1]);
				System.out.println(string);
				request2.put(split[split.length - 1],
						AESEncryptionDecryption.encrypt(string.toString(), secret));

			}
		}

		request.remove("encryptedFields");

		return request;

	}

	@SuppressWarnings("unchecked")
	@GetMapping(value = "decrypt1")
	public Object decript(@RequestBody Object encriptRequest) throws Exception {
		Map<String, String> request = (Map<String, String>) encriptRequest;

		if (!request.containsKey("decryptedFields")) {
			Map<String, String> Error = new HashMap<String, String>();
			Error.put("Error", "Please include decryptedFields in request");
			return Error;
		}

		// add Encription fieled to list
		List<String> encriptedFieled = new ArrayList<String>();
		Object string = request.get("decryptedFields");
		String[] strArray = null;
		strArray = string.toString().split(",");

		for (int i = 0; i < strArray.length; i++) {
			String string2 = strArray[i];
			string2 = string2.replace("[", "");
			string2 = string2.replace("]", "");
			string2 = string2.replaceAll("\\s", "");
			encriptedFieled.add(string2);
		}

		for (String fieldName : encriptedFieled) {
			int count = fieldName.length() - fieldName.replaceAll("\\_", "").length();

			if (count == 0) {
				request.put(fieldName, AESEncryptionDecryption.decrypt(request.get(fieldName), secret));
			}

			if (count >= 1) {
				String[] split = fieldName.split("_");

				Map<String, String> request2 = new HashMap<String, String>();

				for (int i = 0; i < split.length - 1; i++) {

					if (i == 0) {
						Object finename = request.get(split[i]);
						request2 = (Map<String, String>) finename;
					} else {
						Object finename = request2.get(split[i]);
						request2 = (Map<String, String>) finename;
					}

				}

				request2.put(split[split.length - 1],
						AESEncryptionDecryption.decrypt(request2.get(split[split.length - 1]), secret));
			}
		}

		request.remove("decryptedFields");

		return request;

	}

	@GetMapping(value = "object1")
	public Object getObject(@RequestBody Object encriptRequest) throws Exception {

		List<Map<String, String>> objectList = new ArrayList<Map<String, String>>();

		@SuppressWarnings("unchecked")
		Map<String, String> request = (Map<String, String>) encriptRequest;
		objectList.add(request);

		Set<String> keySet = request.keySet();
		for (String n : keySet) {
			Object ob1 = request.get(n);
			String ob1String = ob1.toString();
			boolean endsWith = ob1String.endsWith("}");
			if (endsWith) {
				Map<String, String> request2 = (Map<String, String>) ob1;
				objectList.add(request2);

				Set<String> keySet2 = request2.keySet();
				for (String n2 : keySet2) {
					Object ob2 = request2.get(n2);
					String ob2String = ob2.toString();
					boolean endsWith2 = ob2String.endsWith("}");
					if (endsWith2) {
						Map<String, String> request3 = (Map<String, String>) ob2;
						objectList.add(request3);
					}
				}

			}
		}

		List<String> encriptedFieled = new ArrayList<String>();
		Object string = request.get("encriptedFieled");
		String[] strArray = null;
		strArray = string.toString().split(",");

		for (int i = 0; i < strArray.length; i++) {
			String string2 = strArray[i];
			string2 = string2.replace("[", "");
			string2 = string2.replace("]", "");
			string2 = string2.replaceAll("\\s", "");
			encriptedFieled.add(string2);
		}

		for (Map<String, String> maps : objectList) {
			for (String n : encriptedFieled) {
				System.out.println(n);
				if (maps.containsKey(n)) {
					maps.put(n, AESEncryptionDecryption.encrypt(maps.get(n), secret));

				}
			}
		}

		return request;

	}
}
