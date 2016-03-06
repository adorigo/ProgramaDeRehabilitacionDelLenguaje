package ar.org.ineco.prl.programaderehabilitaciondellenguaje.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

public class VerdanaButton extends Button {

    public VerdanaButton (Context context, AttributeSet attrs) {

        super(context, attrs);
        setTypeface(Fonts.getTypeface(context, "fonts/verdana.ttf"));
    }
}
