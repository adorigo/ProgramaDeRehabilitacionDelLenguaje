package ar.org.ineco.prl.ninios.classes;

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
        correctPerAns = (float) correct * 100 / (float) totalTries;
        wrongPerAns = (float) wrong * 100 / (float) totalTries;
    }
}
