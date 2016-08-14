package ar.org.ineco.prl.programaderehabilitaciondellenguaje;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class SplashActivity extends Activity {

    Thread splashThread;

    public void onAttachedToWindow () {

        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        StartAnimations();
    }

    private void StartAnimations () {

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();

        LinearLayout l = (LinearLayout) findViewById(R.id.logos);
        l.clearAnimation();
        l.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();

        ImageView caece = (ImageView) findViewById(R.id.caece);
        caece.clearAnimation();
        ImageView ineco = (ImageView) findViewById(R.id.ineco);
        ineco.clearAnimation();
        ImageView favaloro = (ImageView) findViewById(R.id.favaloro);
        favaloro.clearAnimation();

        caece.startAnimation(anim);
        ineco.startAnimation(anim);
        favaloro.startAnimation(anim);

        splashThread = new Thread() {

            @Override
            public void run () {

                try {

                    int waited = 0;

                    // Splash screen pause time
                    while (waited < 3500) {
                        sleep(100);
                        waited += 100;
                    }

                    Intent intent = new Intent(SplashActivity.this,
                            MainMenuActivity.class);
                    //intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                    startActivity(intent);
                    SplashActivity.this.finish();

                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    SplashActivity.this.finish();
                }

            }
        };

        splashThread.start();
    }
}
