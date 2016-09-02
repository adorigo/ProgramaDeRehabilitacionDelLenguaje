package ar.org.ineco.prl.programaderehabilitaciondellenguaje;

import android.os.Bundle;
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

public class DenominacionActivity extends BaseActivity {

    @Override
    public void onCreate (Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResourceId () {

        return R.layout.activity_denominacion;
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

            LinearLayout yesNoLayout = (LinearLayout) findViewById(R.id.layoutYesNo);
            yesNoLayout.removeAllViews();

            if (currentQuestion.getImages().size() == 0) {
                nextQuestion();
            }

            title.setText(currentQuestion.getText());

            ImageFile img = currentQuestion.getImages().get(0);

            if (!img.getName().isEmpty()) {

                int margin = getResources().getDimensionPixelSize(R.dimen.imgMargin);
                double size = getResources().getDisplayMetrics().heightPixels * 0.65;

                ImageView image = new ImageView(this);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, (int) size);
                layoutParams.setMargins(margin, margin, margin, margin);

                image.setLayoutParams(layoutParams);
                image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                image.setAdjustViewBounds(true);

                if (currentQuestion.getSound() != null) {

                    final int sndId = loadSound(currentQuestion.getSound());
                    image.setOnLongClickListener(new View.OnLongClickListener() {

                        @Override
                        public boolean onLongClick(View v) {

                            playSound(sndId);
                            return false;
                        }
                    });
                }

                image.setImageResource(getResources().getIdentifier(img.getName(), "drawable", this.getPackageName()));

                questionLayout.addView(image);

                Log.d(DenominacionActivity.class.getName(), "Adding ImageView " + img.getName());

            } else {

                VerdanaTextView vText = new VerdanaTextView(this, null);

                vText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                vText.setText(img.getTxtImg());
                vText.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_large));

                if (currentQuestion.getSound() != null) {

                    final int sndId = loadSound(currentQuestion.getSound());
                    vText.setOnLongClickListener(new View.OnLongClickListener() {

                        @Override
                        public boolean onLongClick(View v) {

                            playSound(sndId);
                            return false;
                        }
                    });
                }

                questionLayout.addView(vText);
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
        }
    }

    private void showOptions () {

        final Option option = currentQuestion.getOpts().get(0);

        LinearLayout questionLayout = (LinearLayout) findViewById(R.id.layoutQuestion);
        questionLayout.removeAllViews();

        LinearLayout nextButtonLayout = (LinearLayout) findViewById(R.id.layoutNextButton);
        nextButtonLayout.removeAllViews();

        LinearLayout optionsLayout = (LinearLayout) findViewById(R.id.layoutOptions);
        optionsLayout.removeAllViews();

        LinearLayout yesNoLayout = (LinearLayout) findViewById(R.id.layoutYesNo);
        yesNoLayout.removeAllViews();

        if (option.getImg() != null) {

            int margin = getResources().getDimensionPixelSize(R.dimen.imgMargin);
            double size = getResources().getDisplayMetrics().heightPixels * 0.65;

            ImageFile img = option.getImg();

            ImageView image = new ImageView(this);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, (int) size);
            layoutParams.setMargins(margin, margin, margin, margin);

            image.setLayoutParams(layoutParams);
            image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            image.setAdjustViewBounds(true);

            if (option.getSnd() != null) {

                final int sndId = loadSound(option.getSnd());
                image.setOnLongClickListener(new View.OnLongClickListener() {

                    @Override
                    public boolean onLongClick(View v) {

                        playSound(sndId);
                        return false;
                    }
                });
            }

            image.setImageResource(getResources().getIdentifier(img.getName(), "drawable", this.getPackageName()));

            optionsLayout.addView(image);

        } else {

            VerdanaTextView vText = new VerdanaTextView(this, null);

            vText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            vText.setText(option.getStr());
            vText.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_large));

            if (option.getSnd() != null) {

                final int sndId = loadSound(option.getSnd());
                vText.setOnLongClickListener(new View.OnLongClickListener() {

                    @Override
                    public boolean onLongClick(View v) {

                        playSound(sndId);
                        return false;
                    }
                });
            }

            optionsLayout.addView(vText);

            VerdanaButton yesButton = new VerdanaButton(this, null);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);

            yesButton.setLayoutParams(params);

            yesButton.setText(getResources().getString(R.string.buttonYes));
            yesButton.setGravity(Gravity.CENTER_HORIZONTAL);

            yesButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    checkAnswer(option);
                }
            });

            VerdanaButton noButton = new VerdanaButton(this, null);

            noButton.setLayoutParams(params);

            noButton.setText(getResources().getString(R.string.buttonNo));
            noButton.setGravity(Gravity.CENTER_HORIZONTAL);

            noButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    nextQuestion();
                }
            });

            yesNoLayout.addView(yesButton);
            yesNoLayout.addView(noButton);
        }
    }

    @Override
    public void onPause () {

        super.onPause();

        Log.d(DenominacionActivity.class.getName(), "Loader Closed, FeedbackDialogs Dismissed.");
    }
}
