package be.ecam.dictionaryapp;

import android.content.Context;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import be.ecam.dictionaryapp.Database.NewWordActivity;

public class MainActivity extends AppCompatActivity  implements SharedPreferences.OnSharedPreferenceChangeListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new AsyncNetworkingTask().execute();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key){
        if (key.equals(true)){
            ChangeLanguage();
        }
    }

    private void ChangeLanguage() {
    }
    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        int itemThatWasClickedId = item.getItemId();
        if(itemThatWasClickedId == R.id.goToPreference){
            Context context = MainActivity.this;
            startActivity(new Intent(context, SettingsActivity.class));
            return true;
        }


        Button btn = (Button)findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NewWordActivity.class));
            }
        });
        return super.onOptionsItemSelected(item);
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

        protected void onPostExecute(String result) {
            String textToShow;
            Context context = MainActivity.this;

            if (result != null && result.equals("{}")) {
                textToShow = "Network Error" ;

            } else {
                textToShow = result;
            }
            Toast . makeText ( context , textToShow ,
                    Toast. LENGTH_SHORT ) . show ( ) ;
        }
    }


}
