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

public class SemanticaActivity extends BaseActivity {


    @Override
    protected int getLayoutResourceId () {

        return R.layout.activity_semantica;
    }

    @Override
    public void drawUI() {

        if (super.currentQuestion != null) {

            VerdanaTextView title = (VerdanaTextView) findViewById(R.id.textTitle);
            title.setText(currentQuestion.getText());

            LinearLayout questionLayout, optionsLayout;

            questionLayout = (LinearLayout) findViewById(R.id.layoutQuestion);
            questionLayout.removeAllViews();

            optionsLayout = (LinearLayout) findViewById(R.id.layoutOptions);
            optionsLayout.removeAllViews();

            if (currentQuestion.getImages().size() > 0) {

                int margin = getResources().getDimensionPixelSize(R.dimen.imgMargin);
                double maxwidth = getResources().getDisplayMetrics().widthPixels / currentQuestion.getImages().size();
                double size = getResources().getDisplayMetrics().heightPixels * 0.45;

                for (ImageFile img : currentQuestion.getImages()) {

                    if (img.getName() != null && !img.getName().isEmpty()) {

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
                    public void onClick (View v) {

                        Option optSelected = (Option) v.getTag();
                        checkAnswer(optSelected);
                    }
                });

                optionsLayout.addView(optionButton);
            }
        }
    }

    @Override
    public void onPause () {

        super.onPause();

        Log.d(SemanticaActivity.class.getName(), "Loader Closed, FeedbackDialogs Dismissed.");
    }
}
