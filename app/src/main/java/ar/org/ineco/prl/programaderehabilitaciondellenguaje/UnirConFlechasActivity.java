package ar.org.ineco.prl.programaderehabilitaciondellenguaje;

import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.Option;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.Question;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.util.VerdanaTextView;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.util.VerdanaToggleButton;

public class UnirConFlechasActivity extends BaseActivity{


    private int stepsReq;
    private int stepsDone;

    private VerdanaToggleButton viewQ, viewO;
    private Question currentQ;
    private Option currentO;

    @Override
    protected int getLayoutResourceId () {

        return R.layout.activity_unirconflechas;
    }

    @Override
    public void drawUI () {

        if (currentQuestion != null) {

            resetState();

            VerdanaTextView title = (VerdanaTextView) findViewById(R.id.textTitle);
            title.setText(currentQuestion.getText());

            LinearLayout questionLayout, optionsLayout;

            questionLayout = (LinearLayout) findViewById(R.id.layoutQuestion);
            questionLayout.removeAllViews();

            optionsLayout = (LinearLayout) findViewById(R.id.layoutOptions);
            optionsLayout.removeAllViews();

            List<Option> options = new ArrayList<>();
            List<Question> childQuestions = new ArrayList<>(currentQuestion.getChildQuestions());
            stepsReq = childQuestions.size();
            stepsDone = 0;

            long seed = System.nanoTime();
            Collections.shuffle(childQuestions, new Random(seed));

            for (Question child : childQuestions) {

                // add options for later.
                options.addAll(child.getOpts());

                VerdanaToggleButton qButton = new VerdanaToggleButton(this, null);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
                qButton.setLayoutParams(layoutParams);

                qButton.setText(child.getText());
                qButton.setTextOn(child.getText());
                qButton.setTextOff(child.getText());
                qButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_large));

                qButton.setTag(child);

                qButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick (View v) {

                        markView(v, "Q");
                    }
                });

                questionLayout.addView(qButton);
            }

            Collections.shuffle(options, new Random(seed));

            for (Option option : options) {

                VerdanaToggleButton oButton = new VerdanaToggleButton(this, null);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
                oButton.setLayoutParams(layoutParams);

                oButton.setText(option.getStr());
                oButton.setTextOn(option.getStr());
                oButton.setTextOff(option.getStr());
                oButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_large));

                oButton.setTag(option);

                oButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick (View v) {

                        markView(v, "O");
                    }
                });

                optionsLayout.addView(oButton);
            }
        }
    }

    public void markView(View v, String type) {

        if (type.equals("Q")) {

            if (viewQ != null) {
                viewQ.setChecked(false);
            }
            viewQ = (VerdanaToggleButton) v;
            viewQ.setChecked(true);
            currentQ = (Question) v.getTag();

        } else {

            if (viewO != null) {
                viewO.setChecked(false);
            }
            viewO = (VerdanaToggleButton) v;
            viewO.setChecked(true);
            currentO = (Option) v.getTag();
        }

        if (currentO != null && currentQ != null) {

            Log.d(UnirConFlechasActivity.class.getName(), currentQ.getId()+" - option Id:"+ currentO.getQid());

            if (currentQ.getId() == currentO.getQid()) {

                viewO.setClickable(false);
                viewQ.setClickable(false);
                stepsDone++;

            } else {

                viewO.setChecked(false);
                viewQ.setChecked(false);
            }

            resetState();

            checkEnding();
        }
    }

    public void resetState() {
        viewO = null;
        viewQ = null;
        currentQ = null;
        currentO = null;
    }

    public void checkEnding() {
        if (stepsDone == stepsReq) {
            super.answerCorrect();
        }
    }

    @Override
    public void onPause () {

        super.onPause();

        Log.d(UnirConFlechasActivity.class.getName(), "Loader Closed, FeedbackDialogs Dismissed.");
    }
}
