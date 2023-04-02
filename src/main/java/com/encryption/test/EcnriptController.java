package com.encryption.test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.encryption.config.AESEncryptionDecryption;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;

@RestController
public class EcnriptController {

	
	@PostMapping("en")
	public Object encript(@RequestBody Object object) throws StreamReadException, DatabindException, IOException {
		String convertObjectToJson = AESEncryptionDecryption.convertObjectToJson(object);
		
	/*
		String convertObjectToJson = AESEncryptionDecryption.convertObjectToJson(object);
		JSONObject obj = new JSONObject(convertObjectToJson);
		String pageName = obj.getJSONObject("pageInfo").getString("pageName");

		JSONArray arr = obj.getJSONArray("posts"); // notice that `"posts": [...]`
		for (int i = 0; i < arr.length(); i++){
		    String post_id = arr.getJSONObject(i).getString("post_id");
		    System.out.println(post_id);
		}
		
		System.out.println(pageName);
		System.out.println(arr.length());
		*/
		
		JsonParser springParser = JsonParserFactory.getJsonParser();
		List<Object> list = springParser.parseList(convertObjectToJson);
		for(Object o : list) {
		    if(o instanceof Map) {
		        Map<String,Object> map = (Map<String,Object>) o;
		        System.out.println(map);
		    }
		}
		return object;
	}
}
