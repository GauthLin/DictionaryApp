package be.ecam.dictionaryapp.Database;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import be.ecam.dictionaryapp.Entity.Translation;
import be.ecam.dictionaryapp.Entity.Word;
import be.ecam.dictionaryapp.R;
import be.ecam.dictionaryapp.translation;

public class NewWordActivity extends AppCompatActivity {
    private EditText inputTxt;
    private TextView showTranslation;
    public String str;
    private JSONObject myTranslation;
    private DictionaryDBHelper dbManager;
    private String textToShow;
    private boolean textCanBeSaved;

    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbManager = new DictionaryDBHelper(this);
        setContentView(R.layout.activity_new_word);

        /*
         * Instanciation de l'input et des 2 boutons de la page add new word.
         * Si on clique sur le btn2, récupère la traduction depuis serveur.
         * Si on clique sur le btn3, stock la traduction dans la DB.
         */
        inputTxt = (EditText) findViewById(R.id.editText);
        showTranslation = (TextView) findViewById((R.id.textView));

        Button btn2 = (Button)findViewById(R.id.button2);
        Button btn3 = (Button)findViewById(R.id.button3);

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str = inputTxt.getText().toString();
                if (!isConnected()) {
                    Toast.makeText(NewWordActivity.this, R.string.plz_connect_to_internet, Toast.LENGTH_LONG).show();
                } else if (!str.equals("")) {
                    new AsyncNetworkingTask().execute();
                } else {
                    Toast.makeText(NewWordActivity.this, R.string.plz_add_word, Toast.LENGTH_LONG).show();
                }
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str = inputTxt.getText().toString();
                if (textCanBeSaved) {
                    Word word = dbManager.getWord(str);
                    if (word == null){
                        word = new Word(str);
                    }

                    Translation translation = new Translation(textToShow, "en");
                    if (!word.getTranslations().contains(translation)) {
                        word.addTranslation(translation);
                        Toast.makeText(NewWordActivity.this, R.string.word_saved, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(NewWordActivity.this, R.string.word_already_saved, Toast.LENGTH_LONG).show();
                    }

                    dbManager.save(word);
                } else {
                    Toast.makeText(NewWordActivity.this, R.string.word_to_save_cannot_be_empty, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public class AsyncNetworkingTask extends AsyncTask< JSONObject , Void , JSONObject > {
        @Override
        protected JSONObject doInBackground(JSONObject... params) {
            myTranslation = new JSONObject();
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(NewWordActivity.this);
            String lang = prefs.getString("LangChoice", "en");

            try {
                myTranslation = translation.get("fr", lang, str);
            }
            catch (IOException | JSONException error){
                Toast.makeText(NewWordActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }

            return myTranslation;
        }

        protected void onPostExecute(JSONObject myTranslation) {
            Context context = NewWordActivity.this;
            textCanBeSaved = false;

            if (myTranslation != null && myTranslation.equals("{}")) {
                Toast.makeText(context, R.string.plz_connect_to_internet, Toast.LENGTH_LONG).show();
            } else {
                try {
                    textToShow = myTranslation.getString("translationText");
                    textCanBeSaved = true;
                    showTranslation.setText(textToShow);
                }
                catch (JSONException error){
                    Toast.makeText(context, R.string.error, Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
