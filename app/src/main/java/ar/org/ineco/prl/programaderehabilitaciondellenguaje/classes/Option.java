package ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes;

public class Option {

    private String text;
    private long value;
    private int isCorrect;
    private long qid;

    public Option (long thisValue, String thisText, int thisCorrect, long thisQid) {

        this.text = thisText;
        this.isCorrect = thisCorrect;
        this.value = thisValue;
        this.qid = thisQid;
    }

    public String getStr () {

        return text;
    }

    public long getValue () {

        return value;
    }

    public long getQid () {

        return qid;
    }

    public boolean checkAns () {

        return isCorrect == 1;
    }

    @Override
    public String toString () {

        return text;
    }
}
