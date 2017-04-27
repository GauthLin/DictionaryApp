package be.ecam.dictionaryapp;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import be.ecam.dictionaryapp.Database.DictionaryDBHelper;
import be.ecam.dictionaryapp.Database.NewWordActivity;
import be.ecam.dictionaryapp.Entity.Word;

public class MainActivity extends AppCompatActivity
        implements  SharedPreferences.OnSharedPreferenceChangeListener,
                    ItemAdapter.ItemAdapterOnClickHandler{

    private DictionaryDBHelper DB;
    private RecyclerView translationsView;
    private ItemAdapter itemAdapter;
    public static List<Word> vocabulary = new ArrayList<>();

    private TextView mDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //DB
        DB = new DictionaryDBHelper(this);
        vocabulary = DB.getWords();

        //Recyclerview
        itemAdapter = new ItemAdapter(this);
        translationsView = (RecyclerView) findViewById(R.id.translationsRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        translationsView.setLayoutManager(layoutManager);
        translationsView.setHasFixedSize(true);
        translationsView.setAdapter(itemAdapter);

        //Preferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }
    public static List<Word> getVocabulary(){
        return vocabulary;
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

        /**
         * création du bouton pour accéder à la page add new word
         */
        Button btn = (Button)findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NewWordActivity.class));
            }
        });
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

        return super.onOptionsItemSelected(item);
    }

    public void onClick(int index) {
        Context context = this;
        Class destinationClass = TranslationActivity.class;
        Intent translationIntent = new Intent(context, destinationClass);
        translationIntent.putExtra(Intent.EXTRA_INDEX, index);
        // on prend l'index, on le met dans l'intent.
        startActivity(translationIntent);
    }





}
