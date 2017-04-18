package be.ecam.dictionaryapp.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Définit un mot à traduire
 */
public class Word {
    private int id;
    private String name;
    private List<Translation> translations;

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

    public void addTranslation(Translation translation) {
        translations.add(translation);
    }

    public void removeTranslation(Translation translation) {
        translations.remove(translation);
    }
}
