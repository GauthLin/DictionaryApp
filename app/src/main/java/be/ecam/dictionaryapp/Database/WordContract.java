package be.ecam.dictionaryapp.Database;


import android.provider.BaseColumns;

public final class WordContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private WordContract() {}

    /* Inner class that defines the table contents */
    public static class WordEntry implements BaseColumns {
        public static final String TABLE_NAME = "word";
        public static final String COLUMN_NAME_NAME = "name";
    }
}
