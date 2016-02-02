package ar.org.ineco.prl.programaderehabilitaciondellenguaje.quiz;

import java.util.ArrayList;
import java.util.List;

public class Question {
    private long id;
    private long ans;
    private String text;
    private List<Option> opts;
    private List<String> imgs;

    public Question(long thisId, long thisAns, String thisText) {
        ans = thisAns;
        text = thisText;
        id = thisId;
        imgs = new ArrayList<>();
    }

    public boolean checkAns(Option guess) {
        return ans==guess.getValue();
    }

    public List<Option> getOpts() {
        return opts;
    }

    public void setOpts(List<Option> thisOpts) {
        opts= thisOpts;
    }

    public int getNOpts(){
        return opts.size();
    }

    public String getText() {
        return text;
    }


    public void addImage(String thisImage) {
        imgs.add(thisImage);
    }

    public List<String> getImages() {
        return imgs;
    }

    public long getId(){
        return id;
    }

    @Override
    public String toString(){
        return text;
    }
}
