package ar.org.ineco.prl.ninios.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.AnyRes;
import android.support.annotation.NonNull;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import ar.org.ineco.prl.ninios.R;
import ar.org.ineco.prl.ninios.classes.Category;
import ar.org.ineco.prl.ninios.database.DatabaseLoader;

public class Utils {

    public static boolean withSound (Context context) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getBoolean("pref_key_feedback", true);
    }

    public static boolean withDragNDrop (Context context) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return !sharedPref.getBoolean("pref_key_cpmotrices", true);
    }

    public static float lvlRate (Context context, Category cat) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String value = "50";
        String key = String.valueOf(cat.getId());

        if (sharedPref.contains(key)) {
            value = sharedPref.getString(key, "50");
        }

        Log.d(Utils.class.getName(), value);

        int val = Integer.valueOf(value);
        if (val == 0) {
            val = 1;
        }

        return val / 100.0f;
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

        return DatabaseLoader.getInstance().getConfigValue("cod");
    }

    public static boolean getSoundStimulus(Context context) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getBoolean("pref_key_estimulos", true);
    }

    public static String md5(final String s) {

        final String SHA256 = "SHA-256";

        try {

            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance(SHA256);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();

            for (byte aMessageDigest : messageDigest) {

                String h = Integer.toHexString(0xFF & aMessageDigest);

                while (h.length() < 2) {
                    h = "0" + h;
                }

                hexString.append(h);
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();
            return "";
        }
    }

    public static Uri getUriToResource(@NonNull Context context, @AnyRes int resId) throws Resources.NotFoundException {

        Resources res = context.getResources();

        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + res.getResourcePackageName(resId)
                + '/' + res.getResourceTypeName(resId)
                + '/' + res.getResourceEntryName(resId));
    }

    public static String getUserName(Context context) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String value = sharedPref.getString("pref_key_name", "");

        return value;
    }

    public static String getUserLastName(Context context) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String value = sharedPref.getString("pref_key_lastname", "");

        return value;
    }
}
