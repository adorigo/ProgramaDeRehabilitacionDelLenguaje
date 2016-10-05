package ar.org.ineco.prl.programaderehabilitaciondellenguaje;

import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.ImageFile;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.Option;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.util.VerdanaButton;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.util.VerdanaTextView;

public class VerdaderoFalsoActivity extends BaseActivity {

    @Override
    protected int getStringResourceId() {
        return R.string.title_activity_verdaderofalso;
    }

    @Override
    protected int getLayoutResourceId () {

        return R.layout.activity_verdaderofalso;
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

            ImageFile img = currentQuestion.getImages().get(0);

            VerdanaTextView vText = new VerdanaTextView(this, null);

            vText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            vText.setText(img.getTxtImg());
            vText.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_large));

            questionLayout.addView(vText);

            for (Option option : currentQuestion.getOpts()) {

                VerdanaButton vButton = new VerdanaButton(this, null);

                vButton.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
                vButton.setText(option.getStr());
                vButton.setTag(option);
                vButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_large));

                vButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick (View v) {

                        Option optSelected = (Option) v.getTag();

                        checkAnswer(optSelected);
                    }
                });

                optionsLayout.addView(vButton);
            }
        }
    }

    @Override
    public void onPause () {

        super.onPause();

        Log.d(VerdaderoFalsoActivity.class.getName(), "Loader Closed, FeedbackDialogs Dismissed.");
    }
}
