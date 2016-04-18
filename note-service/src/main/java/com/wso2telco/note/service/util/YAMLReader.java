package com.wso2telco.note.service.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.yaml.snakeyaml.Yaml;

public class YAMLReader {

	private static final Log log = LogFactory.getLog(YAMLReader.class);
	private final static String FILE_NAME = "./netty-transports.yml";
	private static Map yamlMap = null;

	public static Map getYamlMap() {
		
		if(yamlMap == null){
			
			readYAML();
		}
		
		return yamlMap;
	}

	private static void readYAML() {

		Yaml yaml = new Yaml();

		try {

			yamlMap = (Map) yaml.load(new FileInputStream(new File(FILE_NAME)));
		} catch (FileNotFoundException e) {

			log.error("Error in YAMLReader getYamlMap : " + e.getMessage());
		} catch (Exception e) {

			log.error("Error in YAMLReader getYamlMap : " + e.getMessage());
		}
	}
}
