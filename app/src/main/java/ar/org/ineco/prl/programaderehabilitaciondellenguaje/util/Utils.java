package ar.org.ineco.prl.programaderehabilitaciondellenguaje.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Utils {

    private static final String pinNumber = "1234";

    public static boolean withSound (Context context) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getBoolean("pref_key_feedback", true);
    }

    public static float lvlRate (Context context) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String value = sharedPref.getString("pref_key_lvlrate", "10");
        float result = Integer.valueOf(value) / 100;
        return result;
    }

    public static String getPinPassword () {

        return pinNumber;
    }
}
