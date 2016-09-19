package ar.org.ineco.prl.programaderehabilitaciondellenguaje.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.Category;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.Level;

public class Utils {

    private static final String pinNumber = "1234";

    public static boolean withSound (Context context) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getBoolean("pref_key_feedback", true);
    }

    public static boolean withDragNDrop (Context context) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return !sharedPref.getBoolean("pref_key_cpmotrices", false);
    }

    public static float lvlRate (Context context, Category cat) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String value = "50";
        String key = String.valueOf(cat.getId());

        if (sharedPref.contains(key)) {
            value = sharedPref.getString(key, "50");
        }

        Log.d(Utils.class.getName(), value);

        return Integer.valueOf(value) / 100.0f;
    }

    public static String getPinPassword () {

        return pinNumber;
    }
}
