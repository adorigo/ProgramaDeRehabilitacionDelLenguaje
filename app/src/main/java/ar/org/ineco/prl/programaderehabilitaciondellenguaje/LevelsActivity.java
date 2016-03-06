package ar.org.ineco.prl.programaderehabilitaciondellenguaje;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.ApplicationContext;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.Level;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.Menu;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.util.VerdanaButton;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.util.VerdanaTextView;

public class LevelsActivity extends Activity {

    private Menu menu = ApplicationContext.getMenu();

    @Override
    protected void onCreate (Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels);

        onCreateHelper();
    }

    @SuppressWarnings("deprecation")
    private void onCreateHelper () {

        List<Level> levels = menu.getLevels();

        TextView categoryText = (TextView) findViewById(R.id.textCategoryTitle);
        categoryText.setText(menu.getLabel());

        LinearLayout levelsLayout = (LinearLayout) findViewById(R.id.layoutLevels);
        levelsLayout.removeAllViews();

        for (Level lvl : levels) {

            VerdanaButton lvlButton = new VerdanaButton(this, null);

            lvlButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            lvlButton.setText(lvl.getLvlName());

            if (lvl.isDone()) {

                int sdkVersion = Build.VERSION.SDK_INT;

                if (sdkVersion < Build.VERSION_CODES.M) {
                    lvlButton.setBackgroundColor(getResources().getColor(R.color.level_complete));
                } else {
                    lvlButton.setBackgroundColor(ContextCompat.getColor(this, R.color.level_complete));
                }


            } else {

                lvlButton.setTag(lvl);

                lvlButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick (View v) {

                        startLevel(v.getTag());
                    }
                });
            }
            levelsLayout.addView(lvlButton);
            Log.d(LevelsActivity.class.getName(), "Adding " + lvl.getLvlName());
        }

        if (levels.size() == 0) {

            VerdanaTextView noLevels = new VerdanaTextView(this, null);
            noLevels.setText(R.string.textNoLevels);

            levelsLayout.addView(noLevels);
        }
    }

    private void startLevel (Object thisLevel) {

        Level lvl = (Level) thisLevel;
        menu.setCurrentLevel(lvl);

        Class<?> nextActivity;

        try {

            nextActivity = Class.forName(menu.getActivityNameForLevel());

        } catch (ClassNotFoundException ex) {

            nextActivity = LevelsActivity.class;
        }

        Log.d(LevelsActivity.class.getName(), "This class for lvl: " + nextActivity);

        Intent intent = new Intent(this, nextActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
    }

    public void goBack (View v) {

        menu.goUp();

        Intent intent = new Intent(this, CategoriesActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
    }
}
