package ar.org.ineco.prl.programaderehabilitaciondellenguaje;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.Category;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.Menu;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.util.VerdanaButton;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.util.VerdanaTextView;


public class CategoriesActivity extends Activity {

    private Menu menu = Menu.getInstance();

    @Override
    protected void onCreate (Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        onCreateHelper();
    }

    private void onCreateHelper () {

        List<Category> Categories = menu.getCategories();

        TextView moduleText = (TextView) findViewById(R.id.textModuleTitle);
        moduleText.setText(menu.getLabel());

        LinearLayout categoriesLayout = (LinearLayout) findViewById(R.id.layoutCategories);
        categoriesLayout.removeAllViews();

        for (Category cat : Categories) {
            VerdanaButton catButton = new VerdanaButton(this, null);
            catButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            catButton.setText(cat.getCatName());
            catButton.setTag(cat);
            catButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick (View v) {

                    startCategory(v.getTag());
                }
            });

            categoriesLayout.addView(catButton);
        }

        if (Categories.size() == 0) {
            VerdanaTextView noCategories = new VerdanaTextView(this, null);
            noCategories.setText(R.string.textNoCategories);
            categoriesLayout.addView(noCategories);
        }

    }

    private void startCategory (Object thisCategory) {

        Category cat = (Category) thisCategory;

        menu.setCurrentCategory(cat);

        if (menu.canGoLower()) {

            onCreateHelper();

        } else {

            Intent intent = new Intent(this, LevelsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            //intent.putExtra("Category", String.valueOf(cat.getCatNumber()));

            startActivity(intent);
        }
    }

    public void goBack (View v) {

        if (menu.canGoUp()) {

            menu.goUp();

            onCreateHelper();

        } else {

            Intent intent = new Intent(this, MainMenuActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);
        }
    }
}
