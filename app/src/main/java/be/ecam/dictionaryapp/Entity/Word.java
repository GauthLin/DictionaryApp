package be.ecam.dictionaryapp.Entity;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Defines a word to translate
 */
public class Word {
    private int id;
    private String name; // word to translation
    private List<Translation> translations; // list of translations of that word

    public Word(String name) {
        this(0, name);
    }

    public Word(int id, String name) {
        this.id = id;
        this.name = name;
        this.translations = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Translation> getTranslations() {
        return translations;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public Translation getTranslation(String lang) {
        for (Translation translation :
                getTranslations()) {
            if (Objects.equals(translation.getLanguage(), lang)) {
                return translation;
            }
        }

        return null;
    }

    public void addTranslation(Translation translation) {
        translations.add(translation);
    }

    public void removeTranslation(Translation translation) {
        translations.remove(translation);
    }
}
