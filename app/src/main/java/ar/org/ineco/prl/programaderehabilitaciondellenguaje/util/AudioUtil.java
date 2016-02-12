package ar.org.ineco.prl.programaderehabilitaciondellenguaje.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import ar.org.ineco.prl.programaderehabilitaciondellenguaje.R;

public class AudioUtil {
    private final int sdkVersion = Build.VERSION.SDK_INT;

    public final int TRIVIA_RIGHT_ANSWER;
    public final int TRIVIA_WRONG_ANSWER;

    private float rate = 1.0f;
    private float leftVolume = 1.0f;
    private float rightVolume = 1.0f;
    private SoundPool sndPool;
    private Context pContext;

    public AudioUtil(Context thisContext) {
        if (sdkVersion < Build.VERSION_CODES.LOLLIPOP) {
            sndPool = buildLessLollipop();
        } else {
            sndPool = buildLollipop();
        }

        TRIVIA_RIGHT_ANSWER = sndPool.load(thisContext, R.raw.acierto, 0);
        TRIVIA_WRONG_ANSWER = sndPool.load(thisContext, R.raw.error, 0);

        pContext = thisContext;
    }

    private SoundPool buildLessLollipop() {
        return new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private SoundPool buildLollipop() {
        SoundPool.Builder sndPoolBuilder = new SoundPool.Builder();
        sndPoolBuilder.setAudioAttributes(new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build());
        return sndPoolBuilder.build();
    }

    public int loadSound(int soundResourceID) {
        return sndPool.load(pContext, soundResourceID, 0);
    }

    public void playSound(int soundID) {
        sndPool.play(soundID, leftVolume, rightVolume, 1, 0, rate);
    }

    public void unloadAll() {
        sndPool.release();
    }
}
