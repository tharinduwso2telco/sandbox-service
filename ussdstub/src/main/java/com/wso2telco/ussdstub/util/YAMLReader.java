/*******************************************************************************
 * Copyright (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) 
 * 
 * All Rights Reserved. WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.wso2telco.ussdstub.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.yaml.snakeyaml.Yaml;

import com.wso2telco.ussdstub.exception.ErrorCodes;
import com.wso2telco.ussdstub.exception.USSDException;



// TODO: Auto-generated Javadoc
/**
 * The Class YAMLReader.
 */
public class YAMLReader {

	/** The Constant log. */
	private static final Log log = LogFactory.getLog(YAMLReader.class);
	
	/** The Constant FILE_NAME. */
	private final static String FILE_NAME = "./netty-transports.yml";
	
	/** The yaml map. */
	private static Map<String,Object> yamlMap = null;

	/*public static String getApplicationProperty(String key) throws USSDException {
        return getYamlMap().get(key);
    }*/
	
	/**
	 * Gets the yaml map.
	 *
	 * @return the yaml map
	 * @throws USSDException the USSD exception
	 */
	public static Map<String,Object> getYamlMap() throws USSDException{

		if (yamlMap == null) {

			try {
				
				readYAML();
			} catch (USSDException e) {
				
				throw e;
			}
		}

		return yamlMap;
	}

	/**
	 * Read yaml.
	 *
	 * @throws USSDException the USSD exception
	 */
	@SuppressWarnings("unchecked")
	private static void readYAML() throws USSDException {

		Yaml yaml = new Yaml();

		try {
			yamlMap = (Map<String,Object>) yaml.load(new FileInputStream(new File(FILE_NAME)));
		} catch (FileNotFoundException e) {

			log.error("Error in YAMLReader readYAML -> " + FILE_NAME + " not found : ", e);
			throw new USSDException(ErrorCodes.ERROR_NETTY_TRANSPORTS_YAML_NOT_FOUND, e);
		} catch (Exception e) {

			log.error("Error in YAMLReader readYAML : ", e);
			throw new USSDException(ErrorCodes.ERROR_NETTY_TRANSPORTS_YAML_READ, e);
		}
	}
}
