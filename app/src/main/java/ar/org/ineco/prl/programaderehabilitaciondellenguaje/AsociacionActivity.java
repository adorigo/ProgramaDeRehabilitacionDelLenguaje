package ar.org.ineco.prl.programaderehabilitaciondellenguaje;

import android.os.Bundle;
import android.util.DisplayMetrics;
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

public class AsociacionActivity extends BaseActivity {

    @Override
    protected int getLayoutResourceId () {

        return R.layout.activity_asociacion;
    }

    @Override
    public void drawUI () {

        if (super.currentQuestion != null) {

            VerdanaTextView title = (VerdanaTextView) findViewById(R.id.textTitle);
            title.setText(currentQuestion.getText());

            LinearLayout questionLayout, optionsLayout;

            questionLayout = (LinearLayout) findViewById(R.id.layoutQuestion);
            questionLayout.removeAllViews();

            optionsLayout = (LinearLayout) findViewById(R.id.layoutOptions);
            optionsLayout.removeAllViews();

            int margin = getResources().getDimensionPixelSize(R.dimen.imgMargin);
            double size = getResources().getDisplayMetrics().heightPixels * 0.45;

            if (currentQuestion.getImages().size() > 0) {

                ImageFile img = currentQuestion.getImages().get(0);

                if (img.getName() != null && !img.getName().isEmpty()) {

                    ImageView image = new ImageView(this);

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            (int) size);
                    layoutParams.setMargins(margin, margin, margin, margin);

                    image.setLayoutParams(layoutParams);
                    image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    image.setAdjustViewBounds(true);

                    image.setImageResource(getResources().getIdentifier(img.getName(), "drawable", this.getPackageName()));

                    questionLayout.addView(image);

                    Log.d(AsociacionActivity.class.getName(), "Adding ImageView " + img.getName());

                } else {

                    VerdanaTextView text = new VerdanaTextView(this, null);

                    text.setText(img.getTxtImg());
                    text.setHeight((int) size);
                    text.setGravity(Gravity.CENTER);
                    text.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_large));

                    questionLayout.addView(text);

                    Log.d(AsociacionActivity.class.getName(), "Adding TextView " + img.getTxtImg());
                }
            }

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
                        public void onClick (View v) {

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
                        public void onClick (View v) {

                            Option optSelected = (Option) v.getTag();
                            checkAnswer(optSelected);
                        }
                    });

                    optionsLayout.addView(optionImg);

                    Log.d(AsociacionActivity.class.getName(), "Adding ImageView " + img.getName());
                }
            }
        }
    }

    @Override
    public void onPause () {

        super.onPause();

        Log.d(AsociacionActivity.class.getName(), "Loader Closed, FeedbackDialogs Dismissed.");
    }
}
