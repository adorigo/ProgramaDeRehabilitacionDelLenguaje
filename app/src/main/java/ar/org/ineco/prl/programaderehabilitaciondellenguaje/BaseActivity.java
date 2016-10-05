package ar.org.ineco.prl.programaderehabilitaciondellenguaje;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.util.Iterator;
import java.util.List;

import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.ApplicationContext;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.Menu;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.Option;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.Question;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.SoundFile;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.util.AudioUtil;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.util.FeedbackDialog;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.util.Utils;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.util.VerdanaTextView;

public abstract class BaseActivity extends Activity implements View.OnClickListener {

    public Question currentQuestion;
    public Iterator iterator;

    public int currentQuestionNumber;
    public int totalQuestionNumber;
    public float completeRate;
    public float lvlRate;

    private Menu menu = ApplicationContext.getMenu();
    private FeedbackDialog feedbackEnd;
    private FeedbackDialog feedbackCorrectAns;
    private FeedbackDialog feedbackWrongAns;
    private FeedbackDialog feedbackInfo;
    private ProgressBar vProgress;
    private AudioUtil audioUtil = ApplicationContext.getSndUtil();
    private int helpSnd;

    @Override
    public void onCreate (Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(getLayoutResourceId());

        VerdanaTextView textTitle = (VerdanaTextView) findViewById(R.id.textTitle);
        textTitle.setText(getResources().getString(getStringResourceId()));

        feedbackEnd = new FeedbackDialog(this, R.layout.activity_quiz_popup_end);
        feedbackEnd.findViewById(R.id.buttonGoBack).setOnClickListener(this);

        feedbackCorrectAns = new FeedbackDialog(this, R.layout.activity_quiz_popup_correctans);
        feedbackCorrectAns.findViewById(R.id.buttonNext).setOnClickListener(this);

        feedbackWrongAns = new FeedbackDialog(this, R.layout.activity_quiz_popup_incorrectans);
        feedbackWrongAns.findViewById(R.id.buttonTryAgain).setOnClickListener(this);

        feedbackInfo = new FeedbackDialog(this, R.layout.activity_quiz_popup_info);
        feedbackInfo.findViewById(R.id.buttonGoBackInfo).setOnClickListener(this);
        VerdanaTextView tView = (VerdanaTextView) feedbackInfo.findViewById(R.id.textPopup);

        if (Utils.withDragNDrop(this)) {
            tView.setText(getResources().getString(R.string.explanationIntruso));

        } else {
            tView.setText(getResources().getString(R.string.explanationIntrusoWithoutDD));
        }

        ImageView helpInfo = (ImageView) findViewById(R.id.infoButton);

        if (helpInfo != null) {

            helpInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(feedbackInfo.getWindow().getAttributes());
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;

                    feedbackInfo.show();
                    feedbackInfo.getWindow().setAttributes(lp);
                }
            });
        }

        lvlRate = Utils.lvlRate(this, menu.getCurrentCategory());

        vProgress = (ProgressBar) findViewById(R.id.progressBar);
        if (vProgress != null) {
            vProgress.setProgress(Math.round(completeRate * 100 / lvlRate));
        }

        ImageView helpSndLayout = (ImageView) findViewById(R.id.audioAyuda);

        if (menu.getAudioCategory() != null) {

            try {
                helpSnd = getResources().getIdentifier(menu.getAudioCategory().getName(), "raw", this.getPackageName());
                audioUtil.loadSound(helpSnd);
            } catch (RuntimeException rE) {
                Log.d(BaseActivity.class.getName(), rE.getMessage());
                helpSnd = -1;
            }


            helpSndLayout.setOnClickListener(this);

        } else {

            helpSndLayout.setVisibility(ImageView.GONE);
        }

        onCreateHelper();
    }

    protected abstract int getStringResourceId();

    protected abstract int getLayoutResourceId ();

    public int loadSound (SoundFile sndFile) {

        try {

            int sndId = getResources().getIdentifier(sndFile.getName(), "raw", this.getPackageName());
            audioUtil.loadSound(sndId);

            return sndId;

        } catch (RuntimeException rE) {
            Log.d(BaseActivity.class.getName(), rE.getMessage());
            return -1;
        }


    }

    public void playSound (int sndId) {

        if (sndId >= 0 && Utils.getSoundStimulus(this)) audioUtil.playSound(sndId);
    }

    private void onCreateHelper () {

        List<Question> allQuestions = menu.getCurrentLevel().getPendingQuestions();

        if (allQuestions.size() > 0) {

            totalQuestionNumber = allQuestions.size();
            currentQuestionNumber = 0;
            iterator = allQuestions.iterator();
            Log.d(BaseActivity.class.getName(), "QNumber: " + currentQuestionNumber + ", TotalQ: " + totalQuestionNumber);
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
        if (currentQuestion.makeTry(thisOption)) {

            answerCorrect();

        } else {

            answerIncorrect();
        }
    }

    public void answerCorrect() {

        currentQuestion.check();

        if (currentQuestion.isFirstTry()) {
            currentQuestionNumber++;
        }

        feedbackCorrectAns.show();

        if (Utils.withSound(this)) {
            audioUtil.playSound(R.raw.acierto);
        }
    }

    public void answerIncorrect() {

        feedbackWrongAns.show();

        if (Utils.withSound(this)) {
            audioUtil.playSound(R.raw.error);
        }
    }

    public void nextQuestion () {

        if (feedbackCorrectAns.isShowing()) {

            feedbackCorrectAns.hide();
        }

        completeRate = ((float) currentQuestionNumber) / totalQuestionNumber;

        if (Float.isNaN(lvlRate)) {
            lvlRate = Utils.lvlRate(this, menu.getCurrentCategory());
        }

        int percentDone = Math.round(completeRate * 100 / lvlRate);

        if (vProgress != null) {
            vProgress.setProgress(percentDone);
        }

        Log.d(BaseActivity.class.getName(), "Current Questions done: "+ currentQuestionNumber);
        Log.d(BaseActivity.class.getName(), completeRate + "% done of " + lvlRate);

        if (iterator.hasNext() && completeRate < lvlRate) {

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

            case R.id.buttonGoBack:
                goBack();
                break;

            case R.id.buttonNext:
                nextQuestion();
                break;

            case R.id.audioAyuda:
                if (helpSnd >= 0) audioUtil.playSound(helpSnd);
                break;

            case R.id.buttonTryAgain:
                feedbackWrongAns.dismiss();
                break;

            case R.id.buttonGoBackInfo:
                feedbackInfo.dismiss();
                break;
        }
    }

    @Override
    public void onPause () {

        super.onPause();

        feedbackEnd.dismiss();
        feedbackCorrectAns.dismiss();
        feedbackWrongAns.dismiss();
        feedbackInfo.dismiss();
    }
}
