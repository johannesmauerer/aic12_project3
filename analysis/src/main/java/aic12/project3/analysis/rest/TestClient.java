package aic12.project3.analysis.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import aic12.project3.common.beans.Request;

public class TestClient
{
    public static void main(String[] args) throws IOException
    {
        URL url = new URL("http://localhost:8080/cloudservice-1.0-SNAPSHOT/sentiment/analyze");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setDoOutput(true);
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        
        Request request = new Request();
        request.setId(1);
        request.setCompanyName("microsoft");
        request.setMinNoOfTweets(25);
        request.setFrom(new Date());
        request.setTo(new Date());
        
        OutputStream os = con.getOutputStream();
        os.write(request.toJSON().getBytes());
        os.flush();
        
        if (con.getResponseCode() != HttpURLConnection.HTTP_OK)
        {
            System.out.println("error");
        }
        else
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String output;
            while ((output = br.readLine()) != null)
            {
                System.out.println(output);
            }
        }
        
        con.disconnect();
    }
}
