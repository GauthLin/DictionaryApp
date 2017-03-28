package be.ecam.dictionaryapp;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * Created by damien on 21.03.17.
 */

public final class Networking {
    public static JSONObject getApiTranslation(JSONObject translationRequest) throws IOException, JSONException {

        String translation;
        JSONObject result;
        String line;
        StringBuffer jsonString = new StringBuffer();

        String urlAdress = "http://www.transltr.org/api/translate";
        URL url = new URL(urlAdress);
        HttpURLConnection connection;

        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.addRequestProperty("Accept", "application/json");
        connection.addRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
        writer.write(translationRequest.toString());
        writer.close();

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while((line = br.readLine()) != null){
                jsonString.append(line);
            }
            br.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        connection.disconnect();

        translation = jsonString.toString();
        result = new JSONObject(translation);

        return result;
    }
}
