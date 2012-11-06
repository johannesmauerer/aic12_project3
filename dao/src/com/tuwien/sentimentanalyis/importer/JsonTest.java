package com.tuwien.sentimentanalyis.importer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
        JSONParser parser = new JSONParser();
         
        try {
 
            FileReader freader = new FileReader("example_tweets.txt");
            BufferedReader breader = new BufferedReader(freader);
            
            String line = breader.readLine();
            while (line != null){
            if(line.contains("{")){
                Object obj = parser.parse(line);
                 
                JSONObject jsonObject = (JSONObject) obj;

                KeyFinder finder = new KeyFinder();
                finder.setMatchKey("created_at");
                try{
                  while(!finder.isEnd()){
                    parser.parse(line, finder, true);
                    if(finder.isFound()){
                      finder.setFound(false);
                      System.out.println("found created_at:");
                      System.out.println(finder.getValue());
                    }
                 }           
                }
                catch(ParseException pe){
                  pe.printStackTrace();
                }

 
                String name = (String) jsonObject.get("text");
                System.out.println(name + "\n");
                
            }
                    
            line = breader.readLine();
            }
                
 
        } catch (FileNotFoundException e) {
                e.printStackTrace();
        } catch (IOException e) {
                e.printStackTrace();
        } catch (ParseException e) {
                e.printStackTrace();
        }

    }

}

