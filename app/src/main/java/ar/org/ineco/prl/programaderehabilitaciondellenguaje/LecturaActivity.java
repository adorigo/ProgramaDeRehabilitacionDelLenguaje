package ar.org.ineco.prl.programaderehabilitaciondellenguaje;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.Option;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.util.VerdanaButton;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.util.VerdanaTextView;

public class LecturaActivity extends BaseActivity {

    @Override
    public void onCreate (Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResourceId () {

        return R.layout.activity_lectura;
    }

    @Override
    public void drawUI () {

        if (currentQuestion != null) {

            VerdanaTextView title = (VerdanaTextView) findViewById(R.id.textTitle);
            title.setText(currentQuestion.getText());

            ScrollView textLayout = (ScrollView) findViewById(R.id.layoutText);
            textLayout.removeAllViews();

            LinearLayout nextButtonLayout = (LinearLayout) findViewById(R.id.layoutNextButton);
            nextButtonLayout.removeAllViews();

            if (currentQuestion.getNOpts() == 0) {
                super.nextQuestion();
            }

            Option oText = currentQuestion.getOpts().get(0);
            VerdanaTextView vText = new VerdanaTextView(this, null);

            vText.setText(oText.getStr());
            vText.setGravity(Gravity.CENTER);
            vText.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_large));

            if (oText.getSnd() != null) {

                final int sndId = loadSound(oText.getSnd());
                vText.setOnLongClickListener(new View.OnLongClickListener() {

                    @Override
                    public boolean onLongClick (View v) {

                        playSound(sndId);
                        return false;
                    }
                });
            }

            Handler handler = new Handler();
            Runnable run = new Runnable() {

                @Override
                public void run () {

                    addNextButton();
                }
            };

            handler.postDelayed(run, 2000);
        }
    }

    public void addNextButton () {

        LinearLayout nextButtonLayout = (LinearLayout) findViewById(R.id.layoutNextButton);

        VerdanaButton nextQuestion = new VerdanaButton(this, null);
        nextQuestion.setText("Siguiente");
        nextQuestion.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick (View v) {

                nextQuestion();
            }
        });

        nextButtonLayout.addView(nextQuestion);
    }

    @Override
    public void onPause () {

        super.onPause();

        Log.d(LecturaActivity.class.getName(), "Loader Closed, FeedbackDialogs Dismissed.");
    }
}
