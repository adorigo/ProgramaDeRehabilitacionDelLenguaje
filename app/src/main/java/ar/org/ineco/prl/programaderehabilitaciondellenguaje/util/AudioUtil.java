package ar.org.ineco.prl.programaderehabilitaciondellenguaje.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import ar.org.ineco.prl.programaderehabilitaciondellenguaje.R;

public class AudioUtil {

    private static SoundPool sndPool;
    private static Map<Integer, Integer> soundsIds;
    private static Context pContext;

    private static AudioUtil ourInstance;

    public static AudioUtil getInstance (Context context) {

        if (ourInstance == null) {
            ourInstance = new AudioUtil(context);
        }
        return ourInstance;
    }

    private AudioUtil (Context thisContext) {

        int sdkVersion = Build.VERSION.SDK_INT;

        if (sdkVersion < Build.VERSION_CODES.LOLLIPOP) {
            sndPool = buildLessLollipop();
        } else {
            sndPool = buildLollipop();
        }

        soundsIds = new HashMap<>();

        sndPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                Log.d(AudioUtil.class.getName(), "Sample ready: " + sampleId);
            }
        });

        soundsIds.put(R.raw.acierto, sndPool.load(thisContext, R.raw.acierto, 1));
        soundsIds.put(R.raw.error, sndPool.load(thisContext, R.raw.error, 1));

        pContext = thisContext;
    }

    @SuppressWarnings("deprecation")
    private static SoundPool buildLessLollipop () {

        return new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static SoundPool buildLollipop () {

        SoundPool.Builder sndPoolBuilder = new SoundPool.Builder();
        sndPoolBuilder.setAudioAttributes(new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build());
        return sndPoolBuilder.build();
    }

    public void loadSound(int soundResourceID) {

        if (!soundsIds.containsKey(soundResourceID)) {
            soundsIds.put(soundResourceID, sndPool.load(pContext, soundResourceID, 1));
        }
    }

    public void playSound(int soundResourceID) {

        float rightVolume = 1.0f;
        float leftVolume = 1.0f;
        float rate = 1.0f;
        sndPool.play(soundsIds.get(soundResourceID), leftVolume, rightVolume, 1, 0, rate);
    }

    public void unloadAll () {

        sndPool.release();
    }
}
