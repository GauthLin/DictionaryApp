package be.ecam.dictionaryapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new AsyncNetworkingTask().execute();
    }

    public class AsyncNetworkingTask extends AsyncTask< String , Void , String > {

        @Override
        protected String doInBackground(String... params) {

            JSONObject myTranslation = new JSONObject();
            String result;

            try {
                myTranslation = translation.get("en", "fr", "welcome");
            }
            catch (IOException error){
                error.printStackTrace();
            }
            catch (JSONException error){
                error.printStackTrace();
            }

            result = myTranslation.toString();

            String tag = "tag";
            Log.v(tag, result);

            return result;
        }

        protected void onPostExecute(String queryResults) {

            if (queryResults != null) {

            } else {

            }
        }
    }
}
