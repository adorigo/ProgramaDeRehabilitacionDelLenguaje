package ar.org.ineco.prl.ninios;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import ar.org.ineco.prl.ninios.classes.ImageFile;
import ar.org.ineco.prl.ninios.classes.Option;
import ar.org.ineco.prl.ninios.util.VerdanaTextView;

public class LocalizacionActivity extends BaseActivity {

    private int correctOpts;
    private List<Option> optsSelected;

    @Override
    protected int getStringResourceId() {
        return R.string.title_activity_localizacion;
    }

    @Override
    protected int getLayoutResourceId () {

        return R.layout.activity_localizacion;
    }


    @Override
    public void drawUI () {

        if (currentQuestion != null) {

            correctOpts = currentQuestion.getCorrectOpts();
            if (optsSelected != null) {
                optsSelected.clear();
            } else {
                optsSelected = new ArrayList<>();
            }

            Log.d(LocalizacionActivity.class.getName(), "PregId: " + currentQuestion.getId());

            int margin = getResources().getDimensionPixelSize(R.dimen.imgMargin);

            VerdanaTextView title = (VerdanaTextView) findViewById(R.id.textTitle);
            title.setText(currentQuestion.getText());

            ImageView sndQuestion = (ImageView) findViewById(R.id.audioQuestion);
            sndQuestion.setOnClickListener(null);

            LinearLayout optionsLayout1 = (LinearLayout) findViewById(R.id.layoutOptions1);
            optionsLayout1.removeAllViews();

            if (currentQuestion.getSound() != null) {

                final int sndId = loadSound(currentQuestion.getSound());
                sndQuestion.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        playSound(sndId);
                    }
                });
            } else {
                sndQuestion.setVisibility(View.GONE);
            }

            for (Option option : currentQuestion.getOpts()) {

                ImageView optionImg = new ImageView(this);

                ImageFile img = option.getImg();

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
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

                        checkOption(v);
                    }
                });

                optionsLayout1.addView(optionImg);

                Log.d(LocalizacionActivity.class.getName(), "Adding ImageView " + img.getName());
            }
        }
    }

    private void checkOption(View v) {

        Option opt = (Option) v.getTag();

        if (v.getAlpha() > 0.5f) {

            v.setAlpha(0.5f);
            optsSelected.add(opt);

        } else {
            v.setAlpha(1.0f);
            optsSelected.remove(opt);
        }

        if (optsSelected.size() == correctOpts) {
            boolean allCorrect = true;
            for (Option option : optsSelected) {
                allCorrect &= currentQuestion.makeTry(option);
            }

            if (allCorrect) {
                answerCorrect();
            } else {
                answerIncorrect();
            }
        }
    }

    @Override
    public void onPause () {

        super.onPause();

        Log.d(LocalizacionActivity.class.getName(), "Loader Closed, FeedbackDialogs Dismissed.");
    }
}
