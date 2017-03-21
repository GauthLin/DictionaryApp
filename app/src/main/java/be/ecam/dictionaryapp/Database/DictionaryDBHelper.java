package be.ecam.dictionaryapp.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
}
