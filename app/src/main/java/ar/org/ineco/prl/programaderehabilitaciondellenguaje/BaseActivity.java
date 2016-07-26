package ar.org.ineco.prl.programaderehabilitaciondellenguaje;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.Iterator;
import java.util.List;

import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.ApplicationContext;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.Menu;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.Option;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.Question;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.util.AudioUtil;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.util.FeedbackDialog;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.util.Utils;

public abstract class BaseActivity extends Activity implements View.OnClickListener {

    public Question currentQuestion;
    public Iterator iterator;

    public int currentQuestionNumber;
    public int totalQuestionNumber;

    private Menu menu = ApplicationContext.getMenu();
    private FeedbackDialog feedbackEnd;
    private FeedbackDialog feedbackCorrectAns;
    private FeedbackDialog feedbackWrongAns;
    private AudioUtil audioUtil = ApplicationContext.getSndUtil();
    private int helpSnd;

    @Override
    protected void onCreate (Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        loadActivityLayout();

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

    public abstract void loadActivityLayout();

    private void onCreateHelper () {

        List<Question> allQuestions = menu.getCurrentLevel().getPendingQuestions();

        if (allQuestions.size() > 0) {

            totalQuestionNumber = allQuestions.size();
            currentQuestionNumber = 1;
            iterator = allQuestions.iterator();
            Log.d(SemanticaActivity.class.getName(), "QNumber: " + currentQuestionNumber + ", TotalQ: " + totalQuestionNumber);
            currentQuestion = (Question) iterator.next();
            drawUI();

        } else {

            menu.getCurrentLevel().check();

            showEndDialogOptions();
        }
    }

    public abstract void drawUI();

    private void showEndDialogOptions () {

        feedbackEnd.show();
    }

    public void checkAnswer (Option thisOption) {

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
    public void onClick(View v) {

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
        feedbackWrongAns.dismiss();
    }
}
