package ar.org.ineco.prl.programaderehabilitaciondellenguaje;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.v4.content.FileProvider;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.ApplicationContext;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.Category;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.Menu;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.database.DatabaseLoader;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.util.ReportCreator;

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
                AvanceDeNivelFragment.class.getName().equals(fragmentName) ||
                ObtenerReporteFragment.class.getName().equals(fragmentName) ||
                ReiniciarCategoriaFragment.class.getName().equals(fragmentName);
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

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class ReiniciarCategoriaFragment extends PreferenceFragment {

        private PreferenceScreen reiniciarNivel;
        private Menu menu = ApplicationContext.getMenu();

        @Override
        public void onCreate (Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_avance_nivel);

            reiniciarNivel = (PreferenceScreen) this.findPreference("avanceNivel");

            for (Category cat : menu.getFirstCategories()) {

                PreferenceCategory prefCategory = new PreferenceCategory(reiniciarNivel.getContext());
                prefCategory.setTitle(cat.getName());
                prefCategory.setKey(String.valueOf(cat.getId()));

                reiniciarNivel.addPreference(prefCategory);

                for (Category leaf : cat.getAllLeafs()) {

                    Preference prefCat = new Preference(prefCategory.getContext());
                    prefCat.setTitle(leaf.getName());
                    prefCat.setSummary(leaf.getPath());

                    final Category category = leaf;

                    prefCat.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                        @Override
                        public boolean onPreferenceClick(Preference preference) {
                            category.reset();
                            Toast.makeText(reiniciarNivel.getContext(),
                                    "Categoría reiniciada", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    });

                    prefCategory.addPreference(prefCat);
                }
            }
        }
    }

    public static class ObtenerReporteFragment extends PreferenceFragment {

        private PreferenceScreen obtenerReporte;

        @Override
        public void onCreate (Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_obtener_reporte);

            obtenerReporte = (PreferenceScreen) this.findPreference("obtenerReporte");

            PreferenceCategory category = (PreferenceCategory) this.findPreference("pref_key_ot");

            Intent i = new Intent(Intent.ACTION_SEND);
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            i.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            i.setType("plain/text");

            String body = getResources().getString(R.string.bodyReport);
            String subject = getResources().getString(R.string.subjectReport);

            Uri contentUri = ReportCreator.generateCsvReport();

            if(contentUri != null) i.putExtra(Intent.EXTRA_STREAM, contentUri);
            i.putExtra(Intent.EXTRA_SUBJECT, subject);
            i.putExtra(Intent.EXTRA_TEXT, body);
            startActivity(Intent.createChooser(i, "E-mail"));

            Preference back = new Preference(obtenerReporte.getContext());
            back.setTitle("Atrás");
            back.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

                @Override
                public boolean onPreferenceClick (Preference preference) {
                    Intent intent = new Intent(obtenerReporte.getContext(), SettingsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    startActivity(intent);
                    return true;
                }
            });

            category.addPreference(back);
        }
    }
}
