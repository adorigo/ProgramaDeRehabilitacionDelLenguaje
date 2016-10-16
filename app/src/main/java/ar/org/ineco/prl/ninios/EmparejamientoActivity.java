package ar.org.ineco.prl.ninios;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import ar.org.ineco.prl.ninios.classes.ImageFile;
import ar.org.ineco.prl.ninios.classes.Option;
import ar.org.ineco.prl.ninios.util.VerdanaTextView;

public class EmparejamientoActivity extends BaseActivity {

    @Override
    protected int getStringResourceId() {
        return R.string.title_activity_verdaderofalso;
    }

    @Override
    protected int getLayoutResourceId () {

        return R.layout.activity_emparejamiento;
    }


    @Override
    public void drawUI () {

        if (currentQuestion != null) {

            VerdanaTextView title = (VerdanaTextView) findViewById(R.id.textTitle);
            title.setText("");

            LinearLayout questionLayout = (LinearLayout) findViewById(R.id.layoutQuestion);
            questionLayout.removeAllViews();

            LinearLayout optionsLayout = (LinearLayout) findViewById(R.id.layoutOptions);
            optionsLayout.removeAllViews();

            if (currentQuestion.getImages().size() == 0) {
                nextQuestion();
            }

            title.setText(currentQuestion.getText());

            int margin = 5;

            for (ImageFile img : currentQuestion.getImages()) {

                ImageView image = new ImageView(this);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
                layoutParams.setMargins(margin, margin, margin, margin);

                image.setLayoutParams(layoutParams);
                image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                image.setAdjustViewBounds(true);

                image.setImageResource(getResources().getIdentifier(img.getName(), "drawable", this.getPackageName()));

                if (img.getSnd() != null) {

                    final int idSound = loadSound(img.getSnd());

                    image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            playSound(idSound);
                        }
                    });
                }

                questionLayout.addView(image);

                Log.d(PragmaticaActivity.class.getName(), "Adding ImageView " + img.getName());
            }

            for (Option option : currentQuestion.getOpts()) {

                ImageFile imgOption = option.getImg();

                ImageView optionImg = new ImageView(this);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
                layoutParams.setMargins(margin, margin, margin, margin);

                optionImg.setLayoutParams(layoutParams);
                optionImg.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                optionImg.setAdjustViewBounds(true);

                optionImg.setImageResource(getResources().getIdentifier(imgOption.getName(), "drawable", this.getPackageName()));

                optionImg.setTag(option);

                optionImg.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        Option optSelected = (Option) v.getTag();
                        checkAnswer(optSelected);
                    }
                });

                optionsLayout.addView(optionImg);

                Log.d(ComprensionActivity.class.getName(), "Adding ImageView " + imgOption.getName());
            }
        }
    }

    @Override
    public void onPause () {

        super.onPause();

        Log.d(EmparejamientoActivity.class.getName(), "Loader Closed, FeedbackDialogs Dismissed.");
    }
}
