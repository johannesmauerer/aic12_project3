package aic12.project3.service.util;

import java.io.File;
import java.util.*;

import org.apache.log4j.Logger;
 
public class ManagementConfig 
{
   Properties configFile;
   
   private Logger logger = Logger.getRootLogger();
   private String propertyFile = "config.properties";
   
   public ManagementConfig()
   {
	  
		
	configFile = new java.util.Properties();
	try {			
	  configFile.load(this.getClass().getClassLoader().
	  getResourceAsStream(propertyFile));			
	}catch(Exception eta){
	    eta.printStackTrace();
	}
   }
 
   public String getProperty(String key)
   {
	String value = this.configFile.getProperty(key);		
	return value;
   }
}