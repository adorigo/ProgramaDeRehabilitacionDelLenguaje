package ar.org.ineco.prl.programaderehabilitaciondellenguaje.util;

import android.app.Activity;
import android.app.Dialog;
import android.media.MediaDataSource;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import ar.org.ineco.prl.programaderehabilitaciondellenguaje.R;

import java.io.IOException;

public class FeedbackDialog extends Dialog {

    public FeedbackDialog(Activity thisActivity, int layoutId){
        super(thisActivity);
        LayoutInflater factory = LayoutInflater.from(thisActivity);
        View v = factory.inflate(layoutId, null);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(v);
        setCanceledOnTouchOutside(false);
    }
}
