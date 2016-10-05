package ar.org.ineco.prl.programaderehabilitaciondellenguaje;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.Category;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.Level;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.Menu;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.Option;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.Question;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.database.DatabaseLoader;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.util.CategoryAdapter;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.util.VerdanaEditText;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.util.VerdanaTextView;

public class TaskCreatorActivity extends Activity {

    private Menu menu = Menu.getInstance();
    private DatabaseLoader dbLoader = DatabaseLoader.getInstance();
    private Level selectedLevel;

    @Override
    protected void onCreate (Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        chooseView();
    }

    private void chooseView() {

        if (selectedLevel != null) {
            showForm();
        } else {
            fillCategoriesAvailable();
        }
    }

    private void fillCategoriesAvailable() {

        setContentView(R.layout.activity_taskcreator);

        VerdanaTextView textTitle = (VerdanaTextView) findViewById(R.id.textTitle);
        textTitle.setText(getResources().getString(R.string.title_activity_taskcreator));

        ProgressBar vProgress = (ProgressBar) findViewById(R.id.progressBar);
        vProgress.setVisibility(View.INVISIBLE);

        ImageView helpSndLayout = (ImageView) findViewById(R.id.audioAyuda);
        helpSndLayout.setVisibility(View.INVISIBLE);

        ArrayList<Category> listCategories = new ArrayList<>();
        for (Category leaf : menu.getTopCategory().getAllLeafs()) {
            if (leaf.getId() == 3) {
                listCategories.add(leaf);
            }
        }

        CategoryAdapter adapter = new CategoryAdapter(this, listCategories);

        ExpandableListView listview = (ExpandableListView) findViewById(R.id.categories);
        listview.setAdapter(adapter);

        listview.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                selectLevel((Level) v.getTag());
                return true;
            }
        });

    }

    public void selectLevel (Level lvl) {

        selectedLevel = lvl;

        showForm();
    }

    private void showForm() {

        setContentView(R.layout.activity_taskcreator_verbal);

        VerdanaTextView textTitle = (VerdanaTextView) findViewById(R.id.textTitle);
        textTitle.setText(getResources().getString(R.string.title_activity_taskcreator));

        ProgressBar vProgress = (ProgressBar) findViewById(R.id.progressBar);
        vProgress.setVisibility(View.INVISIBLE);

        ImageView helpSndLayout = (ImageView) findViewById(R.id.audioAyuda);
        helpSndLayout.setVisibility(View.INVISIBLE);
    }

    public void goBack (View v) {

        selectedLevel = null;

        chooseView();
    }

    public void saveTask(View v) {

        VerdanaEditText vQuestion = (VerdanaEditText) findViewById(R.id.inputQuestion);
        VerdanaEditText vInputOptionCorrect = (VerdanaEditText) findViewById(R.id.inputOptionCorrect);
        VerdanaEditText vInputOption2 = (VerdanaEditText) findViewById(R.id.inputOption2);
        VerdanaEditText vInputOption3 = (VerdanaEditText) findViewById(R.id.inputOption3);

        if (vQuestion.getText().length() == 0 || vInputOptionCorrect.getText().length() == 0 ||
                vInputOption2.getText().length() == 0 || vInputOption3.getText().length() == 0) {
            Toast.makeText(this, R.string.errorMissingInput, Toast.LENGTH_LONG).show();
            return;
        }

        Question newQuestion = new Question(0, vQuestion.getText().toString(), 0, 0, null);

        Option optionCorrect = new Option(0, vInputOptionCorrect.getText().toString(), 1, 0, null, null);
        Option option2 = new Option(0, vInputOption2.getText().toString(), 0, 0, null, null);
        Option option3 = new Option(0, vInputOption3.getText().toString(), 0, 0, null, null);

        newQuestion.addOption(optionCorrect);
        newQuestion.addOption(option2);
        newQuestion.addOption(option3);

        DatabaseLoader.getInstance().createTaks(newQuestion, selectedLevel);

        vQuestion.getText().clear();
        vInputOptionCorrect.getText().clear();
        vInputOption2.getText().clear();
        vInputOption3.getText().clear();

        if (selectedLevel.isDone()) {

            selectedLevel.resetLevel();
        }

        Toast.makeText(this, R.string.taskCreated, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(this, SettingsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
