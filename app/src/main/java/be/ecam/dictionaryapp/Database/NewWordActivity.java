package be.ecam.dictionaryapp.Database;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import be.ecam.dictionaryapp.MainActivity;
import be.ecam.dictionaryapp.R;
import be.ecam.dictionaryapp.translation;

public class NewWordActivity extends AppCompatActivity {

    private EditText inputTxt;
    private TextView showTranslation;
    public String str;
    private JSONObject myTranslation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_word);


        inputTxt = (EditText) findViewById(R.id.editText);
        showTranslation = (TextView) findViewById((R.id.textView));

       Button btn = (Button)findViewById(R.id.button2);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str = inputTxt.getText().toString();
                new AsyncNetworkingTask().execute();
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
            String textToShow;
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
