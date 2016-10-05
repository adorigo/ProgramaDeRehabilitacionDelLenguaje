package ar.org.ineco.prl.programaderehabilitaciondellenguaje;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.Option;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.util.Utils;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.util.VerdanaButton;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.util.VerdanaTextView;

public class LecturaActivity extends BaseActivity {

    @Override
    protected int getStringResourceId() {
        return R.string.title_activity_lectura;
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

            vText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            String textToShow = oText.getStr();
            switch (Utils.getTextType(this)) {
                case "may":
                    textToShow = textToShow.toUpperCase();
                    break;
                case "min":
                    textToShow = textToShow.toLowerCase();
                    break;
                case "amb":
                    break;
            }
            vText.setText(textToShow);
            vText.setTextSize(TypedValue.COMPLEX_UNIT_PX, Utils.getTextSize(this));
            textLayout.addView(vText);


            if (oText.getSnd() != null) {

                final int sndId = loadSound(oText.getSnd());
                vText.setOnLongClickListener(new View.OnLongClickListener() {

                    @Override
                    public boolean onLongClick(View v) {

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

            handler.postDelayed(run, Utils.getTimeWait(this) * 1000);
        }
    }

    public void addNextButton () {

        LinearLayout nextButtonLayout = (LinearLayout) findViewById(R.id.layoutNextButton);

        VerdanaButton nextQuestion = new VerdanaButton(this, null);
        nextQuestion.setText("Siguiente");
        nextQuestion.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick (View v) {

                currentQuestion.check();
                currentQuestionNumber++;
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
