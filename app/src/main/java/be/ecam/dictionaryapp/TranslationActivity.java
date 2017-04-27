package be.ecam.dictionaryapp;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.List;

import be.ecam.dictionaryapp.Entity.Translation;
import be.ecam.dictionaryapp.Entity.Word;

public class TranslationActivity extends AppCompatActivity {

    private List<Word> vocabList = MainActivity.getVocabulary();
    private Word myTranslationObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setContentView(R.layout.activity_translation); // l'inflate est déjà compris dans le setContentView

        TextView WordTextView = (TextView) findViewById(R.id.Word);
        TextView EnLangTextView = (TextView) findViewById(R.id.TransEng);
        TextView EnTradTextView = (TextView) findViewById(R.id.TransEs);

        Intent intent = getIntent();

        int position = intent.getIntExtra(Intent.EXTRA_INDEX, 0);
        myTranslationObject = vocabList.get(position);

        String trad1 = "";
        String trad2 = "";


        for (Translation translation :
                myTranslationObject.getTranslations()) {

            String lang = translation.getLanguage();
            String trad = translation.getTranslation();
            EnLangTextView.setText(lang);
            EnTradTextView.setText(trad);
        }

        // on passe le Int au find de Weather, qui va nous renvoyer le bon weather.

        WordTextView.setText(myTranslationObject.getName());


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }
}
// pour le menu activity, creer un intent et lancer l'activity via l'intent.
