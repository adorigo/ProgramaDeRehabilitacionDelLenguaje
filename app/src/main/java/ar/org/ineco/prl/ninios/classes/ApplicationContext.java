package ar.org.ineco.prl.ninios.classes;

import android.app.Application;

import ar.org.ineco.prl.ninios.util.AudioUtil;

public class ApplicationContext extends Application {

    private static ApplicationContext instance;
    private static Menu menu;
    private static AudioUtil sndUtil;
    public static ApplicationContext get () {

        return instance;
    }

    public static Menu getMenu () {

        return menu;
    }

    public static AudioUtil getSndUtil () {

        return sndUtil;
    }

    @Override
    public void onCreate () {

        super.onCreate();

        instance = this;

        menu = Menu.getInstance();
        sndUtil = AudioUtil.getInstance(this);
    }
}
