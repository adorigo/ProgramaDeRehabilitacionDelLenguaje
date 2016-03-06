package ar.org.ineco.prl.programaderehabilitaciondellenguaje;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import ar.org.ineco.prl.programaderehabilitaciondellenguaje.util.Utils;

public class LoginActivity extends Activity {

    private EditText pinView;

    @Override
    protected void onCreate (Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pinView = (EditText) findViewById(R.id.textPin);
    }

    public void doLogin (View v) {

        if (pinView.getText().toString().equals(Utils.getPinPassword())) {
            Intent intent = new Intent(this, SettingsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            Toast.makeText(this, "PIN Incorrecto", Toast.LENGTH_SHORT).show();
        }
    }

    public void goBack (View v) {

        Intent intent = new Intent(this, MainMenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
