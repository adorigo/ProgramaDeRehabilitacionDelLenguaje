package ar.org.ineco.prl.programaderehabilitaciondellenguaje.quiz;

public class Option {
    private String text;
    private long value;
    private long qid;

    public Option(long thisValue, String thisText, long thisQid){
        this.text = thisText;
        this.value = thisValue;
        this.qid = thisQid;
    }

    public String getStr() {
        return text;
    }

    public long getValue() {
        return value;
    }

    public long getQid(){
        return qid;
    }

    @Override
    public String toString(){
        return text;
    }
}
