package ar.org.ineco.prl.programaderehabilitaciondellenguaje.util;

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;
import java.util.Map;

public class Fonts {

    private static final Map<String, Typeface> typefaces = new HashMap<>();

    public static Typeface getTypeface(Context ctx, String fontName) {
        Typeface typeface = typefaces.get(fontName);
        if (typeface == null) {
            typeface = Typeface.createFromAsset(ctx.getAssets(), fontName);
            typefaces.put(fontName, typeface);
        }
        return typeface;
    }
}
