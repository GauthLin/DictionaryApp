package be.ecam.dictionaryapp;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

/**
 * Created by guill on 28-03-17.
 */

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle bundle, String a) {
        addPreferencesFromResource(R.xml.settings);
    }
}
