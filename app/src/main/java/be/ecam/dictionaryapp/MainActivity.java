package be.ecam.dictionaryapp;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new AsyncNetworkingTask().execute();
    }

    public class AsyncNetworkingTask extends AsyncTask < String , Void , String > {
        @Override
        protected String doInBackground(String... params) {
            String json = null;

            try {
                json = Networking.getApiTranslation();
            }
            catch (IOException error){
                error.printStackTrace();
            }

            return json;
        }

        protected void onPostExecute(String queryResults) {

            if (queryResults != null) {

            } else {

            }
        }
    }
}
