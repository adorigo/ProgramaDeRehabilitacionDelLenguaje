package ar.org.ineco.prl.ninios;

import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import ar.org.ineco.prl.ninios.classes.ImageFile;
import ar.org.ineco.prl.ninios.classes.Option;
import ar.org.ineco.prl.ninios.util.VerdanaButton;
import ar.org.ineco.prl.ninios.util.VerdanaTextView;

public class IdentificacionActivity extends BaseActivity {

    @Override
    protected int getStringResourceId() {
        return R.string.title_activity_identificacion;
    }

    @Override
    protected int getLayoutResourceId () {

        return R.layout.activity_identificacion;
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

                VerdanaTextView text = new VerdanaTextView(this, null);

                text.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));

                text.setText(img.getTxtImg());
                text.setHeight((int) size);
                text.setGravity(Gravity.CENTER);
                text.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_large));

                questionLayout.addView(text);

                Log.d(IdentificacionActivity.class.getName(), "Adding TextView " + img.getTxtImg());
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

                    Log.d(IdentificacionActivity.class.getName(), "Adding ImageView " + img.getName());
                }
            }
        }
    }

    @Override
    public void onPause () {

        super.onPause();

        Log.d(IdentificacionActivity.class.getName(), "Loader Closed, FeedbackDialogs Dismissed.");
    }
}
