package ar.org.ineco.prl.programaderehabilitaciondellenguaje;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Iterator;
import java.util.List;

import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.ApplicationContext;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.Menu;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.Option;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.Question;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.util.AudioUtil;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.util.DragNDrop.LongClickListener;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.util.FeedbackDialog;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.util.Utils;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.util.VerdanaButton;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.util.VerdanaTextView;

public class IntrusoActivity extends Activity implements android.view.View.OnClickListener {

    private Question currentQuestion;
    private Iterator iterator;

    private int currentQuestionNumber;
    private int totalQuestionNumber;

    private Menu menu = ApplicationContext.getMenu();
    private FeedbackDialog feedbackEnd;
    private FeedbackDialog feedbackCorrectAns;
    private FeedbackDialog feedbackWrongAns;
    private AudioUtil audioUtil = ApplicationContext.getSndUtil();

    @Override
    protected void onCreate (Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_intruso);

        feedbackEnd = new FeedbackDialog(this, R.layout.activity_quiz_popup_end);
        feedbackEnd.findViewById(R.id.buttonGoBack).setOnClickListener(this);

        feedbackCorrectAns = new FeedbackDialog(this, R.layout.activity_quiz_popup_correctans);
        feedbackCorrectAns.findViewById(R.id.buttonNext).setOnClickListener(this);

        feedbackWrongAns = new FeedbackDialog(this, R.layout.activity_quiz_popup_incorrectans);
        feedbackWrongAns.findViewById(R.id.buttonTryAgain).setOnClickListener(this);

        audioUtil.loadSound(R.raw.categorizacion_palabras);

        ImageView audioAyuda = (ImageView) findViewById(R.id.audioAyuda);
        audioAyuda.setOnClickListener(this);

        onCreateHelper();
    }

    private void onCreateHelper () {

        List<Question> allQuestions = menu.getCurrentLevel().getPendingQuestions();

        if (allQuestions.size() > 0) {

            totalQuestionNumber = allQuestions.size();
            currentQuestionNumber = 1;
            iterator = allQuestions.iterator();
            currentQuestion = (Question) iterator.next();
            drawUI();

        } else {

            menu.getCurrentLevel().check();

            showEndDialogOptions();
        }
    }

    private void drawUI () {

        if (currentQuestion != null) {

            Log.d(IntrusoActivity.class.getName(), "PregId: " + currentQuestion.getId());

            int margin = getResources().getDimensionPixelSize(R.dimen.imgMargin);
            double maxwidth = getResources().getDisplayMetrics().widthPixels / currentQuestion.getOpts().size();
            double width = getResources().getDisplayMetrics().widthPixels * 0.20;
            double size = getResources().getDisplayMetrics().heightPixels * 0.35;
            double height = getResources().getDisplayMetrics().heightPixels * 0.20;

            VerdanaTextView title = (VerdanaTextView) findViewById(R.id.textTitle);
            title.setText(currentQuestion.getText());

            LinearLayout optionsLayout1 = (LinearLayout) findViewById(R.id.layoutOptions1);
            optionsLayout1.removeAllViews();

            LinearLayout optionsLayout2 = (LinearLayout) findViewById(R.id.layoutOptions2);
            optionsLayout2.removeAllViews();

            ImageView dropLayout = (ImageView) findViewById(R.id.dropArea);
            dropLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, (int) height));
            dropLayout.setScaleType(ImageView.ScaleType.FIT_XY);
            dropLayout.setAdjustViewBounds(true);
            if (Utils.withDragNDrop(this)) {

                dropLayout.setOnDragListener(new View.OnDragListener() {

                    @Override
                    public boolean onDrag (View v, DragEvent event) {

                        int dragEvent = event.getAction();
                        ImageView bin = (ImageView) v;

                        switch (dragEvent) {
                            case DragEvent.ACTION_DRAG_EXITED:

                                bin.setImageResource(R.drawable.closedbin);
                                break;
                            case DragEvent.ACTION_DRAG_ENTERED:

                                bin.setImageResource(R.drawable.openedbin);
                                break;
                            case DragEvent.ACTION_DROP:

                                View view = (View) event.getLocalState();
                                Option option = (Option) view.getTag();
                                bin.setImageResource(R.drawable.closedbin);

                                checkAnswer(option);
                                break;
                        }

                        return true;
                    }
                });

            } else {
                dropLayout.setVisibility(ImageView.GONE);
            }


            boolean first = true;

            for (Option option : currentQuestion.getOpts()) {

                if (option.getImg() != null) {

                    ImageView image = new ImageView(this);

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, (int) size);
                    layoutParams.setMargins(margin, margin, margin, margin);

                    image.setLayoutParams(layoutParams);
                    image.setMaxWidth((int) maxwidth);
                    image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    image.setAdjustViewBounds(true);

                    image.setTag(option);

                    if (Utils.withDragNDrop(this)) {
                        image.setOnLongClickListener(new LongClickListener());
                    } else {
                        image.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick (View v) {

                                Option optSelected = (Option) v.getTag();
                                checkAnswer(optSelected);
                            }
                        });
                    }


                    image.setImageResource(getResources().getIdentifier(option.getImg().getName(), "drawable", this.getPackageName()));

                    if (first) {
                        optionsLayout2.addView(image);
                        first = false;
                    } else {
                        optionsLayout1.addView(image);
                    }

                } else {

                    VerdanaButton button = new VerdanaButton(this, null);

                    button.setText(option.getStr());
                    button.setWidth((int) width);
                    button.setHeight((int) size);

                    button.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));

                    button.setGravity(Gravity.CENTER);

                    button.setTag(option);

                    if (Utils.withDragNDrop(this)) {
                        button.setOnLongClickListener(new LongClickListener());
                    } else {
                        button.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick (View v) {

                                Option optSelected = (Option) v.getTag();
                                checkAnswer(optSelected);
                            }
                        });
                    }


                    if (first) {
                        optionsLayout2.addView(button);
                        first = false;
                    } else {
                        optionsLayout1.addView(button);
                    }
                }
            }
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
                feedbackWrongAns.hide();
                break;

            case R.id.buttonGoBack:
                goBack();
                break;

            case R.id.buttonNext:
                nextQuestion();
                break;

            case R.id.audioAyuda:
                audioUtil.playSound(R.raw.categorizacion_palabras);
                break;
        }
    }

    @Override
    public void onPause () {

        super.onPause();

        feedbackEnd.dismiss();
        feedbackCorrectAns.dismiss();
        feedbackWrongAns.dismiss();

        Log.d(IntrusoActivity.class.getName(), "Loader Closed, FeedbackDialogs Dismissed.");
    }
}
