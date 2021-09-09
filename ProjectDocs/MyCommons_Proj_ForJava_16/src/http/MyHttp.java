/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 *
 * @author KOCMOC
 */
public class MyHttp {

    public static String http_request(String url_) throws MalformedURLException, IOException {
        //
        URL url = new URL(url_);
        //
        URLConnection conn = url.openConnection();
        ((HttpURLConnection) conn).setRequestMethod("GET");
        //
        InputStream ins = conn.getInputStream();
        InputStreamReader isr = new InputStreamReader(ins);
        BufferedReader in = new BufferedReader(isr);
        String inputLine;
        String result = "";
        while ((inputLine = in.readLine()) != null) {
            result += inputLine;
        }
        //
        String[] arr = result.split("###");
        //
        if (arr.length == 0) {
            System.out.println("HTTP REQ FAILED");
            return "failed";
        }
        //
        String temp = arr[1];
        String value = temp.split(":")[1];
        System.out.println("HTTP REQ VAL: " + value);
        return value;
    }

    /**
     * 
     * @param urlString
     * @return - if response == 200 -> connection established
     * @throws MalformedURLException
     * @throws IOException 
     */
    public static int http_connect(String urlString) throws MalformedURLException, IOException {
        URL u = new URL(urlString);
        HttpURLConnection huc = (HttpURLConnection) u.openConnection();
        huc.setRequestMethod("GET");
        huc.connect(); // this is a must, only after this the connection is established
        return huc.getResponseCode();
    }

}
