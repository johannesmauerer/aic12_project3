package aic12.project3.common.util;

import java.util.*;
 
public class ConfigReader {
   Properties configFile;
   
   public ConfigReader(String file)
   {
	configFile = new java.util.Properties();
	try {			
	  configFile.load(this.getClass().getClassLoader().
	  getResourceAsStream(file));			
	}catch(Exception eta){
	    eta.printStackTrace();
	}
   }
 
   public String getProperty(String key)
   {
	return this.configFile.getProperty(key);		
   }
}