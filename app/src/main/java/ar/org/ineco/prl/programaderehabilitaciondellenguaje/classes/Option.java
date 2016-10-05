package ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes;

import ar.org.ineco.prl.programaderehabilitaciondellenguaje.database.DatabaseLoader;

public class Option {

    private long id;
    private String text;
    private boolean isCorrect;
    private long qid;
    private ImageFile img;
    private SoundFile snd;

    public Option(long thisId, String thisText, int thisCorrect, long thisQid, ImageFile thisImg, SoundFile thisSnd) {

        id = thisId;
        text = thisText;
        isCorrect = (thisCorrect == 1);
        qid = thisQid;
        img = thisImg;
        snd = thisSnd;
    }

    public ImageFile getImg() {
        return img;
    }

    public SoundFile getSnd() {
        return snd;
    }

    public String getStr () {

        return text;
    }

    public long getId () {

        return id;
    }

    public long getQid () {

        return qid;
    }

    public boolean getIsCorrect() {
        return isCorrect;
    }

    public boolean checkAns () {

        DatabaseLoader.getInstance().createLogRecord(this, isCorrect);
        return isCorrect;
    }

    @Override
    public String toString () {

        return text;
    }
}
