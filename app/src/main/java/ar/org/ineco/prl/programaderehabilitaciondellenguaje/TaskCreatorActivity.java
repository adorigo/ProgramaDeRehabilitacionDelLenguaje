package ar.org.ineco.prl.programaderehabilitaciondellenguaje;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.Category;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.Level;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.Menu;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.database.DatabaseLoader;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.util.CategoryAdapter;

public class TaskCreatorActivity extends Activity {

    private Category selectedCategory;
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
        } else if (selectedCategory != null) {
            fillLevelsAvailable();
        } else {
            fillCategoriesAvailable();
        }
    }

    private void fillCategoriesAvailable() {

        setContentView(R.layout.activity_taskcreator);

        ArrayList<Category> listCategories = new ArrayList<>();
        for (Category leaf : menu.getTopCategory().getAllLeafs()) {
            listCategories.add(leaf);
        }

        CategoryAdapter adapter = new CategoryAdapter(this, listCategories);

        ExpandableListView listview = (ExpandableListView) findViewById(R.id.categories);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectCategory((Category) view.getTag());
            }
        });

    }

    public void selectCategory (Category cat) {

        selectedCategory = cat;

        fillLevelsAvailable();
    }

    private void fillLevelsAvailable() {

        setContentView(R.layout.activity_taskcreator);
    }

    public void selectLevel (Level lvl) {

        selectedLevel = lvl;

        showForm();
    }

    private void showForm() {

        setContentView(R.layout.activity_taskcreator);
    }

    public void goBack (View v) {

        Intent intent = new Intent(this, MainMenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
