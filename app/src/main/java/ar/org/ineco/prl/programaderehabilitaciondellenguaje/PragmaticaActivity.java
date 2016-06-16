package ar.org.ineco.prl.programaderehabilitaciondellenguaje;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Iterator;
import java.util.List;

import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.ApplicationContext;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.ImageFile;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.Menu;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.Option;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.Question;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.util.AudioUtil;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.util.FeedbackDialog;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.util.Utils;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.util.VerdanaButton;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.util.VerdanaTextView;

public class PragmaticaActivity extends Activity implements View.OnClickListener {

    private Question currentQuestion;
    private Iterator iterator;

    private int currentQuestionNumber;
    private int totalQuestionNumber;

    private Menu menu = ApplicationContext.getMenu();
    private FeedbackDialog feedbackEnd;
    private FeedbackDialog feedbackCorrectAns;
    private FeedbackDialog feedbackWrongAns;
    private AudioUtil audioUtil = ApplicationContext.getSndUtil();
    private int helpSnd;

    @Override
    protected void onCreate (Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pragmatica);

        TextView qstText = (TextView) findViewById(R.id.textTitle);
        qstText.setText(menu.getLabel());

        feedbackEnd = new FeedbackDialog(this, R.layout.activity_quiz_popup_end);
        feedbackEnd.findViewById(R.id.buttonGoBack).setOnClickListener(this);

        feedbackCorrectAns = new FeedbackDialog(this, R.layout.activity_quiz_popup_correctans);
        feedbackCorrectAns.findViewById(R.id.buttonNext).setOnClickListener(this);

        feedbackWrongAns = new FeedbackDialog(this, R.layout.activity_quiz_popup_incorrectans);
        feedbackWrongAns.findViewById(R.id.buttonTryAgain).setOnClickListener(this);

        ImageView helpSndLayout = (ImageView) findViewById(R.id.audioAyuda);

        if (menu.getAudioCategory() != null) {

            helpSnd = getResources().getIdentifier(menu.getAudioCategory().getName(), "raw", this.getPackageName());
            audioUtil.loadSound(helpSnd);

            helpSndLayout.setOnClickListener(this);

        } else {

            helpSndLayout.setVisibility(ImageView.GONE);
        }

