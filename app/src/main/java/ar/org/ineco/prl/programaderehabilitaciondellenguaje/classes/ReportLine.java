package ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes;

public class ReportLine {

    public Category category;
    public int correctAns;
    public int wrongAns;
    public int totalTries;
    public float correctPerAns;
    public float wrongPerAns;

    public ReportLine (Category cat, int correct, int wrong) {

        category = cat;
        correctAns = correct;
        wrongAns = wrong;
        totalTries = correct + wrong;
        correctPerAns = correct*100/totalTries;
        wrongPerAns = wrong*100/totalTries;
    }
}
