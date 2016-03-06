package ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes;

import android.app.Application;

public class ApplicationContext extends Application {

    private static ApplicationContext instance;

    public static ApplicationContext get () {

        return instance;
    }

    @Override
    public void onCreate () {

        super.onCreate();

        instance = this;
    }
}
