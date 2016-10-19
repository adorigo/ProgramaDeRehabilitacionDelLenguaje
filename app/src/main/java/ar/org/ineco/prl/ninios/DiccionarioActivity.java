package ar.org.ineco.prl.ninios;


import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

import ar.org.ineco.prl.ninios.classes.ApplicationContext;
import ar.org.ineco.prl.ninios.classes.Option;
import ar.org.ineco.prl.ninios.classes.Question;
import ar.org.ineco.prl.ninios.util.OptionAdapter;

public class DiccionarioActivity extends BaseActivity{

    HashMap<Long, Integer> soundMap = new HashMap<>();

    @Override
    protected int getStringResourceId () {

        return R.string.title_activity_diccionario;
    }

    @Override
    protected int getLayoutResourceId () {

        return R.layout.activity_diccionario;
    }

    @Override
    public void drawUI () {

        ArrayList<Option> options = new ArrayList<>();

        for (Question question : menu.getCurrentLevel().getPendingQuestions()) {
            options.addAll(question.getOpts());
        }

        OptionAdapter adapter = new OptionAdapter(this, options);

        GridView grid = (GridView) findViewById(R.id.images);
        grid.setAdapter(adapter);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {

                Option option = (Option) view.getTag();

                playSound(getResources().getIdentifier(option.getSnd().getName(), "raw", getApplicationContext().getPackageName()));
            }
        });
    }
}
