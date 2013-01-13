package aic12.project3.common.config;

import java.util.Properties;

public class GeneralConfig {

	protected Properties configFile;

	public GeneralConfig(String propertyFile) {
		configFile = new java.util.Properties();
		try {			
			configFile.load(this.getClass().getClassLoader().
					getResourceAsStream(propertyFile));			
		}catch(Exception eta){
			eta.printStackTrace();
		}
	}

	public String getProperty(String key) {
		String value = configFile.getProperty(key);
		if(value == null) {
			throw new IllegalArgumentException("key in config file not found!");
		}
		return value;		
	}

}