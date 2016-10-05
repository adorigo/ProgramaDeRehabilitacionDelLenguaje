package ar.org.ineco.prl.programaderehabilitaciondellenguaje;

import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.Option;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.util.DragNDrop.LongClickListener;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.util.Utils;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.util.VerdanaButton;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.util.VerdanaTextView;

public class IntrusoActivity extends BaseActivity {

    @Override
    protected int getStringResourceId() {
        return R.string.title_activity_intruso;
    }

    @Override
    protected int getLayoutResourceId () {

        return R.layout.activity_intruso;
    }


    @Override
    public void drawUI () {

        if (currentQuestion != null) {

            Log.d(IntrusoActivity.class.getName(), "PregId: " + currentQuestion.getId());

            int margin = getResources().getDimensionPixelSize(R.dimen.imgMargin);
            double maxwidth = getResources().getDisplayMetrics().widthPixels / currentQuestion.getOpts().size();
            double width = getResources().getDisplayMetrics().widthPixels * 0.20;
            double size = getResources().getDisplayMetrics().heightPixels * 0.35;
            double height = getResources().getDisplayMetrics().heightPixels * 0.20;

            VerdanaTextView title = (VerdanaTextView) findViewById(R.id.textTitle);
            title.setText(currentQuestion.getText());

            LinearLayout optionsLayout1 = (LinearLayout) findViewById(R.id.layoutOptions1);
            optionsLayout1.removeAllViews();

            LinearLayout optionsLayout2 = (LinearLayout) findViewById(R.id.layoutOptions2);
            optionsLayout2.removeAllViews();

            ImageView dropLayout = (ImageView) findViewById(R.id.dropArea);
            //dropLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, (int) height));
            dropLayout.setScaleType(ImageView.ScaleType.FIT_XY);
            dropLayout.setAdjustViewBounds(true);
            if (Utils.withDragNDrop(this)) {

                dropLayout.setOnDragListener(new View.OnDragListener() {

                    @Override
                    public boolean onDrag (View v, DragEvent event) {

                        int dragEvent = event.getAction();
                        ImageView bin = (ImageView) v;

                        switch (dragEvent) {
                            case DragEvent.ACTION_DRAG_EXITED:

                                bin.setImageResource(R.drawable.closedbin);
                                break;
                            case DragEvent.ACTION_DRAG_ENTERED:

                                bin.setImageResource(R.drawable.openedbin);
                                break;
                            case DragEvent.ACTION_DROP:

                                View view = (View) event.getLocalState();
                                Option option = (Option) view.getTag();
                                bin.setImageResource(R.drawable.closedbin);

                                checkAnswer(option);
                                break;
                        }

                        return true;
                    }
                });

            } else {
                dropLayout.setVisibility(ImageView.GONE);
            }

            for (Option option : currentQuestion.getOpts()) {

                if (option.getImg() != null) {

                    ImageView image = new ImageView(this);

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, (int) size);
                    layoutParams.setMargins(margin, margin, margin, margin);

                    image.setLayoutParams(layoutParams);
                    image.setMaxWidth((int) maxwidth);
                    image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    image.setAdjustViewBounds(true);

                    image.setTag(option);

                    if (Utils.withDragNDrop(this)) {
                        image.setOnLongClickListener(new LongClickListener());
                    } else {
                        image.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick (View v) {

                                Option optSelected = (Option) v.getTag();
                                checkAnswer(optSelected);
                            }
                        });
                    }

                    image.setImageResource(getResources().getIdentifier(option.getImg().getName(), "drawable", this.getPackageName()));

                    optionsLayout1.addView(image);

                } else {

                    VerdanaButton button = new VerdanaButton(this, null);

                    button.setText(option.getStr());
                    button.setWidth((int) width);
                    button.setHeight((int) size);

                    button.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));

                    button.setGravity(Gravity.CENTER);

                    button.setTag(option);

                    if (Utils.withDragNDrop(this)) {
                        button.setOnLongClickListener(new LongClickListener());
                    } else {
                        button.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick (View v) {

                                Option optSelected = (Option) v.getTag();
                                checkAnswer(optSelected);
                            }
                        });
                    }

                    optionsLayout1.addView(button);
                }
            }
        }
    }

    @Override
    public void onPause () {

        super.onPause();

        Log.d(IntrusoActivity.class.getName(), "Loader Closed, FeedbackDialogs Dismissed.");
    }
}
