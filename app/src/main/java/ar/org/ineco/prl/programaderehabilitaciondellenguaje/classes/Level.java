package ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes;

import java.util.ArrayList;
import java.util.List;

import ar.org.ineco.prl.programaderehabilitaciondellenguaje.database.DatabaseLoader;

public class Level {

    private long id;
    private int number;
    private boolean done;
    private long catid;

    private List<Question> questions;

    public Level (long thisLVLId, int thisLVLNumber, int thisLVLDone, long thisCatNumber) {

        number = thisLVLNumber;
        id = thisLVLId;
        done = (thisLVLDone == 1);
        catid = thisCatNumber;
        questions = new ArrayList<>();
    }

    public String getLvlName () {

        return "Nivel " + String.valueOf(number);
    }

    public int getNumber () {

        return number;
    }

    public long getCatid () {

        return catid;
    }

    public long getId () {

        return id;
    }

    public boolean isDone () {

        return done;
    }

    public List<Question> getQuestions () {

        return questions;
    }

    public List<Question> getPendingQuestions () {

        List<Question> pendingQuestions = new ArrayList<>();

        for (Question question : this.questions) {

            if (!question.isDone()) {

                pendingQuestions.add(question);
            }
        }

        return pendingQuestions;
    }

    public void addAllQuestions (List<Question> thisQuestions) {

        questions.addAll(thisQuestions);
    }

    public void check () {

        this.done = true;
        DatabaseLoader.getInstance().checkLevel(this);
    }

    public void resetQuestions () {

        for (Question question : this.questions) {

            question.reset();
        }
        DatabaseLoader.getInstance().resetLevel(this);
    }
}
