package ar.org.ineco.prl.programaderehabilitaciondellenguaje;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.widget.EditText;

import java.util.List;

import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.ApplicationContext;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.Category;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.Menu;

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
                ModuloLecturaPreferenceFragment.class.getName().equals(fragmentName) ||
                AvanceDeNivelFragment.class.getName().equals(fragmentName);
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

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class AvanceDeNivelFragment extends PreferenceFragment {

        private PreferenceScreen avanceNivel;
        private Menu menu = ApplicationContext.getMenu();

        @Override
        public void onCreate (Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_avance_nivel);

            avanceNivel = (PreferenceScreen) this.findPreference("avanceNivel");

            for (Category cat : menu.getFirstCategories()) {

                PreferenceCategory prefCategory = new PreferenceCategory(avanceNivel.getContext());
                prefCategory.setTitle(cat.getName());
                prefCategory.setKey(String.valueOf(cat.getId()));

                avanceNivel.addPreference(prefCategory);

                for (Category leaf : cat.getAllLeafs()) {

                    EditTextPreference prefCat = new EditTextPreference(prefCategory.getContext());
                    prefCat.setTitle(leaf.getName());
                    prefCat.setSummary(leaf.getPath());
                    prefCat.setKey(String.valueOf(leaf.getId()));
                    prefCat.setDefaultValue("50");

                    // Solo numeros.
                    EditText et = prefCat.getEditText();
                    et.setInputType(InputType.TYPE_CLASS_NUMBER);

                    prefCategory.addPreference(prefCat);
                }
            }
        }
    }
}
