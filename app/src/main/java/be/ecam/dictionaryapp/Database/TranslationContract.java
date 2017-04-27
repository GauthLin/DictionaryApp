package be.ecam.dictionaryapp.Database;

import android.provider.BaseColumns;

public class TranslationContract {
    private TranslationContract() {
    }

    /* Inner class that defines the table contents */
    public static class TranslationEntry implements BaseColumns {
        public static final String TABLE_NAME = "translations";
        public static final String COLUMN_NAME_WORD_ID = "word_id";
        public static final String COLUMN_NAME_TRANSLATION = "translation";
        public static final String COLUMN_NAME_LANGUAGE = "language";
    }
}
