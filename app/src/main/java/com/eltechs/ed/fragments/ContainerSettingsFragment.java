package com.eltechs.ed.fragments;

import android.content.*;
import android.content.SharedPreferences.*;
import android.os.*;
import android.support.v7.app.*;
import android.support.v7.preference.*;
// import com.eltechs.ed.guestContainers.*;
import com.kdt.eltechsaxs.*;

import com.kdt.eltechsaxs.R;

public class ContainerSettingsFragment extends PreferenceFragmentCompat implements OnSharedPreferenceChangeListener {
    public static final String ARG_CONT_ID = "CONT_ID";

    public void onCreatePreferences(Bundle bundle, String str) {
        PreferenceManager preferenceManager = getPreferenceManager();
        preferenceManager.setSharedPreferencesName("com.eltechs.ed.CONTAINER_CONFIG_" + Long.toString(getArguments().getLong("CONT_ID")));
        setPreferencesFromResource(R.xml.container_prefs, str);
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle((int) R.string.wd_title_container_prop);
    }

    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        for (int i = 0; i < getPreferenceScreen().getPreferenceCount(); i++) {
            Preference preference = getPreferenceScreen().getPreference(i);
            updatePreference(preference);
            preference.setSingleLineTitle(false);
        }
    }

    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String str) {
        updatePreference(findPreference(str));
    }

    private void updatePreference(Preference preference) {
        if (preference != null && (preference instanceof EditTextPreference)) {
            EditTextPreference editTextPreference = (EditTextPreference) preference;
            editTextPreference.setSummary((CharSequence) editTextPreference.getText());
        }
    }
}
