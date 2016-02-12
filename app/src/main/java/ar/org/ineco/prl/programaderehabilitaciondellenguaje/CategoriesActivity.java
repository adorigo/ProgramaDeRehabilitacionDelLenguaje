package ar.org.ineco.prl.programaderehabilitaciondellenguaje;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.Category;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.database.DatabaseLoader;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.util.VerdanaButton;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.util.VerdanaTextView;

import java.util.List;


public class CategoriesActivity extends Activity {
    private DatabaseLoader databaseLoader;
    private String moduleID;
    private List<Category> allCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        moduleID = getIntent().getStringExtra("Module");

        databaseLoader = new DatabaseLoader(this);
        databaseLoader.openReadable();

        onCreateHelper();
    }

    private void onCreateHelper() {
        allCategories = databaseLoader.getAllCategories(moduleID);
        TextView moduleText = (TextView) findViewById(R.id.textModuleTitle);
        moduleText.setText(databaseLoader.getModuleName(moduleID));
        LinearLayout categoriesLayout = (LinearLayout) findViewById(R.id.layoutCategories);
        categoriesLayout.removeAllViews();
        for (Category cat : allCategories) {
            VerdanaButton catButton = new VerdanaButton(this, null);
            catButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            catButton.setText(cat.getCatName());
            catButton.setTag(cat);
            catButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startCategory(v.getTag());
                }
            });

            categoriesLayout.addView(catButton);
            Log.d(CategoriesActivity.class.getName(), "Adding " + cat.getCatName());
        }

        if (allCategories.size() == 0) {
            VerdanaTextView noCategories = new VerdanaTextView(this, null);
            noCategories.setText(R.string.textNoCategories);
            categoriesLayout.addView(noCategories);
        }
        databaseLoader.close();
    }

    private void startCategory(Object thisCategory) {
        Category cat = (Category) thisCategory;
        Intent intent = new Intent(this, LevelsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("Module", moduleID);
        intent.putExtra("Category", String.valueOf(cat.getCatNumber()));
        startActivity(intent);
    }

    public void goBack(View v) {
        Intent intent = new Intent(this, ModulesActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("Module", moduleID);
        startActivity(intent);
    }
}
