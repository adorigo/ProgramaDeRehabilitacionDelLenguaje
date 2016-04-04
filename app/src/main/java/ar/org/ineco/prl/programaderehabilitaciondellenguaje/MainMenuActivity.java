package ar.org.ineco.prl.programaderehabilitaciondellenguaje;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.ApplicationContext;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.Menu;

public class MainMenuActivity extends Activity {

    private Menu menu = ApplicationContext.getMenu();

    @Override
    protected void onCreate (Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }


    public void startModulesActivity (View view) {

        menu.goToTop();

        Intent intent = new Intent(this, CategoriesActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void startLoginActivity (View view) {

        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void exitApp (View view) {

        finish();
    }
}
