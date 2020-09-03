package com.jetlyn.springbatchtest.dataloaders;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class HttpClient {
    HttpURLConnection connection = null;

    public String getJson(String urlString) throws Exception {
        URL url = new URL(urlString);
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        InputStream is = connection.getInputStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            response.append(line);
            response.append('\r');
        }
        rd.close();
        connection.disconnect();
        return response.toString();
    }
}
