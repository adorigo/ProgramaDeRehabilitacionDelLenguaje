package ar.org.ineco.prl.programaderehabilitaciondellenguaje.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Utils {

    public static boolean withSound(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getBoolean("pref_key_feedback", true);
    }
}
