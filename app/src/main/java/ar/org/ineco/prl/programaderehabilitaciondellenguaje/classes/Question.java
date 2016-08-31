package ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import ar.org.ineco.prl.programaderehabilitaciondellenguaje.database.DatabaseLoader;

public class Question {

    private long id;
    private String text;
    private boolean done;
    private List<Option> opts;
    private List<ImageFile> imgs;
    private long parentQuestion;
    private List<Question> childQuestions;
    private SoundFile sound;

    public Question(long thisId, String thisText, int thisQuestionDone, long thisParentQuestion, SoundFile sndFile) {

        text = thisText;
        id = thisId;
        done = (thisQuestionDone == 1);
        parentQuestion = thisParentQuestion;
        imgs = new ArrayList<>();
        opts = new ArrayList<>();
        childQuestions = new ArrayList<>();
        sound = sndFile;
    }

    public List<Option> getOpts () {

        long seed = System.nanoTime();
        Collections.shuffle(opts, new Random(seed));
        return opts;
    }

    public SoundFile getSound() {
        return sound;
    }

    public void addOption (Option thisOpt) {

        opts.add(thisOpt);
    }

    public int getNOpts () {

        return opts.size();
    }

    public String getText () {

        return text;
    }

    public void addImage (ImageFile thisImage) {

        imgs.add(thisImage);
    }

    public void addImages (List<ImageFile> allQuestionsImages) {

        imgs.addAll(allQuestionsImages);
    }

    public List<ImageFile> getImages () {

        long seed = System.nanoTime();
        Collections.shuffle(imgs, new Random(seed));
        return imgs;
    }

    public long getId () {

        return id;
    }

    @Override
    public String toString () {

        return text;
    }

    public List<Question> getChildQuestions () {

        return childQuestions;
    }

    public void addChildQuestion (Question thisQuestion) {

        childQuestions.add(thisQuestion);
    }

    public Long getParentQuestion () {

        return parentQuestion;
    }

    public boolean hasParent () {

        return getParentQuestion() != null && getParentQuestion() != 0;
    }

    public void check () {

        this.done = true;
        DatabaseLoader.getInstance().checkQuestion(this);
    }

    public void reset () {

        this.done = false;
    }

    public boolean isDone () {

        return done;
    }
}
