package ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes;

public class Option {

    private long id;
    private String text;
    private boolean isCorrect;
    private long qid;

    public Option (long thisId, String thisText, int thisCorrect, long thisQid) {

        this.id = thisId;
        this.text = thisText;
        this.isCorrect = (thisCorrect == 1);
        this.qid = thisQid;
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

    public boolean checkAns () {

        return isCorrect;
    }

    @Override
    public String toString () {

        return text;
    }
}
