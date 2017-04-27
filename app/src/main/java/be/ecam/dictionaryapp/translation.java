package be.ecam.dictionaryapp;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import be.ecam.dictionaryapp.Database.DictionaryDBHelper;

//public pour etre accédée
//static pour ne pas devoir faire new.

/**
 * Created by damien on 28.03.17.
 */

public final class translation{

    private String origin = null;
    private String trans = null;
    private static ArrayList<translation> translationsCollection = new ArrayList<>(); // on crée une liste vide

    public static JSONObject get(String from, String to, String text) throws IOException, JSONException{
        JSONObject result;
        JSONObject translationRequest = new JSONObject();
        try {
            translationRequest.put("text",text);
            translationRequest.put("from",from);
            translationRequest.put("to",to);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        result = Networking.getApiTranslation(translationRequest);

        return result;
    }

}

