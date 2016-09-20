package ar.org.ineco.prl.programaderehabilitaciondellenguaje.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import ar.org.ineco.prl.programaderehabilitaciondellenguaje.R;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.Category;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.Level;

public class Utils {

    private static final String pinNumber = "2785";

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

    public static float getTextSize(Context context) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String value = sharedPref.getString("pref_key_text_size", "med");
        float dim = 18.0f;
        switch (value) {

            case "med":
                dim = context.getResources().getDimension(R.dimen.text_size_medium);
            break;
            case "gra":
                dim = context.getResources().getDimension(R.dimen.text_size_large);
            break;
            case "chi":
                dim = context.getResources().getDimension(R.dimen.text_size_small);
            break;
        }

        return dim;
    }

    public static String getTextType(Context context) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String value = sharedPref.getString("pref_key_text_type", "nor");

        return value;
    }

    public static int getTimeWait(Context context) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String value = sharedPref.getString("pref_key_time_shown", "6");

        return Integer.parseInt(value);
    }

    public static String getPinPassword () {

        return pinNumber;
    }

    public static boolean getSoundStimulus(Context context) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getBoolean("pref_key_estimulos", true);
    }
}
