package ar.org.ineco.prl.programaderehabilitaciondellenguaje;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Toast;

import java.util.Iterator;
import java.util.List;

import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.ApplicationContext;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.ImageFile;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.Menu;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.Option;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.Question;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.util.AudioUtil;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.util.DragNDrop.LongClickListener;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.util.FeedbackDialog;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.util.Utils;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.util.VerdanaButton;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.util.VerdanaTextView;

public class SemanticaActivity extends Activity implements android.view.View.OnClickListener {

    private Question currentQuestion;
    private Iterator iterator;

    private int currentQuestionNumber;
    private int totalQuestionNumber;

    private Menu menu = ApplicationContext.getMenu();
    private FeedbackDialog feedbackEnd;
    private FeedbackDialog feedbackCorrectAns;
    private AudioUtil audioUtil = ApplicationContext.getSndUtil();

    @Override
    protected void onCreate (Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (menu.getCurrentLevel().getCatid() == 31 || menu.getCurrentLevel().getCatid() == 32) {
            setContentView(R.layout.activity_intruso);
        } else {
            setContentView(R.layout.activity_semantica);
        }


        feedbackEnd = new FeedbackDialog(this, R.layout.activity_quiz_popup_end);
        feedbackEnd.findViewById(R.id.buttonGoBack).setOnClickListener(this);

        feedbackCorrectAns = new FeedbackDialog(this, R.layout.activity_quiz_popup_correctans);
        feedbackCorrectAns.findViewById(R.id.buttonNext).setOnClickListener(this);

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
            Log.d(PragmaticaActivity.class.getName(), "QNumber: " + currentQuestionNumber + ", TotalQ: " + totalQuestionNumber);
            currentQuestion = (Question) iterator.next();
            drawUI();

        } else {

            menu.getCurrentLevel().check();

            showEndDialogOptions();
        }
    }

    private void drawUI() {

        if (currentQuestion != null) {

            VerdanaTextView title = (VerdanaTextView) findViewById(R.id.textTitle);
            title.setText(currentQuestion.getText());

            LinearLayout questionLayout, optionsLayout, optionsLayout1, optionsLayout2;

            if (menu.getCurrentLevel().getCatid() == 31 || menu.getCurrentLevel().getCatid() == 32) {

                optionsLayout1 = (LinearLayout) findViewById(R.id.layoutOptions1);
                optionsLayout1.removeAllViews();

                optionsLayout2 = (LinearLayout) findViewById(R.id.layoutOptions2);
                optionsLayout2.removeAllViews();

                boolean first = true;

                for (Option option : currentQuestion.getOpts()) {

                    Log.d(SemanticaActivity.class.getName(), "Amount Opts: " + currentQuestion.getOpts().size());

                    View OptionDraggable;
                    int margin = getResources().getDimensionPixelSize(R.dimen.imgMargin);
                    double maxwidth = getResources().getDisplayMetrics().widthPixels / currentQuestion.getOpts().size();
                    double size = getResources().getDisplayMetrics().heightPixels * 0.35;

                    if (option.getImg() != null) {

                        ImageView image = new ImageView(this);

                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, (int) size);
                        layoutParams.setMargins(margin, margin, margin, margin);

                        image.setLayoutParams(layoutParams);
                        image.setMaxWidth((int) maxwidth);
                        image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                        image.setAdjustViewBounds(true);

                        image.setTag(option);

                        image.setOnLongClickListener(new LongClickListener());

                        image.setImageResource(getResources().getIdentifier(option.getImg().getName(), "drawable", this.getPackageName()));
                        Log.d(SemanticaActivity.class.getName(), "Adding ImageView " + option.getImg().getName());

                        OptionDraggable = image;

                    } else {

                        VerdanaButton button = new VerdanaButton(this, null);

                        button.setText(option.getStr());
                        button.setEms(3);

                        button.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));

                        button.setGravity(Gravity.CENTER_HORIZONTAL);

                        button.setTag(option);

                        button.setOnLongClickListener(new LongClickListener());

                        OptionDraggable = button;
                    }

                    if (first) {
                        optionsLayout2.addView(OptionDraggable);
                        first = false;
                    } else {
                        optionsLayout1.addView(OptionDraggable);
                    }
                }

            } else {

                questionLayout = (LinearLayout) findViewById(R.id.layoutQuestion);
                questionLayout.removeAllViews();

                optionsLayout = (LinearLayout) findViewById(R.id.layoutOptions);
                optionsLayout.removeAllViews();

                if (currentQuestion.getImages().size() > 0) {

                    int margin = getResources().getDimensionPixelSize(R.dimen.imgMargin);
                    double maxwidth = getResources().getDisplayMetrics().widthPixels / currentQuestion.getImages().size();
                    double size = getResources().getDisplayMetrics().heightPixels * 0.45;

                    for (ImageFile img : currentQuestion.getImages()) {

                        if (img.getName() != null) {

                            ImageView image = new ImageView(this);

                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, (int) size);
                            layoutParams.setMargins(margin, margin, margin, margin);

                            image.setLayoutParams(layoutParams);
                            image.setMaxWidth((int) maxwidth);
                            image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                            image.setAdjustViewBounds(true);

                            image.setImageResource(getResources().getIdentifier(img.getName(), "drawable", this.getPackageName()));

                            questionLayout.addView(image);

                            Log.d(SemanticaActivity.class.getName(), "Adding ImageView " + img.getName());
                        } else {

                            VerdanaTextView text = new VerdanaTextView(this, null);

                            text.setText(img.getTxtImg());
                            text.setWidth((int) maxwidth);
                            text.setHeight((int) size);
                            text.setGravity(Gravity.CENTER);
                            text.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_large));

                            questionLayout.addView(text);

                            Log.d(SemanticaActivity.class.getName(), "Adding TextView " + img.getTxtImg());
                        }
                    }
                }

                for (Option option : currentQuestion.getOpts()) {

                    VerdanaButton optionButton = new VerdanaButton(this, null);

                    optionButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));

                    optionButton.setText(option.getStr());
                    optionButton.setGravity(Gravity.CENTER_HORIZONTAL);

                    optionButton.setTag(option);

                    optionButton.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            Option optSelected = (Option) v.getTag();
                            checkAnswer(optSelected);
                        }
                    });

                    optionsLayout.addView(optionButton);
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

            Toast.makeText(this, getResources().getString(R.string.incorrectAns), Toast.LENGTH_SHORT).show();   //puto el que lee

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
    public void onClick(View v) {

        switch (v.getId()) {

            /*case R.id.buttonReset:
                resetQuestions();
                break;*/

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
    public void onResume () {

        super.onResume();

        Log.d(SemanticaActivity.class.getName(), "Loader Opened.");
    }

    @Override
    public void onPause () {

        super.onPause();

        feedbackEnd.dismiss();
        feedbackCorrectAns.dismiss();

        Log.d(SemanticaActivity.class.getName(), "Loader Closed, FeedbackDialogs Dismissed.");
    }
}
