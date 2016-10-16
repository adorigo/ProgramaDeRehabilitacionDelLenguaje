package ar.org.ineco.prl.ninios.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ToggleButton;

public class VerdanaToggleButton extends ToggleButton {

    public VerdanaToggleButton (Context context, AttributeSet attrs) {

        super(context, attrs);
        setTypeface(Fonts.getTypeface(context, "fonts/verdana.ttf"));
    }
}
