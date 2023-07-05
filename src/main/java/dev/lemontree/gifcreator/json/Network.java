package dev.lemontree.gifcreator.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Network {
    public static String getJson(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String output;
        StringBuilder response = new StringBuilder();

        while ((output = in.readLine()) != null) {
            response.append(output);
        }

        in.close();

        return response.toString();
    }

    public static InputStream getFile(String url) throws IOException {
        URL fileUrl = new URL(url);

        HttpURLConnection connection = (HttpURLConnection) fileUrl.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("accept", "media/gif");

        int status = connection.getResponseCode();
        if (status == HttpURLConnection.HTTP_OK) {
            return connection.getInputStream();
        } else {
            connection.disconnect();
            return null;
        }
    }
}
