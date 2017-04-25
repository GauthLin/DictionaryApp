package be.ecam.dictionaryapp.Database;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbManager = new DictionaryDBHelper(this);
        dbManager.getWords();
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
                new AsyncNetworkingTask().execute();
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str = inputTxt.getText().toString();
                Word word = dbManager.getWord(str);
                if (word == null){
                    word = new Word(str);
                }

                Translation translation = new Translation(textToShow, "en");
                if (!word.getTranslations().contains(translation)) {
                    word.addTranslation(translation);
                }

                dbManager.save(word);
                dbManager.getWords();
            }
        });
    }

    public class AsyncNetworkingTask extends AsyncTask< JSONObject , Void , JSONObject > {

        @Override
        protected JSONObject doInBackground(JSONObject... params) {

            myTranslation = new JSONObject();
            String result;

            try {
                myTranslation = translation.get("en", "fr", str);
            }
            catch (IOException error){
                error.printStackTrace();
            }
            catch (JSONException error){
                error.printStackTrace();
            }

            //result = myTranslation.toString();

            String tag = "tag";
            //Log.v(tag, result);

            return myTranslation;
        }

        protected void onPostExecute(JSONObject myTranslation) {

            Context context = NewWordActivity.this;

            if (myTranslation != null && myTranslation.equals("{}")) {
                textToShow = "Network Error" ;

            } else {
                try {
                    textToShow = myTranslation.getString("translationText");
                }
                catch (JSONException error){
                    error.printStackTrace();
                    textToShow = "Il y a eu une erreur";
                }

            }
            showTranslation.setText(textToShow);
            /*Toast.makeText (context, textToShow,
                    Toast.LENGTH_SHORT ).show() ;*/
        }
    }
}
