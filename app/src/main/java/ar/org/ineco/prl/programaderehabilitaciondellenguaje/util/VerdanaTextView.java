package ar.org.ineco.prl.programaderehabilitaciondellenguaje.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import ar.org.ineco.prl.programaderehabilitaciondellenguaje.util.Fonts;

public class VerdanaTextView extends TextView {

    public VerdanaTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(Fonts.getTypeface(context, "fonts/verdana.ttf"));
    }
}
