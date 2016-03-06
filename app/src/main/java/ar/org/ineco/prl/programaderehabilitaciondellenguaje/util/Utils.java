package ar.org.ineco.prl.programaderehabilitaciondellenguaje.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class Utils {

    private static final String pinNumber = "1234";

    public static boolean withSound (Context context) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getBoolean("pref_key_feedback", true);
    }

    public static float lvlRate (Context context) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String value = sharedPref.getString("pref_key_lvlrate", "15");

        Log.d(Utils.class.getName(), value);

        return Integer.valueOf(value) / 100.0f;
    }

    public static String getPinPassword () {

        return pinNumber;
    }
}
