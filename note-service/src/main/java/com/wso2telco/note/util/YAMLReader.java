package com.wso2telco.note.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.yaml.snakeyaml.Yaml;

import com.wso2telco.note.exception.NoteException;

public class YAMLReader {

	private static final Log log = LogFactory.getLog(YAMLReader.class);
	private final static String FILE_NAME = "./netty-transports.yml";
	private static Map yamlMap = null;

	public static Map getYamlMap() throws NoteException{

		if (yamlMap == null) {

			try {
				
				readYAML();
			} catch (NoteException e) {
				
				throw e;
			}
		}

		return yamlMap;
	}

	private static void readYAML() throws NoteException {

		Yaml yaml = new Yaml();

		try {

			yamlMap = (Map) yaml.load(new FileInputStream(new File(FILE_NAME)));
		} catch (FileNotFoundException e) {

			log.error("Error in YAMLReader readYAML -> " + FILE_NAME + " not found : ", e);
			throw new NoteException(ErrorCodes.ERROR_NETTY_TRANSPORTS_YAML_NOT_FOUND, e);
		} catch (Exception e) {

			log.error("Error in YAMLReader readYAML : ", e);
			throw new NoteException(ErrorCodes.ERROR_NETTY_TRANSPORTS_YAML_READ, e);
		}
	}
}
