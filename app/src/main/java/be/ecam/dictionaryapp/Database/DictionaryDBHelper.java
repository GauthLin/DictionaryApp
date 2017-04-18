package be.ecam.dictionaryapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import be.ecam.dictionaryapp.Entity.Translation;
import be.ecam.dictionaryapp.Entity.Word;

public class DictionaryDBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Dictionary.db";

    private static final String SQL_CREATE_WORD_ENTRIES = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s VARCHAR(255))",
            WordContract.WordEntry.TABLE_NAME,
            WordContract.WordEntry._ID,
            WordContract.WordEntry.COLUMN_NAME_NAME);

    private static final String SQL_DELETE_WORD_ENTRIES = "DROP TABLE IF EXISTS "+ WordContract.WordEntry.TABLE_NAME;

    private static final String SQL_CREATE_TRANS_ENTRIES = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s VARCHAR(255), %s VARCHAR(2), %s INTEGER, FOREIGN KEY(%s) REFERENCES word(%s))",
            TranslationContract.TranslationEntry.TABLE_NAME,
            TranslationContract.TranslationEntry._ID,
            TranslationContract.TranslationEntry.COLUMN_NAME_TRANSLATION,
            TranslationContract.TranslationEntry.COLUMN_NAME_LANGUAGE,
            TranslationContract.TranslationEntry.COLUMN_NAME_WORD_ID,
            TranslationContract.TranslationEntry.COLUMN_NAME_WORD_ID,
            WordContract.WordEntry._ID);

    private static final String SQL_DELETE_TRANS_ENTRIES = "DROP TABLE IF EXISTS "+ TranslationContract.TranslationEntry.TABLE_NAME;


    public DictionaryDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_WORD_ENTRIES);
        db.execSQL(SQL_CREATE_TRANS_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_WORD_ENTRIES);
        db.execSQL(SQL_DELETE_TRANS_ENTRIES);
        onCreate(db);
    }

    public Word save(Word word) {
        // Si le mot à traduire n'existe pas encore
        if (word.getId() == 0) {
            // Gets the data repository in write mode
            SQLiteDatabase db = this.getWritableDatabase();

            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(WordContract.WordEntry.COLUMN_NAME_NAME, word.getName());

            // Insert the new row, returning the primary key value of the new row
            long newRowId = db.insert(WordContract.WordEntry.TABLE_NAME, null, values);

            return new Word((int)newRowId, word.getName());
        }
        // Si le mot à traduire existe déjà
        else {
            SQLiteDatabase db = this.getReadableDatabase();

            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(WordContract.WordEntry.COLUMN_NAME_NAME, word.getName());

            // Which row to update, based on the title
            String selection = WordContract.WordEntry._ID + " = ?";
            String[] selectionArgs = { Integer.toString(word.getId()) };

            int count = db.update(
                    WordContract.WordEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);

            return word;
        }
    }

    public void delete(Word word) {
        for (Translation translation :
                word.getTranslations()) {
            this.delete(translation);
        }

        // Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();
        // Define 'where' part of query.
        String selection = WordContract.WordEntry._ID + " = ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { Integer.toString(word.getId()) };
        // Issue SQL statement.
        db.delete(WordContract.WordEntry.TABLE_NAME, selection, selectionArgs);
    }

    public List<Word> getWords() {
        List<Word> words = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                WordContract.WordEntry._ID,
                WordContract.WordEntry.COLUMN_NAME_NAME
        };

        // TODO inner join with translate
        Cursor cursor = db.query(
                WordContract.WordEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        cursor.close();

        return words;
    }

    public Translation save(Translation translation, int word_id) {
        if (translation.getId() == 0)
        {
            // Gets the data repository in write mode
            SQLiteDatabase db = this.getWritableDatabase();

            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(TranslationContract.TranslationEntry.COLUMN_NAME_TRANSLATION, translation.getTranslation());
            values.put(TranslationContract.TranslationEntry.COLUMN_NAME_LANGUAGE, translation.getLanguage());
            values.put(TranslationContract.TranslationEntry.COLUMN_NAME_WORD_ID, word_id);

            // Insert the new row, returning the primary key value of the new row
            long newRowId = db.insert(TranslationContract.TranslationEntry.TABLE_NAME, null, values);

            return new Translation((int)newRowId, translation.getTranslation(), translation.getLanguage());
        }
        else
        {
            SQLiteDatabase db = this.getReadableDatabase();

            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(TranslationContract.TranslationEntry.COLUMN_NAME_LANGUAGE, translation.getLanguage());
            values.put(TranslationContract.TranslationEntry.COLUMN_NAME_TRANSLATION, translation.getTranslation());
            values.put(TranslationContract.TranslationEntry.COLUMN_NAME_WORD_ID, word_id);

            // Which row to update, based on the title
            String selection = TranslationContract.TranslationEntry._ID + " = ?";
            String[] selectionArgs = { Integer.toString(translation.getId()) };

            int count = db.update(
                    TranslationContract.TranslationEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);

            return translation;
        }
    }

    public void delete(Translation translation) {
        // Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();
        // Define 'where' part of query.
        String selection = TranslationContract.TranslationEntry._ID + " = ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { Integer.toString(translation.getId()) };
        // Issue SQL statement.
        db.delete(TranslationContract.TranslationEntry.TABLE_NAME, selection, selectionArgs);
    }
}
