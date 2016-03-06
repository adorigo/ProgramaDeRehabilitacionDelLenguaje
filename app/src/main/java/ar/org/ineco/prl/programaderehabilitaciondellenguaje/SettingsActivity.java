package ar.org.ineco.prl.programaderehabilitaciondellenguaje;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

import java.util.List;

public class SettingsActivity extends PreferenceActivity {

    @Override
    public boolean onIsMultiPane () {

        return isXLargeTablet(this);
    }

    private static boolean isXLargeTablet (Context context) {

        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    @Override
    public boolean isValidFragment (String fragmentName) {

        return GeneralPreferenceFragment.class.getName().equals(fragmentName) ||
                ModuloLecturaPreferenceFragment.class.getName().equals(fragmentName);
    }


    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders (List<Header> target) {

        super.onBuildHeaders(target);
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment {

        @Override
        public void onCreate (Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class ModuloLecturaPreferenceFragment extends PreferenceFragment {

        @Override
        public void onCreate (Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_modulo_lectura);
        }
    }
}
