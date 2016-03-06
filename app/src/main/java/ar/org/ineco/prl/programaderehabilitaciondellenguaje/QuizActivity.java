package ar.org.ineco.prl.programaderehabilitaciondellenguaje;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.ImageFile;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.Option;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.Question;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.util.AudioUtil;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.database.DatabaseLoader;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.util.FeedbackDialog;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.util.Utils;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.util.VerdanaButton;

import java.util.Iterator;
import java.util.List;

public class QuizActivity extends Activity implements android.view.View.OnClickListener{

    private DatabaseLoader databaseLoader;
    private List<Question> allQuestions;
    private Question currentQuestion;
    private Iterator iterator;
    private int currentQuestionNumber;
    private int totalQuestionNumber;
    private String moduleID;
    private String categoryID;
    private String levelID;
    private FeedbackDialog feedbackEnd;
    private FeedbackDialog feedbackCorrectAns;
    private AudioUtil audioUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        moduleID = getIntent().getStringExtra("Module");
        categoryID = getIntent().getStringExtra("Category");
        levelID = getIntent().getStringExtra("Level");

        TextView qstText = (TextView) findViewById(R.id.textTitle);
        if (categoryID.equals("1")) {
            qstText.setText("Señale la categoría a la que pertenecen estos elementos");
        }

        feedbackEnd = new FeedbackDialog(this, R.layout.activity_quiz_popup_end);
        feedbackEnd.findViewById(R.id.buttonReset).setOnClickListener(this);
        feedbackEnd.findViewById(R.id.buttonGoBack).setOnClickListener(this);

        feedbackCorrectAns = new FeedbackDialog(this,R.layout.activity_quiz_popup_correctans);
        feedbackCorrectAns.findViewById(R.id.buttonNext).setOnClickListener(this);

        audioUtil = new AudioUtil(this);

        databaseLoader = DatabaseLoader.getInstance();
        databaseLoader.openWritable();

        onCreateHelper();
    }

    private void onCreateHelper() {

        allQuestions = databaseLoader.getAllQuestions(Long.valueOf(levelID));

        if(allQuestions.size()>0){

            totalQuestionNumber = allQuestions.size();
            currentQuestionNumber = 1;
            iterator = allQuestions.iterator();
            currentQuestion = (Question) iterator.next();
            drawUI();

        }else{

            showEndDialogOptions();
        }
    }

    private void showEndDialogOptions() {
        feedbackEnd.show();
    }

    private void checkAnswer(Option thisOption) {

        if(thisOption.checkAns()){

            feedbackCorrectAns.show();

            if (Utils.withSound(this)) {
                audioUtil.playSound(audioUtil.TRIVIA_RIGHT_ANSWER);
            }

        }else{

            Toast.makeText(this, "Wrong!", Toast.LENGTH_SHORT).show();

            if (Utils.withSound(this)) {
                audioUtil.playSound(audioUtil.TRIVIA_WRONG_ANSWER);
            }
        }
    }

    public void nextQuestion(){

        databaseLoader.checkQuestion(currentQuestion);

        feedbackCorrectAns.hide();

        if(iterator.hasNext()){

            currentQuestion = (Question) iterator.next();
            currentQuestionNumber++;
            drawUI();

        }else{

            showEndDialogOptions();
        }
    }

    public void resetQuestions(){

        feedbackEnd.hide();

        databaseLoader.resetQuestions();

        onCreateHelper();
    }

    public void goBack(){

        feedbackEnd.hide();

        Intent intent = new Intent(this, LevelsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("Module", moduleID);
        intent.putExtra("Category", categoryID);

        startActivity(intent);
    }

    private void drawUI() {

        if(!(currentQuestion==null)){

            LinearLayout questionLayout = (LinearLayout) findViewById(R.id.layoutQuestion);
            questionLayout.removeAllViews();

            for (ImageFile img : currentQuestion.getImages()) {

                ImageView image = new ImageView(this);

                image.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));

                image.setImageResource(getResources().getIdentifier(img.getName(), "drawable", this.getPackageName()));

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);

                lp.setMargins(5, 5, 5, 5);

                image.setLayoutParams(lp);

                questionLayout.addView(image);

                Log.d(QuizActivity.class.getName(), "Adding ImageView " + img.getName());
            }

            LinearLayout optionsLayout = (LinearLayout) findViewById(R.id.layoutOptions);

            optionsLayout.removeAllViews();

            for(Option option:currentQuestion.getOpts()){

                VerdanaButton optionButton = new VerdanaButton(this, null);

                optionButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                                            ViewGroup.LayoutParams.WRAP_CONTENT));

                optionButton.setText(option.getStr());

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

    @Override
    public void onResume(){

        super.onResume();

        databaseLoader.openWritable();
        audioUtil = new AudioUtil(this);

        Log.d(QuizActivity.class.getName(), "Loader Opened.");
    }

    @Override
    public void onPause(){

        super.onPause();

        databaseLoader.close();
        feedbackEnd.dismiss();
        feedbackCorrectAns.dismiss();
        audioUtil.unloadAll();

        Log.d(QuizActivity.class.getName(), "Loader Closed, FeedbackDialogs Dismissed.");
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.buttonReset:
                resetQuestions();
                break;

            case R.id.buttonGoBack:
                goBack();
                break;

            case R.id.buttonNext:
                nextQuestion();
                break;
        }
    }
}
