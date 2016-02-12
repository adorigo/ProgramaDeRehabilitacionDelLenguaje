package ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes;

public class Category {
    private int catNumber;
    private String catName;

    public Category(int thisCatNumber, String thisCatName) {
        catNumber = thisCatNumber;
        catName = thisCatName;
    }

    public String getCatName() {
        return catName;
    }

    public int getCatNumber() {
        return catNumber;
    }
}
