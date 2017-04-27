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

    private static final String SQL_DELETE_WORD_ENTRIES = "DROP TABLE IF EXISTS " + WordContract.WordEntry.TABLE_NAME;

    private static final String SQL_CREATE_TRANS_ENTRIES = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s VARCHAR(255), %s VARCHAR(2), %s INTEGER, FOREIGN KEY(%s) REFERENCES word(%s))",
            TranslationContract.TranslationEntry.TABLE_NAME,
            TranslationContract.TranslationEntry._ID,
            TranslationContract.TranslationEntry.COLUMN_NAME_TRANSLATION,
            TranslationContract.TranslationEntry.COLUMN_NAME_LANGUAGE,
            TranslationContract.TranslationEntry.COLUMN_NAME_WORD_ID,
            TranslationContract.TranslationEntry.COLUMN_NAME_WORD_ID,
            WordContract.WordEntry._ID);

    private static final String SQL_DELETE_TRANS_ENTRIES = "DROP TABLE IF EXISTS " + TranslationContract.TranslationEntry.TABLE_NAME;


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

    /**
     * Saves to the database the word
     *
     * @param word the word to save
     * @return the word with its id
     */
    public Word save(Word word) {
        Word new_word;

        // if the word doesn't already exit in db
        if (word.getId() == 0) {
            // Gets the data repository in write mode
            SQLiteDatabase db = this.getWritableDatabase();

            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(WordContract.WordEntry.COLUMN_NAME_NAME, word.getName());

            // Insert the new row, returning the primary key value of the new row
            long newRowId = db.insert(WordContract.WordEntry.TABLE_NAME, null, values);
            new_word = new Word((int) newRowId, word.getName());
        }
        // If the word already exits in db
        else {
            SQLiteDatabase db = this.getReadableDatabase();

            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(WordContract.WordEntry.COLUMN_NAME_NAME, word.getName());

            // Which row to update, based on the title
            String selection = WordContract.WordEntry._ID + " = ?";
            String[] selectionArgs = {Integer.toString(word.getId())};

            db.update(WordContract.WordEntry.TABLE_NAME, values, selection, selectionArgs);

            new_word = new Word(word.getId(), word.getName());
        }

        // Update the translations bound to that word
        for (Translation translation :
                word.getTranslations()) {
            new_word.addTranslation(save(translation, new_word.getId()));
        }

        return new_word;
    }

    /**
     * Deletes a specific word
     *
     * @param word the word to delete
     */
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
        String[] selectionArgs = {Integer.toString(word.getId())};
        // Issue SQL statement.
        db.delete(WordContract.WordEntry.TABLE_NAME, selection, selectionArgs);
    }

    /**
     * Gets the list of the words
     *
     * @return list of words
     */
    public List<Word> getWords() {
        List<Word> words = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        final String query = String.format("SELECT * FROM %s", WordContract.WordEntry.TABLE_NAME);
        Cursor wCursor = db.rawQuery(query, null);

        while (wCursor.moveToNext()) {
            String word_name = wCursor.getString(wCursor.getColumnIndex(WordContract.WordEntry.COLUMN_NAME_NAME));
            int word_id = wCursor.getInt(wCursor.getColumnIndex(WordContract.WordEntry._ID));

            Word word = new Word(word_id, word_name);
            word = getTranslationsOf(word);

            words.add(word);
        }

        wCursor.close();

        return words;
    }

    /**
     * Gets a specific word
     *
     * @param name the name of the word to search
     * @return the word with its translations
     */
    public Word getWord(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = String.format("SELECT * FROM %s w WHERE LOWER(w.%s) = ?",
                WordContract.WordEntry.TABLE_NAME,
                WordContract.WordEntry.COLUMN_NAME_NAME);

        Word word = null;
        Cursor cursor = db.rawQuery(query, new String[] {name.toLowerCase()});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            String word_name = cursor.getString(cursor.getColumnIndex(WordContract.WordEntry.COLUMN_NAME_NAME));
            int word_id = cursor.getInt(cursor.getColumnIndex(WordContract.WordEntry._ID));

            word = new Word(word_id, word_name);
            word = getTranslationsOf(word);
        }

        cursor.close();

        return word;
    }

    /**
     * Gets the all the translations of the word
     *
     * @param word the word to get the translations
     * @return the word with all its translations
     */
    private Word getTranslationsOf(Word word) {
        SQLiteDatabase db = this.getReadableDatabase();
        String trans_query = String.format("SELECT * FROM %s t WHERE t.%s=?", TranslationContract.TranslationEntry.TABLE_NAME, TranslationContract.TranslationEntry.COLUMN_NAME_WORD_ID);
        Cursor tCursor = db.rawQuery(trans_query, new String[]{String.valueOf(word.getId())});
        while (tCursor.moveToNext()) {
            int trans_id = tCursor.getInt(tCursor.getColumnIndex(TranslationContract.TranslationEntry._ID));
            String trans = tCursor.getString(tCursor.getColumnIndex(TranslationContract.TranslationEntry.COLUMN_NAME_TRANSLATION));
            String trans_lang = tCursor.getString(tCursor.getColumnIndex(TranslationContract.TranslationEntry.COLUMN_NAME_LANGUAGE));
            Translation translation = new Translation(trans_id, trans, trans_lang);
            word.addTranslation(translation);
        }
        tCursor.close();

        return word;
    }

    /**
     * Saves to the database the translation
     *
     * @param translation the translation to save
     * @param word_id     the word related to the translation
     * @return the translation with its id
     */
    public Translation save(Translation translation, int word_id) {
        if (translation.getId() == 0) {
            // Gets the data repository in write mode
            SQLiteDatabase db = this.getWritableDatabase();

            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(TranslationContract.TranslationEntry.COLUMN_NAME_TRANSLATION, translation.getTranslation());
            values.put(TranslationContract.TranslationEntry.COLUMN_NAME_LANGUAGE, translation.getLanguage());
            values.put(TranslationContract.TranslationEntry.COLUMN_NAME_WORD_ID, word_id);

            // Insert the new row, returning the primary key value of the new row
            long newRowId = db.insert(TranslationContract.TranslationEntry.TABLE_NAME, null, values);

            return new Translation((int) newRowId, translation.getTranslation(), translation.getLanguage());
        } else {
            SQLiteDatabase db = this.getReadableDatabase();

            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(TranslationContract.TranslationEntry.COLUMN_NAME_LANGUAGE, translation.getLanguage());
            values.put(TranslationContract.TranslationEntry.COLUMN_NAME_TRANSLATION, translation.getTranslation());
            values.put(TranslationContract.TranslationEntry.COLUMN_NAME_WORD_ID, word_id);

            // Which row to update, based on the title
            String selection = TranslationContract.TranslationEntry._ID + " = ?";
            String[] selectionArgs = {Integer.toString(translation.getId())};

            int count = db.update(
                    TranslationContract.TranslationEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);

            return translation;
        }
    }

    /**
     * Deletes the specific translation
     *
     * @param translation the translation to delete
     */
    public void delete(Translation translation) {
        // Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();
        // Define 'where' part of query.
        String selection = TranslationContract.TranslationEntry._ID + " = ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = {Integer.toString(translation.getId())};
        // Issue SQL statement.
        db.delete(TranslationContract.TranslationEntry.TABLE_NAME, selection, selectionArgs);
    }
}
