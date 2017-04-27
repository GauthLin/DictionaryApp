package be.ecam.dictionaryapp;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import be.ecam.dictionaryapp.Database.DictionaryDBHelper;
import be.ecam.dictionaryapp.Entity.Word;

public class QuizActivity extends AppCompatActivity {
    private boolean is_quiz_generated = false;
    private String lang;

    // Contains the list of words to translate and the input of user
    private List<TextView> textViews = new ArrayList<>();
    private List<EditText> editTexts = new ArrayList<>();
    List<Word> words;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set the language for translation
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        lang = prefs.getString("LangChoice", "en");

        // Gets all the words
        DictionaryDBHelper dbHelper = new DictionaryDBHelper(this);
        words = dbHelper.getWords(lang);

        final Button quizBtn = (Button) findViewById(R.id.validate_quiz_btn);
        final Button newQuizBtn = (Button) findViewById(R.id.generate_new_quiz);
        quizBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If the quiz has been generated
                if (is_quiz_generated) {
                    // Validates the quiz
                    int errors = validateQuiz();
                    // if there are errors --> colored in red
                    if (errors > 0) {
                        Toast.makeText(QuizActivity.this, R.string.errors_quiz, Toast.LENGTH_LONG).show();
                        newQuizBtn.setVisibility(View.VISIBLE);
                    }
                    // If no error, ask the user if he wants a new quiz
                    else {
                        newQuizBtn.setVisibility(View.VISIBLE);
                        Toast.makeText(QuizActivity.this, R.string.success_quiz, Toast.LENGTH_LONG).show();
                        is_quiz_generated = false;
                        quizBtn.setText(R.string.regenerate_quiz);
                    }
                }
                // If the quiz has not been generated yet
                else {
                    // If there are more than one word in the current language --> generate quiz
                    if (words.size() > 0) {
                        is_quiz_generated = true;
                        quizBtn.setText(R.string.validate_quiz);
                        generateQuiz(words);
                    }
                    // If not, tell the user
                    else {
                        Toast.makeText(QuizActivity.this, R.string.no_words_for_quiz, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        // Button to display responses
        newQuizBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newQuizBtn.setVisibility(View.GONE);
                is_quiz_generated = false;
                quizBtn.setText(R.string.regenerate_quiz);
                showResponse();
            }
        });
    }

    public void generateQuiz(List<Word> words) {
        // Shuffle all the words
        Collections.shuffle(words);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.quiz_words_list);
        // Removes all views + display the new words
        linearLayout.removeAllViews();
        linearLayout.setVisibility(View.VISIBLE);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        // Clear the tables
        editTexts.clear();
        textViews.clear();

        // Displays the word
        for (int i = 0; i < 10; i++) {
            if (i >= words.size())
                break;

            TextView textView = new TextView(this);
            textView.setLayoutParams(layoutParams);
            textView.setText(words.get(i).getName());
            linearLayout.addView(textView);

            EditText editText = new EditText(this);
            editText.setLayoutParams(layoutParams);
            linearLayout.addView(editText);

            editTexts.add(editText);
            textViews.add(textView);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public int validateQuiz() {
        int errors = 0;

        for (int i = 0; i < textViews.size(); i++) {
            String input = editTexts.get(i).getText().toString().toLowerCase().trim();
            String translation = words.get(i).getTranslation(lang).getTranslation().toLowerCase();
            if (!Objects.equals(input, translation)) {
                errors++;
                textViews.get(i).setTextColor(Color.RED);
            } else {
                textViews.get(i).setTextColor(Color.BLACK);
            }
        }
        return errors;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void showResponse() {
        for (int i = 0; i < textViews.size(); i++) {
            String translation = words.get(i).getTranslation(lang).getTranslation().toLowerCase();
            editTexts.get(i).setText(translation);
        }
    }
}
