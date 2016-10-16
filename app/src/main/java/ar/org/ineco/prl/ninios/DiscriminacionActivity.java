package ar.org.ineco.prl.ninios;

import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import ar.org.ineco.prl.ninios.classes.ImageFile;
import ar.org.ineco.prl.ninios.classes.Option;
import ar.org.ineco.prl.ninios.util.VerdanaButton;
import ar.org.ineco.prl.ninios.util.VerdanaTextView;

public class DiscriminacionActivity extends BaseActivity {

    @Override
    protected int getStringResourceId() {
        return R.string.title_activity_discriminacion;
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_discriminacion;
    }

    @Override
    public void drawUI() {

        if (currentQuestion != null) {

            VerdanaTextView title = (VerdanaTextView) findViewById(R.id.textTitle);
            title.setText(currentQuestion.getText());

            ImageView sndQuestion = (ImageView) findViewById(R.id.audioQuestion);
            sndQuestion.setOnClickListener(null);

            LinearLayout optionsLayout = (LinearLayout) findViewById(R.id.layoutOptions);
            optionsLayout.removeAllViews();

            if (currentQuestion.getNOpts() == 0) {
                super.nextQuestion();
            }


            if (currentQuestion.getSound() != null) {

                final int sndId = loadSound(currentQuestion.getSound());
                sndQuestion.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        playSound(sndId);
                    }
                });
            }

            int margin = 5;
            for (Option option : currentQuestion.getOpts()) {

                if (option.getImg() == null) {

                    VerdanaButton optionButton = new VerdanaButton(this, null);

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
                    optionButton.setLayoutParams(layoutParams);

                    optionButton.setText(option.getStr());
                    optionButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_large));
                    optionButton.setGravity(Gravity.CENTER);

                    optionButton.setTag(option);

                    optionButton.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            Option optSelected = (Option) v.getTag();
                            checkAnswer(optSelected);
                        }
                    });

                    optionsLayout.addView(optionButton);

                } else {

                    ImageFile img = option.getImg();
                    ImageView optionImg = new ImageView(this);

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
                    layoutParams.setMargins(margin, margin, margin, margin);

                    optionImg.setLayoutParams(layoutParams);
                    optionImg.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    optionImg.setAdjustViewBounds(true);

                    optionImg.setImageResource(getResources().getIdentifier(img.getName(), "drawable", this.getPackageName()));

                    optionImg.setTag(option);

                    optionImg.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            Option optSelected = (Option) v.getTag();
                            checkAnswer(optSelected);
                        }
                    });

                    optionsLayout.addView(optionImg);

                    Log.d(DiscriminacionActivity.class.getName(), "Adding ImageView " + img.getName());
                }
            }
        }
    }

    @Override
    public void onPause() {

        super.onPause();

        Log.d(DiscriminacionActivity.class.getName(), "Loader Closed, FeedbackDialogs Dismissed.");
    }
}
