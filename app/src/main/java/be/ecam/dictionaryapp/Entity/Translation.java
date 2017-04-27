package be.ecam.dictionaryapp.Entity;


import android.annotation.TargetApi;
import android.os.Build;

import java.util.Objects;

/**
 * Defines the translation class
 */
public class Translation {
    private int id;
    private String translation;
    private String language;

    public Translation(String translation, String language) {
        this(0, translation, language);
    }

    public Translation(int id, String translation, String language) {
        this.id = id;
        this.translation = translation;
        this.language = language;
    }

    public int getId() {
        return id;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object o) {
        if (o instanceof Translation) {
            Translation translation = (Translation) o;
            if (Objects.equals(this.getTranslation().toLowerCase(), translation.getTranslation().toLowerCase())
                    && Objects.equals(this.getLanguage(), ((Translation) o).getLanguage()))
                return true;
        }
        return false;
    }
}
