package ar.org.ineco.prl.programaderehabilitaciondellenguaje;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import ar.org.ineco.prl.programaderehabilitaciondellenguaje.util.VerdanaTextView;

public class SplashActivityAbout extends Activity {

    Thread splashThread;

    public void onAttachedToWindow () {

        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_last);
        StartAnimations();
    }

    private void StartAnimations () {

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();

        FrameLayout layoutAbout = (FrameLayout) findViewById(R.id.frameAbout);
        layoutAbout.clearAnimation();
        layoutAbout.setAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();

        VerdanaTextView about = (VerdanaTextView) findViewById(R.id.tViewAbout);
        about.clearAnimation();
        VerdanaTextView about1 = (VerdanaTextView) findViewById(R.id.tViewAbout1);
        about1.clearAnimation();
        VerdanaTextView about4 = (VerdanaTextView) findViewById(R.id.tViewAbout4);
        about4.clearAnimation();
        VerdanaTextView about2 = (VerdanaTextView) findViewById(R.id.tViewAbout2);
        about2.clearAnimation();
        VerdanaTextView about3 = (VerdanaTextView) findViewById(R.id.tViewAbout3);
        about3.clearAnimation();

        about.startAnimation(anim);
        about1.startAnimation(anim);
        about4.startAnimation(anim);
        about2.startAnimation(anim);
        about3.startAnimation(anim);

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

                    Intent intent = new Intent(SplashActivityAbout.this,
                            MainMenuActivity.class);
                    //intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                    startActivity(intent);
                    SplashActivityAbout.this.finish();

                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    SplashActivityAbout.this.finish();
                }

            }
        };

        splashThread.start();
    }
}
