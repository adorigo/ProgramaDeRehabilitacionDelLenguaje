package ar.org.ineco.prl.programaderehabilitaciondellenguaje;

import android.util.Log;

public class UnirConFlechasActivity extends BaseActivity{


    @Override
    protected int getLayoutResourceId () {

        return 0;
    }

    @Override
    public void drawUI () {

    }

    @Override
    public void onPause () {

        super.onPause();

        Log.d(UnirConFlechasActivity.class.getName(), "Loader Closed, FeedbackDialogs Dismissed.");
    }
}
