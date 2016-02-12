package ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Question {
    private long id;
    private String text;
    private List<Option> opts;
    private List<ImageFile> imgs;

    public Question(long thisId, String thisText) {
        text = thisText;
        id = thisId;
        imgs = new ArrayList<>();
    }

    public List<Option> getOpts() {
        long seed = System.nanoTime();
        Collections.shuffle(opts, new Random(seed));
        return opts;
    }

    public void addOption(Option thisOpt) {
        opts.add(thisOpt);
    }

    public int getNOpts(){
        return opts.size();
    }

    public String getText() {
        return text;
    }


    public void addImage(ImageFile thisImage) {
        imgs.add(thisImage);
    }

    public List<ImageFile> getImages() {
        long seed = System.nanoTime();
        Collections.shuffle(imgs, new Random(seed));
        return imgs;
    }

    public long getId(){
        return id;
    }

    @Override
    public String toString(){
        return text;
    }

    public void addImages(List<ImageFile> allQuestionsImages) {
        imgs.addAll(allQuestionsImages);
    }
}
