package ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes;

import android.app.Application;

public class ApplicationContext extends Application {

    private static ApplicationContext instance;
    private static Menu menu;

    public static ApplicationContext get () {

        return instance;
    }

    public static Menu getMenu () {

        return menu;
    }

    @Override
    public void onCreate () {

        super.onCreate();

        instance = this;

        menu = Menu.getInstance();
    }
}