        onCreateHelper();
    }

    private void onCreateHelper () {

        List<Question> allQuestions = menu.getCurrentLevel().getPendingQuestions();

        if (allQuestions.size() > 0) {

            totalQuestionNumber = menu.getCurrentLevel().getQuestions().size();
            currentQuestionNumber = 0;
            iterator = allQuestions.iterator();
            Log.d(PragmaticaActivity.class.getName(), "QNumber: " + currentQuestionNumber + ", TotalQ: " + totalQuestionNumber);
            currentQuestion = (Question) iterator.next();
            drawUI();

        } else {

            menu.getCurrentLevel().check();

            showEndDialogOptions();
        }
    }

    private void drawUI () {

        if (currentQuestion != null) {

            VerdanaTextView title = (VerdanaTextView) findViewById(R.id.textTitle);
            title.setText("");

            LinearLayout questionLayout = (LinearLayout) findViewById(R.id.layoutQuestion);
            questionLayout.removeAllViews();

            LinearLayout nextButtonLayout = (LinearLayout) findViewById(R.id.layoutNextButton);
            nextButtonLayout.removeAllViews();

            LinearLayout optionsLayout = (LinearLayout) findViewById(R.id.layoutOptions);
            optionsLayout.removeAllViews();

            if (currentQuestion.getImages().size() > 0) {

                title.setText(currentQuestion.getText());

                int margin = getResources().getDimensionPixelSize(R.dimen.imgMargin);
                double maxwidth = getResources().getDisplayMetrics().widthPixels / currentQuestion.getImages().size();
                double size = getResources().getDisplayMetrics().heightPixels * 0.65;
                //Log.d(PragmaticaActivity.class.getName(), "size: "+size);
                for (ImageFile img : currentQuestion.getImages()) {

                    ImageView image = new ImageView(this);

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, (int) size);
                    layoutParams.setMargins(margin, margin, margin, margin);

                    image.setLayoutParams(layoutParams);
                    image.setMaxWidth((int) maxwidth);
                    image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    image.setAdjustViewBounds(true);

                    image.setImageResource(getResources().getIdentifier(img.getName(), "drawable", this.getPackageName()));

                    questionLayout.addView(image);

                    Log.d(PragmaticaActivity.class.getName(), "Adding ImageView " + img.getName());
                }

                VerdanaButton nextButton = new VerdanaButton(this, null);

                nextButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));

                nextButton.setText(getResources().getString(R.string.buttonNext));
                nextButton.setGravity(Gravity.CENTER_HORIZONTAL);

                nextButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        showOptions();
                    }
                });

                nextButtonLayout.addView(nextButton);

            } else {

                title.setText(R.string.title_activity_pragmatica);

                VerdanaTextView questionText = new VerdanaTextView(this, null);

                questionText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));

                questionText.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.questionText));
                questionText.setText(currentQuestion.getText());
                questionText.setGravity(Gravity.CENTER_HORIZONTAL);

                questionLayout.addView(questionText);

                showOptions();
            }
        }
    }

    private void showOptions () {

        if (currentQuestion.getImages().size() >= 1) {

            LinearLayout questionLayout = (LinearLayout) findViewById(R.id.layoutQuestion);
            questionLayout.removeAllViews();

            LinearLayout nextButtonLayout = (LinearLayout) findViewById(R.id.layoutNextButton);
            nextButtonLayout.removeAllViews();
        }

        LinearLayout optionsLayout = (LinearLayout) findViewById(R.id.layoutOptions);
        optionsLayout.removeAllViews();

        for (Option option : currentQuestion.getOpts()) {

            VerdanaButton optionButton = new VerdanaButton(this, null);

            optionButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            optionButton.setText(option.getStr());
            optionButton.setGravity(Gravity.CENTER_HORIZONTAL);

            optionButton.setTag(option);

            optionButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick (View v) {

                    Option optSelected = (Option) v.getTag();
                    checkAnswer(optSelected);
                }
            });

            optionsLayout.addView(optionButton);
        }
    }

    private void showEndDialogOptions () {

        feedbackEnd.show();
    }

    private void checkAnswer (Option thisOption) {

        if (thisOption.checkAns()) {

            currentQuestion.check();

            feedbackCorrectAns.show();

            if (Utils.withSound(this)) {
                audioUtil.playSound(R.raw.acierto);
            }

        } else {

            feedbackWrongAns.show();

            if (Utils.withSound(this)) {
                audioUtil.playSound(R.raw.error);
            }
        }
    }

    public void nextQuestion () {

        feedbackCorrectAns.hide();

        currentQuestionNumber++;

        float completeRate = ((float) currentQuestionNumber) / totalQuestionNumber;

        Log.d(PragmaticaActivity.class.getName(), "QNumber: " + currentQuestionNumber + ", TotalQ: " + totalQuestionNumber);

        Log.d(PragmaticaActivity.class.getName(),
                completeRate
                        + " <= "
                        + Utils.lvlRate(this)
        );


        if (iterator.hasNext() && completeRate <= Utils.lvlRate(this)) {

            currentQuestion = (Question) iterator.next();
            drawUI();

        } else {

            menu.getCurrentLevel().check();

            showEndDialogOptions();
        }
    }

    public void resetQuestions () {

        feedbackEnd.hide();

        menu.getCurrentLevel().resetQuestions();

        onCreateHelper();
    }

    public void goBack () {

        feedbackEnd.hide();

        menu.setCurrentLevel(null);

        Intent intent = new Intent(this, LevelsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
    }

    @Override
    public void onClick (View v) {

        switch (v.getId()) {

            case R.id.buttonTryAgain:
                feedbackWrongAns.dismiss();
                break;

            case R.id.buttonGoBack:
                goBack();
                break;

            case R.id.buttonNext:
                nextQuestion();
                break;

            case R.id.audioAyuda:
                audioUtil.playSound(helpSnd);
                break;
        }
    }

    @Override
    public void onPause () {

        super.onPause();

        feedbackEnd.dismiss();
        feedbackCorrectAns.dismiss();
        audioUtil.unloadAll();

        Log.d(PragmaticaActivity.class.getName(), "Loader Closed, FeedbackDialogs Dismissed.");
    }
}
