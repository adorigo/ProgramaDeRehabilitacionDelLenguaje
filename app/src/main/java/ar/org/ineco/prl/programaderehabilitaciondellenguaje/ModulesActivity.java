package ar.org.ineco.prl.programaderehabilitaciondellenguaje;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import ar.org.ineco.prl.programaderehabilitaciondellenguaje.database.DatabaseLoader;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.Module;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.util.VerdanaButton;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.util.VerdanaTextView;

import java.util.List;

public class ModulesActivity extends Activity {
    private DatabaseLoader databaseLoader;
    private List<Module> allModules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modules);

        databaseLoader = new DatabaseLoader(this);
        databaseLoader.openReadable();

        onCreateHelper();
    }

    private void onCreateHelper() {
        allModules = databaseLoader.getAllModules();
        LinearLayout modulesLayout = (LinearLayout) findViewById(R.id.layoutModules);
        modulesLayout.removeAllViews();
        for (Module mod : allModules) {
            VerdanaButton modButton = new VerdanaButton(this, null);
            modButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            modButton.setText(mod.getModName());
            modButton.setTag(mod);
            modButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startModule(v.getTag());
                }
            });

            modulesLayout.addView(modButton);
            Log.d(ModulesActivity.class.getName(), "Adding " + mod.getModName());
        }

        if (allModules.size() == 0) {
            VerdanaTextView noModules = new VerdanaTextView(this, null);
            noModules.setText(R.string.textNoModules);
            modulesLayout.addView(noModules);
        }
        databaseLoader.close();
    }

    private void startModule(Object thisCategory) {
        Module mod = (Module) thisCategory;
        Intent intent = new Intent(this, CategoriesActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("Module", String.valueOf(mod.getModNumber()));
        startActivity(intent);
    }

    public void goBack(View v) {
        Intent intent = new Intent(this, MainMenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
