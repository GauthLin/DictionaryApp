package be.ecam.dictionaryapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import be.ecam.dictionaryapp.Database.DictionaryDBHelper;
import be.ecam.dictionaryapp.Entity.Word;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DictionaryDBHelper dbHelper = new DictionaryDBHelper(this);
    }
}
