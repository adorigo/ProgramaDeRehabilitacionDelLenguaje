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
import android.widget.ListAdapter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.ApplicationContext;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.Category;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.Menu;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.util.PrefsHeaderAdapter;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.util.ReportCreator;

public class SettingsActivity extends PreferenceActivity {

    private static List<Header> _headers;

    @Override
    public void setListAdapter(ListAdapter adapter) {
        if (adapter == null) {
            super.setListAdapter(null);
        } else {
            super.setListAdapter(new PrefsHeaderAdapter(this, _headers));
        }
    }

    @Override
    public void onBuildHeaders(List<Header> target) {
        _headers = target;
        loadHeadersFromResource(R.xml.pref_headers, target);
    }
    @Override
    public Header onGetInitialHeader() {
        super.onResume();
        if (SettingsActivity._headers != null) {
            for (int i = 0; i < SettingsActivity._headers.size(); i++) {
                Header h = SettingsActivity._headers.get(i);
                if (PrefsHeaderAdapter.getHeaderType(h) != PrefsHeaderAdapter.HEADER_TYPE_CATEGORY) {
                    return h;
                }
            }
        }
        return null;
    }

    /*@Override
    public boolean onIsMultiPane () {

        return isXLargeTablet(this);
    }

    private static boolean isXLargeTablet (Context context) {

        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }*/

    @Override
    public boolean isValidFragment (String fragmentName) {

        return GeneralPreferenceFragment.class.getName().equals(fragmentName) ||
                ModuloLecturaPreferenceFragment.class.getName().equals(fragmentName) ||
                AvanceDeNivelFragment.class.getName().equals(fragmentName) ||
                ObtenerReporteFragment.class.getName().equals(fragmentName);
    }


    /*@Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders (List<Header> target) {

        super.onBuildHeaders(target);
        loadHeadersFromResource(R.xml.pref_headers, target);
    }*/

    public static class GeneralPreferenceFragment extends PreferenceFragment {

        @Override
        public void onCreate (Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
        }
    }

    public static class ModuloLecturaPreferenceFragment extends PreferenceFragment {

        @Override
        public void onCreate (Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_modulo_lectura);
        }
    }

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

    public static class ObtenerReporteFragment extends PreferenceFragment {

        private PreferenceScreen obtenerReporte;

        @Override
        public void onCreate (Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_obtener_reporte);

            obtenerReporte = (PreferenceScreen) this.findPreference("obtenerReporte");

            Intent i = new Intent(Intent.ACTION_SEND);
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            i.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            i.setType("plain/text");

            String body = "Reporte";
            String subject = "Asunto";

            Uri contentUri = ReportCreator.generateCsvReport();

            i.putExtra(Intent.EXTRA_STREAM, contentUri);
            i.putExtra(Intent.EXTRA_SUBJECT, subject);
            i.putExtra(Intent.EXTRA_TEXT, body);
            startActivity(Intent.createChooser(i, "E-mail"));

            Preference back = new Preference(obtenerReporte.getContext());
            back.setTitle("Atras");
            back.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

                @Override
                public boolean onPreferenceClick (Preference preference) {
                    Intent intent = new Intent(obtenerReporte.getContext(), MainMenuActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    startActivity(intent);
                    return true;
                }
            });
            obtenerReporte.addPreference(back);
        }
    }
}
