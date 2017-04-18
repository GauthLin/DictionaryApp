package be.ecam.dictionaryapp.Entity;


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

    public Translation(int id, String translation, String language){
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
}
