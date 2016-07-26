package ar.org.ineco.prl.programaderehabilitaciondellenguaje;

import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.ImageFile;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.Option;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.util.VerdanaButton;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.util.VerdanaTextView;

public class PragmaticaActivity extends BaseActivity {

    @Override
    public void loadActivityLayout() {
        setContentView(R.layout.activity_pragmatica);
    }

    @Override
    public void drawUI () {

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

    @Override
    public void onPause () {

        super.onPause();

        Log.d(PragmaticaActivity.class.getName(), "Loader Closed, FeedbackDialogs Dismissed.");
    }
}
