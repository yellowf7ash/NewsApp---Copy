package com.example.anhdinh.newsapp;

import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
    }

    public static class NewsPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener{
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);

            // Find the preference we're interested in
            Preference category = findPreference(getString(R.string.settings_category_key));
            // bind the preference value and display it in Preference Summary
            bindPreferenceSummaryToValue(category);

            Preference orderBy = findPreference(getString(R.string.settings_order_by_key));
            bindPreferenceSummaryToValue(orderBy);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            // The code in this method takes care of updating the displayed preference summary after it has been changed
            String stringValue = newValue.toString();
            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int prefIndex = listPreference.findIndexOfValue(stringValue);
                if (prefIndex >= 0) {
                    CharSequence[] labels = listPreference.getEntries();
                    preference.setSummary(labels[prefIndex]);
                }
            } else {
                preference.setSummary(stringValue);
            }

            return true;
        }

        private void bindPreferenceSummaryToValue(Preference preference){
            // Listen for the changes to the preference we pass in
            preference.setOnPreferenceChangeListener(this);

            //read the current value of the preference stored in the SharedPreferences on the device,
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());

            // and display that in the preference summary (so that the user can see the current value of the preference):
            String preferenceString = preferences.getString(preference.getKey(),"");
            onPreferenceChange(preference,preferenceString);
        }
    }
}
