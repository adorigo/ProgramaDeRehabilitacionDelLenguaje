package ar.org.ineco.prl.programaderehabilitaciondellenguaje;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import ar.org.ineco.prl.programaderehabilitaciondellenguaje.database.DatabaseLoader;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.Level;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.util.VerdanaButton;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.util.VerdanaTextView;

import java.util.List;

public class LevelsActivity extends Activity {
    private DatabaseLoader databaseLoader;
    private String categoryID;
    private String moduleID;
    private List<Level> allLevels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels);

        moduleID = getIntent().getStringExtra("Module");
        categoryID = getIntent().getStringExtra("Category");

        databaseLoader = new DatabaseLoader(this);
        databaseLoader.openReadable();

        onCreateHelper();
    }

    private void onCreateHelper() {
        allLevels = databaseLoader.getAllLevels(categoryID);
        TextView categoryText = (TextView) findViewById(R.id.textCategoryTitle);
        categoryText.setText(databaseLoader.getCategoryName(categoryID));
        LinearLayout levelsLayout = (LinearLayout) findViewById(R.id.layoutLevels);
        levelsLayout.removeAllViews();
        for (Level lvl : allLevels) {
            VerdanaButton lvlButton = new VerdanaButton(this, null);
            lvlButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            lvlButton.setText(lvl.getLvlName());
            lvlButton.setTag(lvl);
            lvlButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startLevel(v.getTag());
                }
            });

            levelsLayout.addView(lvlButton);
            Log.d(LevelsActivity.class.getName(), "Adding " + lvl.getLvlName());
        }

        if (allLevels.size() == 0) {
            VerdanaTextView noLevels = new VerdanaTextView(this, null);
            noLevels.setText(R.string.textNoLevels);
            levelsLayout.addView(noLevels);
        }
        databaseLoader.close();
    }

    private void startLevel(Object thisLevel) {
        Level lvl = (Level) thisLevel;
        Intent intent = new Intent(this, QuizActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("Module", moduleID);
        intent.putExtra("Category", categoryID);
        intent.putExtra("Level", String.valueOf(lvl.getLvlNumber()));
        startActivity(intent);
    }

    public void goBack(View v) {
        Intent intent = new Intent(this, CategoriesActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("Module", moduleID);
        intent.putExtra("Category", categoryID);
        startActivity(intent);
    }
}
