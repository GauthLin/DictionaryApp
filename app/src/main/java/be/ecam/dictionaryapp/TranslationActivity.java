package be.ecam.dictionaryapp;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import be.ecam.dictionaryapp.Database.DictionaryDBHelper;
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

        Intent intent = getIntent();

        int position = intent.getIntExtra(Intent.EXTRA_INDEX, 0);
        myTranslationObject = vocabList.get(position);

        String trans = "";

        for (Translation translation :
                myTranslationObject.getTranslations()) {
            trans += String.format("%s - %s\n", translation.getLanguage(), translation.getTranslation());
        }
        EnLangTextView.setText(trans);

        // on passe le Int au find de Weather, qui va nous renvoyer le bon weather.
        WordTextView.setText("Traduction du mot " + myTranslationObject.getName());

        // Deletion of the word if button clicked
        final Button deleteWordBtn = (Button) findViewById(R.id.delete_word);
        final boolean[] already_pressed = {false};
        deleteWordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ask the user to confirm the suppression within delay
                if (!already_pressed[0]) {
                    already_pressed[0] = true;
                    deleteWordBtn.setText(R.string.confirm_suppression);

                    // The user must confirm within 2 secondes
                    // If he didn't confirm, the button will be refreshed
                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    already_pressed[0] = false;
                                    deleteWordBtn.setText(R.string.delete_word);
                                }
                            },
                            2000);
                } else { // confirmation of the suppression
                    DictionaryDBHelper dbHelper = new DictionaryDBHelper(TranslationActivity.this);
                    dbHelper.delete(myTranslationObject);
                    deleteWordBtn.setText(R.string.word_deleted);
                    deleteWordBtn.setEnabled(false);
                }
            }
        });
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
