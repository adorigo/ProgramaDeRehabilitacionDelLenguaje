package ar.org.ineco.prl.ninios.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

public class VerdanaEditText extends EditText {

    public VerdanaEditText (Context context, AttributeSet attrs) {

        super(context, attrs);
        setTypeface(Fonts.getTypeface(context, "fonts/verdana.ttf"));
    }
}
