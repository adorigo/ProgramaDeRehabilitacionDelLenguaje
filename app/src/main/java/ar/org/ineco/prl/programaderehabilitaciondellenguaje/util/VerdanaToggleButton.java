package ar.org.ineco.prl.programaderehabilitaciondellenguaje.util;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.widget.ToggleButton;

import ar.org.ineco.prl.programaderehabilitaciondellenguaje.R;

public class VerdanaToggleButton extends ToggleButton {

    public VerdanaToggleButton (Context context, AttributeSet attrs) {

        super(context, attrs);
        setTypeface(Fonts.getTypeface(context, "fonts/verdana.ttf"));
    }
}
